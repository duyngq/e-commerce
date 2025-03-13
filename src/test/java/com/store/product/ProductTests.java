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
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.HashSet;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)  // Allows non-static @BeforeAll
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
        productRepository.deleteAll();
        savedProduct = productRepository.save(new Product(null, "Laptop", new BigDecimal("1000.00"), new HashSet<>()));
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }

    @Test
    void testAddProduct() throws Exception {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("Laptop");
        productRequest.setPrice(BigDecimal.valueOf(1000));

        String json = objectMapper.writeValueAsString(productRequest);
        mockMvc.perform(post("/api/v1/products")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/products?page=0&size=10")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2))) // Expecting only 2 products per page
                .andExpect(jsonPath("$.totalElements").value(2)) // Total products in DB
                .andExpect(jsonPath("$.totalPages").value(1)); // Expecting 2 pages
    }

    @Test
    void testCreateProduct() throws Exception {
        String productJson = "{\"name\": \"Phone\", \"price\": 500.00}";

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Phone")))
                .andExpect(jsonPath("$.price", is(500.00)));
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

    /*@Test
    void getProductById_Valid() throws Exception {
        mockMvc.perform(get("/api/products/" + savedProduct.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    @Test
    void updateProduct_Valid() throws Exception {
        String request = "{\"name\":\"Updated Laptop\",\"price\":1800.00}";
        mockMvc.perform(put("/api/products/" + savedProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk());
    }*/

    @Test
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
    void testGetProductById_NonExisting() throws Exception {
        mockMvc.perform(get("/api/products/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateProduct_NonExisting() throws Exception {
        String request = "{\"name\":\"Updated\",\"price\":2000.00}";
        mockMvc.perform(put("/api/products/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isNotFound());
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
    }

}
