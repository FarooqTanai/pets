package com.mdotm.pets.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mdotm.pets.exception.ValidationException;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record PetRequest(@Nonnull
                         @JsonProperty(value = "name")
                         String name,

                         @Nonnull
                         @JsonProperty(value = "species")
                         Species species,

                         @Nullable
                         @JsonProperty(value = "age")
                         Integer age,

                         @Nullable
                         @JsonProperty(value = "owner_name")
                         String ownerName) {
    public PetRequest {
        validate(name, age, ownerName);
    }

    private void validate(@Nonnull String name, @Nullable Integer age, @Nullable String ownerName) {
        if (name.isEmpty() || name.length() < 2) {
            throw new ValidationException("name length too short");
        }
        if (age != null && age < 0) {
            throw new ValidationException("age must be greater then or equal to zero");
        }
        if (ownerName.isEmpty() || ownerName.length() < 2) {
            throw new ValidationException("owner_name length too short");
        }
    }
}
