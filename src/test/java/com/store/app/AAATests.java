package com.store.app;

import com.store.ElectronicStoreApplication;
import com.store.security.entity.Role;
import com.store.security.entity.User;
import com.store.security.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ElectronicStoreApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class AAATests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        // Insert test users into H2 in-memory database
        userRepository.save(new User(null, "admin", passwordEncoder.encode("admin123"), Role.ADMIN));
        userRepository.save(new User(null, "cust", passwordEncoder.encode("cust123"), Role.CUSTOMER));
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void testLoginAsAdmin() throws Exception {
        String adminToken = getToken("admin", "admin123");
        assertTrue(adminToken.contains("token:"));
    }

    @Test
    void testAddNewAdmin() throws Exception {
        String addedUser = addUser("test", "test123", "admin");
        assertTrue(addedUser.equals("User registered successfully"));
        String adminToken = getToken("test", "test123");
        assertTrue(adminToken.contains("token:"));
    }

    @Test
    void testAddNewCustomer() throws Exception {
        String addedUser = addUser("custnew", "custnew123", "customer");
        assertTrue(addedUser.equals("User registered successfully"));
        String adminToken = getToken("custnew", "custnew123");
        assertTrue(adminToken.contains("token:"));
    }

    @Test
    void testLoginAsCustomer() throws Exception {
        String custToken = getToken("cust", "cust123");
        assertTrue(custToken.contains("token:"));
    }

    // Helper method to get JWT token for authentication
    private String getToken(String username, String password) throws Exception {
        String userJson = "{\"username\": \"" + username + "\",\"password\": \"" + password + "\"}";
        String response = mockMvc.perform(post("/api/auth/login")
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return response.replace("\"", "");
    }

    private String addUser(String username, String password, String role) throws Exception {
        String userJson = "{\"username\": \"" + username + "\",\"password\": \"" + password + "\",\"role\": \"" + role + "\"}";
        String response = mockMvc.perform(post("/api/auth/register")
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return response.replace("\"", "");
    }
}
