package com.mdotm.pets.api;

import com.mdotm.pets.model.PetRequest;
import com.mdotm.pets.model.PetResponse;
import com.mdotm.pets.service.PetService;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pets")
public class PetController {

    private final int DEFAULT_PAGE = 0;
    private final int DEFAULT_PAGE_SIZE = 10;

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    /**
     * Creates a new pet.
     *
     * @param pet the pet object to create
     * @return the created pet
     */
    @PostMapping(produces = "application/json")
    public ResponseEntity<PetResponse> createPet(@RequestBody @Nonnull PetRequest pet) {
        return ResponseEntity.ok(petService.createPet(pet));
    }

    /**
     * Retrieves a pet by its ID.
     *
     * @param id the ID of the pet
     * @return the pet details
     */
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<PetResponse> getPet(@PathVariable Long id) {
        return ResponseEntity.ok(petService.getPet(id));
    }

    /**
     * Retrieves all pets with pagination.
     *
     * @param pageable pagination and sorting parameters
     * @return a page of pet responses
     */
    @GetMapping(produces = "application/json")
    public ResponseEntity<Page<PetResponse>> getAllPets(@PageableDefault(page = DEFAULT_PAGE, size = DEFAULT_PAGE_SIZE) Pageable pageable) {
        return ResponseEntity.ok(petService.getAllPets(pageable));
    }

    /**
     * Updates an existing pet by ID.
     *
     * @param id  the ID of the pet to update
     * @param pet the updated pet data
     * @return the updated pet
     */
    @PutMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<PetResponse> update(@PathVariable Long id, @Nonnull @RequestBody PetRequest pet) {
        return ResponseEntity.ok(petService.updatePet(id, pet));
    }

    /**
     * Deletes a pet by ID.
     *
     * @param id the ID of the pet to delete
     * @return HTTP 204 No Content on success
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        petService.deletePet(id);
        return ResponseEntity.noContent().build();
    }
}
