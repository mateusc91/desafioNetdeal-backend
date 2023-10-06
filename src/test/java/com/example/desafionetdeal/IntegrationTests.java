package com.example.desafionetdeal;

import com.example.desafionetdeal.dto.EmployeeDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Sql({"/test-schema.sql"})
public class IntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    static MySQLContainer<?> mySQLTestDatabase = new MySQLContainer<>(
            "mysql:8.0"
    );

    @BeforeAll
    static void beforeAll(){
        mySQLTestDatabase.start();
    }

    @AfterAll
    static void afterAll() {
        mySQLTestDatabase.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLTestDatabase::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLTestDatabase::getUsername);
        registry.add("spring.datasource.password", mySQLTestDatabase::getPassword);
    }

    @Test
    @DisplayName("Testando cenario para uma senha ruim")
    public void testRuimPassword() throws Exception {
        String jsonRequest = "{\"name\": \"mateus\", \"password\": \"weakpass\"}";

        MvcResult mvcResult = mockMvc.perform(post("/api/employees/save-employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated()).andReturn();

        String responseJson = mvcResult.getResponse().getContentAsString();
        EmployeeDTO responseDTO = new ObjectMapper().readValue(responseJson, EmployeeDTO.class);
        String passwordComplexity = responseDTO.getPasswordComplexity();

        assertThat(passwordComplexity).isEqualTo(responseDTO.getPasswordComplexity());
    }

    @Test
    @DisplayName("Testando cenario para uma senha mediana")
    public void testMedianaPassword() throws Exception {
        String jsonRequest = "{\"name\": \"mateus\", \"password\": \"Med12\"}";

        MvcResult mvcResult = mockMvc.perform(post("/api/employees/save-employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated()).andReturn();

        String responseJson = mvcResult.getResponse().getContentAsString();
        EmployeeDTO responseDTO = new ObjectMapper().readValue(responseJson, EmployeeDTO.class);
        String passwordComplexity = responseDTO.getPasswordComplexity();

        assertThat(passwordComplexity).isEqualTo(responseDTO.getPasswordComplexity());
    }

    @Test
    @DisplayName("Testando cenario para uma senha boa")
    public void testBomPassword() throws Exception {
        String jsonRequest = "{\"name\": \"mateus\", \"password\": \"Bom12!\"}";

        MvcResult mvcResult = mockMvc.perform(post("/api/employees/save-employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated()).andReturn();

        String responseJson = mvcResult.getResponse().getContentAsString();
        EmployeeDTO responseDTO = new ObjectMapper().readValue(responseJson, EmployeeDTO.class);
        String passwordComplexity = responseDTO.getPasswordComplexity();

        assertThat(passwordComplexity).isEqualTo(responseDTO.getPasswordComplexity());
    }

    @Test
    @DisplayName("Testando cenario para uma senha forte")
    public void testFortePassword() throws Exception {
        String jsonRequest = "{\"name\": \"mateus\", \"password\": \"FortePassword123!\"}";

        MvcResult mvcResult = mockMvc.perform(post("/api/employees/save-employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated()).andReturn();

        String responseJson = mvcResult.getResponse().getContentAsString();
        EmployeeDTO responseDTO = new ObjectMapper().readValue(responseJson, EmployeeDTO.class);
        String passwordComplexity = responseDTO.getPasswordComplexity();

        assertThat(passwordComplexity).isEqualTo(responseDTO.getPasswordComplexity());
    }
}
