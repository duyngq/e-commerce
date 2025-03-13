package com.store.product;

import com.store.BaseTest;
import com.store.product.entity.Discount;
import com.store.product.repository.DiscountRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DiscountTests extends BaseTest {

    @Autowired
    private DiscountRepository discountRepository;

    private Discount savedDiscount;

    @BeforeEach
    public void setUp() {
        discountRepository.deleteAll();
        savedDiscount = discountRepository.save(new Discount(null, "Buy 1 Get 50% Off", 2, new BigDecimal("50.00")));
    }

    @AfterEach
    void tearDown() {
        discountRepository.deleteAll();
    }

    @Test
    void testCreateDiscount() throws Exception {
        String discountJson = "{\"name\": \"Buy 2 Get 1 Free\", \"discountPercentage\": 33.33}";

        mockMvc.perform(post("/api/discounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(discountJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Buy 2 Get 1 Free")))
                .andExpect(jsonPath("$.discountPercentage", is(33.33)));
    }

    @Test
    void testGetAllDiscounts() throws Exception {
        mockMvc.perform(get("/api/discounts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", greaterThanOrEqualTo(1)));
    }

    @Test
    void testDeleteDiscount() throws Exception {
        mockMvc.perform(delete("/api/discounts/" + savedDiscount.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/discounts/" + savedDiscount.getId()))
                .andExpect(status().isNotFound());
    }
}
