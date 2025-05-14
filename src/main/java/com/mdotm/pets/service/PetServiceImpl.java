package com.mdotm.pets.service;

import com.mdotm.pets.dao.PetJpaRepository;
import com.mdotm.pets.dao.PetRepository;
import com.mdotm.pets.exception.GenericException;
import com.mdotm.pets.exception.PetAlreadyExistException;
import com.mdotm.pets.exception.PetNotFoundException;
import com.mdotm.pets.model.PetDocument;
import com.mdotm.pets.model.PetJpaDocument;
import com.mdotm.pets.model.PetRequest;
import com.mdotm.pets.model.PetResponse;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
public class PetServiceImpl implements PetService {

    @Nonnull
    private final PetRepository petRepository;

    @Nonnull
    private final SequenceGeneratorService sequenceGeneratorService;


    public PetServiceImpl(@Nonnull PetRepository petRepository, @Nonnull SequenceGeneratorService sequenceGeneratorService) {
        this.petRepository = petRepository;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    /**
     * Creates a new pet.
     *
     * @param pet the pet request object containing details
     * @return the created pet response
     * @throws PetAlreadyExistException if a pet with the same name, species, and owner already exists
     * @throws GenericException if saving the pet fails
     */
    @Override
    @Nonnull
    public PetResponse createPet(@Nonnull PetRequest pet) {
        petRepository.findByNameAndSpeciesAndOwnerName(pet.name(), pet.species(), pet.ownerName())
                .ifPresent(existing -> {
                    throw new PetAlreadyExistException(
                            String.format("Pet: %s with owner: %s for species: %s already exists", pet.name(), pet.ownerName(), pet.species()));
                });
        var now = Instant.now();
        var id = sequenceGeneratorService.generateSequence("pet_sequence");
        var petDocument = new PetDocument(id, pet.name(), pet.species(), pet.age(), pet.ownerName(), now, now);
        try {
            return petRepository.save(petDocument).toPetResponse();
        } catch (Exception e) {
            log.error("Error saving pet", e);
            throw new GenericException("Failed to save pet");
        }
    }

    /**
     * Retrieves a pet by its ID.
     *
     * @param id the pet ID
     * @return the pet response
     * @throws PetNotFoundException if the pet is not found
     */
    @Override
    @Nonnull
    public PetResponse getPet(Long id) {
        return findPetById(id).toPetResponse();
    }

    /**
     * Retrieves all pets in a paginated format.
     *
     * @param pageable the pagination information
     * @return a page of pet responses
     */
    @Override
    @Nonnull
    public Page<PetResponse> getAllPets(Pageable pageable) {
        return petRepository.findAll(pageable).map(PetDocument::toPetResponse);
    }

    /**
     * Updates a pet by ID.
     *
     * @param id the pet ID
     * @param petRequest the updated pet information
     * @return the updated pet response
     * @throws PetNotFoundException if the pet is not found
     * @throws GenericException if the update fails
     */
    @Override
    @Nonnull
    public PetResponse updatePet(Long id, @Nonnull PetRequest petRequest) {
        var existing = findPetById(id);
        try {
            existing.setName(petRequest.name());
            existing.setSpecies(petRequest.species());
            existing.setAge(petRequest.age());
            existing.setOwnerName(petRequest.ownerName());
            existing.setLastModified(Instant.now());
            return petRepository.save(existing).toPetResponse();
        } catch (Exception e) {
            log.error("Error updating pet: {}" , e);
            throw new GenericException(e.getMessage());
        }
    }

    /**
     * Deletes a pet by ID.
     *
     * @param id the pet ID
     * @throws PetNotFoundException if the pet is not found
     * @throws GenericException if the deletion fails
     */
    @Override
    @Nonnull
    public void deletePet(Long id) {
        var petDocument = findPetById(id);
        try {
            petRepository.delete(petDocument);
        }  catch (Exception e) {
            log.error("Error deleting pet with Id: {}, error: {}" , id, e);
            throw new GenericException(e.getMessage());
        }
    }

    @Nonnull
    private PetDocument findPetById(Long id) {
        return petRepository.findById(id).orElseThrow(() ->
                new PetNotFoundException(String.format("Pet with id: %s not found", id)));
    }
}
