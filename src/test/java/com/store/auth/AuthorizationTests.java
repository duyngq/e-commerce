package com.store.auth;

import com.store.BaseTest;
import com.store.product.entity.Product;
import com.store.product.model.request.ProductRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.HashSet;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)  // Allows non-static @BeforeAll
public class AuthorizationTests extends BaseTest {
    private String token;

    @BeforeAll
    void loginAndGetToken() throws Exception {
        token = getToken("customer", "cust123");
    }

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    void testGetProductWithIncorrectRole() throws Exception {
        mockMvc.perform(get("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetDiscountWithIncorrectRole() throws Exception {
        mockMvc.perform(get("/api/v1/discounts/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void testPutActionWithIncorrectRole() throws Exception {
        String updatedJson = "{\"name\": \"Gaming Laptop\", \"price\": 1200.00}";

        mockMvc.perform(put("/api/v1/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedJson)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void testPostActionWithIncorrectRole() throws Exception {
        String json = "{\"name\": \"Gaming Laptop\", \"price\": 1200.00}";
        mockMvc.perform(post("/api/v1/products")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void testDeleteActionWithIncorrectRole() throws Exception {
        String deletedJson = "[1]";
        mockMvc.perform(delete("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deletedJson)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

}
