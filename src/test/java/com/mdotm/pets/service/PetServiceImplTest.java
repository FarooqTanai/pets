package com.mdotm.pets.service;

import com.mdotm.pets.dao.PetRepository;
import com.mdotm.pets.exception.*;
import com.mdotm.pets.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.time.Instant;
import java.util.*;

import static com.mdotm.pets.model.Species.MONKEY;
import static com.mdotm.pets.model.Species.RABBIT;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PetServiceImplTest {

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private PetServiceImpl petService;

    private PetRequest petRequest;
    private PetDocument petDocument;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        petRequest = new PetRequest("Tommy", MONKEY, 3, "Khan");
        petDocument = new PetDocument(1L, "Tommy", MONKEY, 3, "Khan", Instant.now(), Instant.now());
    }

    @Test
    public void createPet_OK() {
        when(petRepository.findByNameAndSpeciesAndOwnerName(any(), any(), any()))
                .thenReturn(Optional.empty());
        when(petRepository.save(any())).thenReturn(petDocument);

        PetResponse response = petService.createPet(petRequest);

        assertThat(response.name()).isEqualTo("Tommy");
        assertThat(response.species()).isEqualTo(MONKEY);
        assertThat(response.age()).isEqualTo(3);
        assertThat(response.ownerName()).isEqualTo("Khan");
        verify(petRepository).save(any());
    }

    @Test
    public void createPet_AlreadyExist_KO() {
        when(petRepository.findByNameAndSpeciesAndOwnerName(any(), any(), any()))
                .thenReturn(Optional.of(petDocument));

        assertThatThrownBy(() -> petService.createPet(petRequest))
                .isInstanceOf(PetAlreadyExistException.class);
    }

    @Test
    public void getPet_OK() {
        when(petRepository.findById(1L)).thenReturn(Optional.of(petDocument));

        PetResponse response = petService.getPet(1L);

        assertThat(response.name()).isEqualTo("Tommy");
        assertThat(response.species()).isEqualTo(MONKEY);
        assertThat(response.age()).isEqualTo(3);
        assertThat(response.ownerName()).isEqualTo("Khan");
    }

    @Test
    public void getPet_NOT_Found_KO() {
        when(petRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> petService.getPet(1L))
                .isInstanceOf(PetNotFoundException.class);
    }

    @Test
    public void getAllPets_OK() {
        Page<PetDocument> page = new PageImpl<>(List.of(petDocument));
        when(petRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<PetResponse> result = petService.getAllPets(PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).name()).isEqualTo("Tommy");
        assertThat(result.getContent().get(0).species()).isEqualTo(MONKEY);
        assertThat(result.getContent().get(0).age()).isEqualTo(3);
        assertThat(result.getContent().get(0).ownerName()).isEqualTo("Khan");
    }

    @Test
    public void updatePet_OK() {
        when(petRepository.findById(1L)).thenReturn(Optional.of(petDocument));
        when(petRepository.save(any())).thenReturn(petDocument);

        PetRequest updated = new PetRequest("Rabi", RABBIT, 4, "Jack");
        PetResponse response = petService.updatePet(1L, updated);

        assertThat(response.name()).isEqualTo("Rabi");
        assertThat(response.species()).isEqualTo(RABBIT);
        assertThat(response.age()).isEqualTo(4);
        assertThat(response.ownerName()).isEqualTo("Jack");
        verify(petRepository).save(any());
    }

    @Test
    public void deletePet_OK() {
        when(petRepository.findById(1L)).thenReturn(Optional.of(petDocument));
        doNothing().when(petRepository).delete(any());

        petService.deletePet(1L);
        verify(petRepository).delete(petDocument);
    }

    @Test
    public void deletePet_NOT_Found_KO() {
        when(petRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> petService.deletePet(1L))
                .isInstanceOf(PetNotFoundException.class);
    }
}
