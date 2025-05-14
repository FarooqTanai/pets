package com.mdotm.pets.service;

import com.mdotm.pets.model.PetRequest;
import com.mdotm.pets.model.PetResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PetService {

    PetResponse createPet(PetRequest pet);

    PetResponse getPet(Long id);

    Page<PetResponse> getAllPets(Pageable pageable);

    PetResponse updatePet(Long id, PetRequest pet);

    void deletePet(Long id);

}
