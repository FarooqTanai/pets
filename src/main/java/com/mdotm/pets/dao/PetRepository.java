package com.mdotm.pets.dao;

import com.mdotm.pets.model.PetDocument;
import com.mdotm.pets.model.Species;
import jakarta.annotation.Nonnull;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PetRepository extends MongoRepository<PetDocument, Long> {

    Optional<PetDocument> findByNameAndSpeciesAndOwnerName(@Nonnull String name, @Nonnull Species species, @Nonnull String ownerName);
}
