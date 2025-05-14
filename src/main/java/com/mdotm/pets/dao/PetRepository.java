package com.mdotm.pets.dao;

import com.mdotm.pets.model.PetDocument;
import com.mdotm.pets.model.Species;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PetRepository extends JpaRepository<PetDocument, Long> {

    Optional<PetDocument> findByNameAndSpeciesAndOwnerName(@Nonnull String name, @Nonnull Species species, @Nonnull String ownerName);
}
