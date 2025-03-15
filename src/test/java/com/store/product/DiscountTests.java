package com.store.product;

import com.store.BaseTest;
import com.store.product.entity.Discount;
import com.store.product.repository.DiscountRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DiscountTests extends BaseTest {

    @Autowired
    private DiscountRepository discountRepository;
    private String token;
    private Long savedDiscountId;

    @BeforeAll
    void loginAndGetToken() throws Exception {
        token = getToken("admin", "admin123");
    }

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
//        discountRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        if (savedDiscountId != null) {
            discountRepository.deleteById(savedDiscountId);
        }
    }

    @Test
    void testGetDiscount() throws Exception {
        Discount savedDiscount = discountRepository.save(new Discount(null, "Buy 1 Get 50% Off", 2, 50.00));
        savedDiscountId = savedDiscount.getId();
        mockMvc.perform(get("/api/v1/discounts/" + savedDiscountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk()).andExpect(jsonPath("$.size()", greaterThan(0))) // Ensure at least one discount exists
                .andExpect(jsonPath("$.id").exists()) // Check if ID exists
                .andExpect(jsonPath("$.type", is("Buy 1 Get 50% Off")))
                .andExpect(jsonPath("$.quantityRequired", is(2)))
                .andExpect(jsonPath("$.percentage", is(50.00)));
    }

    @Test
    void testCreateDiscount() throws Exception {
        String discountJson = "{\"type\": \"Buy 2 Get 1 Free\", \"percentage\": 33.33}";

        mockMvc.perform(post("/api/v1/discounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(discountJson)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type", is("Buy 2 Get 1 Free")))
                .andExpect(jsonPath("$.quantityRequired", is(0)))
                .andExpect(jsonPath("$.percentage", is(33.33)));
    }

    @Test
    void testUpdateDiscount() throws Exception {
        Discount savedDiscount = discountRepository.save(new Discount(null, "Buy 1 Get 50% Off", 2, 50.00));
        savedDiscountId = savedDiscount.getId();
        String updateDiscountJson = "{\"type\": \"Buy 2 Get 1 Free\", \"quantityRequired\":10, \"percentage\": 33.33}";
        mockMvc.perform(put("/api/v1/discounts/" + savedDiscountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateDiscountJson)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk()).andExpect(jsonPath("$.size()", greaterThan(0))) // Ensure at least one discount exists
                .andExpect(jsonPath("$.id").exists()) // Check if ID exists
                .andExpect(jsonPath("$.type", is("Buy 2 Get 1 Free")))
                .andExpect(jsonPath("$.quantityRequired", is(10)))
                .andExpect(jsonPath("$.percentage", is(33.33)));

        mockMvc.perform(get("/api/v1/discounts/" + savedDiscountId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", greaterThan(0))) // Ensure at least one discount exists
                .andExpect(jsonPath("$.type", is("Buy 2 Get 1 Free")))
                .andExpect(jsonPath("$.quantityRequired", is(10)))
                .andExpect(jsonPath("$.percentage", is(33.33)));
    }

    @Test
    void testDeleteDiscount() throws Exception {
        Discount savedDiscount = discountRepository.save(new Discount(null, "Buy 1 Get 50% Off", 2, 50.00));
        savedDiscountId = savedDiscount.getId();
        String deletedJson = "[" + savedDiscountId + "]";
        mockMvc.perform(delete("/api/v1/discounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deletedJson)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/discounts/" + savedDiscountId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    // TODO: add test cases with invalid data
}
