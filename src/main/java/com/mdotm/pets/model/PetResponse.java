package com.mdotm.pets.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.time.Instant;

public record PetResponse(@Nonnull
                          @JsonProperty(value = "id")
                          Long id,

                          @Nonnull
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
                          String ownerName,

                          @Nonnull
                          @JsonProperty(value = "created_at")
                          Instant createdAt,

                          @Nonnull
                          @JsonProperty(value = "last_modified")
                          Instant lastModified) {
}
