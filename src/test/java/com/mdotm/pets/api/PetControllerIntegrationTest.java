package com.mdotm.pets.api;

import com.mdotm.pets.dao.PetJpaRepository;
import com.mdotm.pets.dao.PetRepository;
import com.mdotm.pets.model.PetDocument;
import com.mdotm.pets.model.PetJpaDocument;
import com.mdotm.pets.service.SequenceGeneratorService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.Instant;
import static com.mdotm.pets.model.Species.CAT;
import static com.mdotm.pets.model.Species.DOG;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PetControllerIntegrationTest {

    private final String API_PATH = "/api/v1/pets";
    private Long PET_ID;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @BeforeEach
    public void setUp() {
        PET_ID= sequenceGeneratorService.generateSequence("pet_sequence");
        Instant now = Instant.now();
        PetDocument pet = new PetDocument(
                PET_ID,
                "lilli",
                CAT,
                3,
                "Mike",
                now,
                now
        );
        PetDocument pet2 = new PetDocument(
                sequenceGeneratorService.generateSequence("pet_sequence"),
                "tommy",
                DOG,
                0,
                "Alessia",
                now,
                now
        );
        petRepository.save(pet).toPetResponse();
        petRepository.save(pet2);
    }

    @AfterEach
    public void cleanUp() {
        petRepository.deleteAll();
    }


    @Test
    public void createPet_OK() throws Exception {
        String requestBody = """
        {
            "name": "tommy",
            "species": "DOG",
            "age": 10,
            "owner_name": "Alberto"
        }
        """;

        mockMvc.perform(post(API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("tommy"))
                .andExpect(jsonPath("$.species").value("DOG"))
                .andExpect(jsonPath("$.age").value(10))
                .andExpect(jsonPath("$.owner_name").value("Alberto"));
    }

    @Test
    public void createPet_Already_Exist_KO() throws Exception {
        String requestBody = """
        {
            "name": "tommy",
            "species": "DOG",
            "age": 10,
            "owner_name": "Alberto"
        }
        """;

        //first create one pet
        mockMvc.perform(post(API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        //now trying to repeat creating the same pet
        mockMvc.perform(post(API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error_code").value("ALREADY_EXIST_ERROR"))
                .andExpect(jsonPath("$.error_message").value("Pet: tommy with owner: Alberto for species: DOG already exists"));
    }

    @Test
    public void createPet_Empty_Name_KO() throws Exception {
        String requestBody = """
        {
            "name": "",
            "species": "DOG",
            "age": 10,
            "owner_name": "Alberto"
        }
        """;

        mockMvc.perform(post(API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error_code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.error_message").value("name length too short"));
    }

    @Test
    public void createPet_Empty_OwnerName_KO() throws Exception {
        String requestBody = """
        {
            "name": "tommy",
            "species": "DOG",
            "age": 10,
            "owner_name": ""
        }
        """;

        mockMvc.perform(post(API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error_code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.error_message").value("owner_name length too short"));
    }

    @Test
    public void createPet_Negative_Age_KO() throws Exception {
        String requestBody = """
        {
            "name": "tommy",
            "species": "DOG",
            "age": -10,
            "owner_name": "Alberto"
        }
        """;

        mockMvc.perform(post(API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error_code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.error_message").value("age must be greater then or equal to zero"));
    }

    @Test
    public void getPet_byId_OK() throws Exception {
        mockMvc.perform(get(API_PATH + "/" + PET_ID ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("lilli"))
                .andExpect(jsonPath("$.species").value("CAT"))
                .andExpect(jsonPath("$.age").value(3))
                .andExpect(jsonPath("$.owner_name").value("Mike"));
    }

    @Test
    public void getPet_NOT_Found_KO() throws Exception {
        mockMvc.perform(get(API_PATH + "/" + 123456 ))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error_code").value("NOT_FOUND_ERROR"))
                .andExpect(jsonPath("$.error_message").value("Pet with id: 123456 not found"));
    }

    @Test
    public void getAllPets_Ok() throws Exception {
        mockMvc.perform(get(API_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content.[0].name").value("lilli"))
                .andExpect(jsonPath("$.content.[0].species").value("CAT"))
                .andExpect(jsonPath("$.content.[0].age").value(3))
                .andExpect(jsonPath("$.content.[0].owner_name").value("Mike"))
                .andExpect(jsonPath("$.content.[1].name").value("tommy"))
                .andExpect(jsonPath("$.content.[1].species").value("DOG"))
                .andExpect(jsonPath("$.content.[1].age").value(0))
                .andExpect(jsonPath("$.content.[1].owner_name").value("Alessia"))
                .andExpect(jsonPath("$.size").value(10));
    }

    @Test
    public void updatePet_OK() throws Exception {
        String requestBody = """
        {
            "name": "tom",
            "species": "MONKEY",
            "age": 2,
            "owner_name": "Pepe"
        }
        """;

        mockMvc.perform(put(API_PATH + "/" + PET_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("tom"))
                .andExpect(jsonPath("$.species").value("MONKEY"))
                .andExpect(jsonPath("$.age").value(2))
                .andExpect(jsonPath("$.owner_name").value("Pepe"));
    }

    @Test
    public void updatePet_NOT_Found_KO() throws Exception {
        String requestBody = """
        {
            "name": "tom",
            "species": "MONKEY",
            "age": 2,
            "owner_name": "Pepe"
        }
        """;

        mockMvc.perform(put(API_PATH + "/44345")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error_code").value("NOT_FOUND_ERROR"))
                .andExpect(jsonPath("$.error_message").value("Pet with id: 44345 not found"));
    }

    @Test
    public void deletePet_byId_OK() throws Exception {
        mockMvc.perform(delete(API_PATH + "/" + PET_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deletePet_byId_NOT_Found_KO() throws Exception {
        mockMvc.perform(delete(API_PATH + "/44444"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error_code").value("NOT_FOUND_ERROR"))
                .andExpect(jsonPath("$.error_message").value("Pet with id: 44444 not found"));
    }
}
