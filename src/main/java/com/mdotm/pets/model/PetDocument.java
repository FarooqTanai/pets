package com.mdotm.pets.model;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pet")
public class PetDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nonnull
    @Column(name = "name")
    private String name;

    @Nonnull
    @Column(name = "species")
    private Species species;

    @Nullable
    @Column(name = "age")
    private Integer age;

    @Nullable
    @Column(name = "owner_name")
    private String ownerName;

    @Nonnull
    @Column(name = "created_at")
    private Instant createAt;

    @Nonnull
    @Column(name = "last_modified")
    private Instant lastModified;

    @Nonnull
    public PetResponse toPetResponse() {
        return new PetResponse(id, name, species, age, ownerName, createAt, lastModified);
    }
}
