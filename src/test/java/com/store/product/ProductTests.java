package com.store.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.BaseTest;
import com.store.product.entity.Product;
import com.store.product.entity.ProductDiscount;
import com.store.product.model.request.ProductRequest;
import com.store.product.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.HashSet;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductTests extends BaseTest {
    @Autowired
    private ProductRepository productRepository;

    private Product savedProduct;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private String token;

    @BeforeAll
    void loginAndGetToken() throws Exception {
        token = getToken("admin", "admin123");
    }

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }

    @Test
    void testCreateProduct() throws Exception {
        String productJson = "{\"name\": \"iPhone\", \"price\": 500.00}";

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("iPhone")))
                .andExpect(jsonPath("$.price", is(500.00)));

        mockMvc.perform(get("/api/v1/products?page=0&size=10")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));

    }

    @Test
    void testGetAllProducts() throws Exception {
        mockMvc.perform(get("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", greaterThanOrEqualTo(1)));
    }

    @Test
    void testUpdateProduct() throws Exception {
        String updatedJson = "{\"name\": \"Gaming Laptop\", \"price\": 1200.00}";
        savedProduct = productRepository.save(new Product(null, "RAM", new BigDecimal("1000.00"), new HashSet<>()));
        mockMvc.perform(put("/api/v1/products/" + savedProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedJson)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Gaming Laptop")))
                .andExpect(jsonPath("$.price", is(1200.00)));
    }

    @Test
    void testDeleteProduct() throws Exception {
        savedProduct = productRepository.save(new Product(null, "RAM", new BigDecimal("1000.00"), new HashSet<>()));

        String deletedJson = "[" + savedProduct.getId() + "]";
        mockMvc.perform(delete("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deletedJson)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/products/" + savedProduct.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void getProductByValidId() throws Exception {
        savedProduct = productRepository.save(new Product(null, "RAM", new BigDecimal("1000.00"), new HashSet<>()));

        mockMvc.perform(get("/api/v1/products/" + savedProduct.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("RAM"))
                .andExpect(jsonPath("$.price").value(1000.00));
    }

    @Test
    void testGetProductById_NonExisting() throws Exception {
        mockMvc.perform(get("/api/v1/products/999")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateProduct_NonExisting() throws Exception {
        String request = "{\"name\":\"Updated\",\"price\":2000.00}";
        mockMvc.perform(put("/api/v1/products/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(request))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(1)
    void testUpdateProductWithDiscount() throws Exception {
        String productDiscountJson = "[{\"productId\":1, \"discountId\":2},{\"productId\":2, \"discountId\":2},{\"productId\":3, \"discountId\":3}]";
        mockMvc.perform(put("/api/v1/products/discounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productDiscountJson)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$[0].productId", is(2)))
                .andExpect(jsonPath("$[0].discountId", is(2)))
                .andExpect(jsonPath("$[1].productId", is(3)))
                .andExpect(jsonPath("$[1].discountId", is(3)));
    }
/*
    @Test
    void updateProduct_Valid() throws Exception {
        String request = "{\"name\":\"Updated Laptop\",\"price\":1800.00}";
        mockMvc.perform(put("/api/products/" + savedProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk());
    }*/

    /*@Test
    void testDeleteProduct_NoDiscount() throws Exception {
        mockMvc.perform(delete("/api/products/" + savedProduct.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void testCreateProduct_MissingFields() throws Exception {
        String request = "{\"name\":\"\",\"price\":0}";
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest());
    }




    @Test
    void testDeleteProduct_Bought() throws Exception {
        mockMvc.perform(delete("/api/products/300"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateProduct_MaxLengthName() throws Exception {
        String longName = "A".repeat(255);
        String request = objectMapper.writeValueAsString(new Product(null, longName, new BigDecimal("999.99"), new HashSet<>()));
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateProduct_PriceZero() throws Exception {
        String request = "{\"name\":\"Free Product\",\"price\":0.00}";
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest());
    }*/

}
