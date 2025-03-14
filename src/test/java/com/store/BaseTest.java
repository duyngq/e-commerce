package com.store;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.auth.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ElectronicStoreApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class BaseTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected UserRepository userRepository;

    protected PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    protected void setUp() throws Exception {
    }

    @AfterEach
    void tearDown() {
    }

    protected String login(String username, String password) throws Exception {
        String userJson = "{\"username\": \"" + username + "\",\"password\": \"" + password + "\"}";
        String response = mockMvc.perform(post("/api/auth/login")
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return response;
    }

    protected String getToken(String username, String password) throws Exception {
        String tokenJson = login(username, password);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(tokenJson);
        return jsonNode.get("token").asText();
    }

    protected String addUser(String username, String password, String role) throws Exception {
        String userJson = "{\"username\": \"" + username + "\",\"password\": \"" + password + "\",\"role\": \"" + role + "\"}";
        String response = mockMvc.perform(post("/api/auth/register")
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return response;
    }
}
