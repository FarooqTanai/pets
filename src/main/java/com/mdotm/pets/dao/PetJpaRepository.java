package com.mdotm.pets.dao;

import com.mdotm.pets.model.PetJpaDocument;
import com.mdotm.pets.model.Species;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PetJpaRepository extends JpaRepository<PetJpaDocument, Long> {

    Optional<PetJpaDocument> findByNameAndSpeciesAndOwnerName(@Nonnull String name, @Nonnull Species species, @Nonnull String ownerName);
}
