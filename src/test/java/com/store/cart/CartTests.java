package com.store.cart;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.BaseTest;
import com.store.cart.model.request.CartItemRequest;
import com.store.cart.model.response.CartItemResponse;
import com.store.cart.model.response.CartResponse;
import com.store.cart.repository.CartRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CartTests extends BaseTest {
    private String token;
    @Autowired
    private CartRepository cartRepository;

    @BeforeAll
    void loginAndGetToken() throws Exception {
        token = getToken("customer", "cust123");
    }

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @AfterEach
    void tearDown() {
            cartRepository.deleteAll();
    }

    @Test
    void testAddProductsToCart() throws Exception {
        // Adjust productId to match your test DB (e.g., product #1)
        List<CartItemRequest> items = List.of(
                new CartItemRequest(1L, 15),
                new CartItemRequest(2L, 7)
        );
        String itemsJson = new ObjectMapper().writeValueAsString(items);

        MvcResult addedCartResult = mockMvc.perform(post("/api/v1/cart")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemsJson))
                .andExpect(status().isOk())
                .andReturn();
        // Get the JSON response as a String
        String addedCartResponseAsJson = addedCartResult.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        CartResponse addedCartResponse = mapper.readValue(addedCartResponseAsJson, CartResponse.class);

        assertEquals(2, addedCartResponse.getItems().size());
        assertEquals(BigDecimal.valueOf(0).setScale(2, BigDecimal.ROUND_HALF_UP), addedCartResponse.getTotalPrice());

        List<CartItemResponse> addedCardItemResponseList = addedCartResponse.getItems();
        // Assert the values of the first object
        CartItemResponse first = addedCardItemResponseList.get(0);
        assertEquals("Macbook", first.getProductName());
        assertEquals(15, first.getQuantity());

        // Assert the values of the second object
        CartItemResponse second = addedCardItemResponseList.get(1);
        assertEquals("Phone", second.getProductName());
        assertEquals(7, second.getQuantity());

        // Get the cart content and assert the items
        MvcResult checkoutCartResult = mockMvc.perform(get("/api/v1/cart/checkout/" + addedCartResponse.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        // Get the JSON response as a String
        String checkoutResponseAsJson = checkoutCartResult.getResponse().getContentAsString();

        CartResponse checkoutResponse = mapper.readValue(checkoutResponseAsJson, CartResponse.class);

        // Assert the Cart
        assertEquals(2, checkoutResponse.getItems().size());
        assertEquals(BigDecimal.valueOf(17440).setScale(2, BigDecimal.ROUND_HALF_UP), checkoutResponse.getTotalPrice());

        List<CartItemResponse> checkoutCartItemResponseList = checkoutResponse.getItems();
        // Assert the values of the first object
        CartItemResponse firstItem = checkoutCartItemResponseList.get(0);
        assertEquals("Macbook", firstItem.getProductName());
        assertEquals(15, firstItem.getQuantity());

        // Assert the values of the second object
        CartItemResponse secondItem = checkoutCartItemResponseList.get(1);
        assertEquals("Phone", secondItem.getProductName());
        assertEquals(7, secondItem.getQuantity());
    }

    @Test
    void testRemoveProductsFromCart() throws Exception {
        // 1) First add some items to the cart
        List<CartItemRequest> itemsToAdd = List.of(
                new CartItemRequest(1L, 15),
                new CartItemRequest(2L, 7)
        );
        String itemsAddJson = new ObjectMapper().writeValueAsString(itemsToAdd);

        MvcResult addedCartResult = mockMvc.perform(post("/api/v1/cart")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemsAddJson))
                .andExpect(status().isOk())
                .andReturn();

        // Get the JSON response as a String
        String addedCartResponseAsJson = addedCartResult.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        CartResponse addedCartResponse = mapper.readValue(addedCartResponseAsJson, CartResponse.class);

        assertEquals(2, addedCartResponse.getItems().size());
        assertEquals(BigDecimal.valueOf(0).setScale(2, BigDecimal.ROUND_HALF_UP), addedCartResponse.getTotalPrice());

        List<CartItemResponse> addedCardItemResponseList = addedCartResponse.getItems();
        // Assert the values of the first object
        CartItemResponse first = addedCardItemResponseList.get(0);
        assertEquals("Macbook", first.getProductName());
        assertEquals(15, first.getQuantity());

        // Assert the values of the second object
        CartItemResponse second = addedCardItemResponseList.get(1);
        assertEquals("Phone", second.getProductName());
        assertEquals(7, second.getQuantity());

        // Get the cart content and assert the items
        MvcResult checkoutCartResult = mockMvc.perform(get("/api/v1/cart/checkout/" + addedCartResponse.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Get the JSON response as a String
        String checkoutResponseAsJson = checkoutCartResult.getResponse().getContentAsString();

        CartResponse checkoutResponse = mapper.readValue(checkoutResponseAsJson, CartResponse.class);

        // Assert the Cart
        assertEquals(2, checkoutResponse.getItems().size());
        assertEquals(BigDecimal.valueOf(17440).setScale(2, BigDecimal.ROUND_HALF_UP), checkoutResponse.getTotalPrice());

        List<CartItemResponse> checkoutCartItemResponseList = checkoutResponse.getItems();
        // Assert the values of the first object
        CartItemResponse firstItem = checkoutCartItemResponseList.get(0);
        assertEquals("Macbook", firstItem.getProductName());
        assertEquals(15, firstItem.getQuantity());

        // Assert the values of the second object
        CartItemResponse secondItem = checkoutCartItemResponseList.get(1);
        assertEquals("Phone", secondItem.getProductName());
        assertEquals(7, secondItem.getQuantity());

        // 2) Now remove productId=1 from the cart
        List<CartItemRequest> itemsToRemove = List.of(
                new CartItemRequest(1L, 6) // Remove 5 from productId=1
        );
        String itemsRemoveJson = new ObjectMapper().writeValueAsString(itemsToRemove);

        MvcResult removedCartResult = mockMvc.perform(put("/api/v1/cart/" + addedCartResponse.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemsRemoveJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(greaterThan(0))))
                .andReturn();


        // Get the JSON response as a String
        String removedCartResponseAsJson = removedCartResult.getResponse().getContentAsString();
        CartResponse removedCartResponse = mapper.readValue(removedCartResponseAsJson, CartResponse.class);

        assertEquals(2, removedCartResponse.getItems().size());
        assertEquals(BigDecimal.valueOf(0).setScale(2, BigDecimal.ROUND_HALF_UP), removedCartResponse.getTotalPrice());

        List<CartItemResponse> removedaddedCardItemResponseList = removedCartResponse.getItems();
        // Assert the values of the first object
        CartItemResponse firstRemoval = removedaddedCardItemResponseList.get(0);
        assertEquals("Macbook", firstRemoval.getProductName());
        assertEquals(6, firstRemoval.getQuantity());

        // Assert the values of the second object
        CartItemResponse secondRemoval = removedaddedCardItemResponseList.get(1);
        assertEquals("Phone", secondRemoval.getProductName());
        assertEquals(7, secondRemoval.getQuantity());

        // 3) Finally, check the cart content
        // Get the cart content and assert the items
        MvcResult checkoutRemovalCartResult = mockMvc.perform(get("/api/v1/cart/checkout/" + addedCartResponse.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Get the JSON response as a String
        String checkoutRemovalCartResponseAsJson = checkoutRemovalCartResult.getResponse().getContentAsString();

        CartResponse checkoutRemovalResponse = mapper.readValue(checkoutRemovalCartResponseAsJson, CartResponse.class);

        // Assert the Cart
        assertEquals(2, checkoutRemovalResponse.getItems().size());
        assertEquals(BigDecimal.valueOf(12640).setScale(2, BigDecimal.ROUND_HALF_UP), checkoutRemovalResponse.getTotalPrice());

        List<CartItemResponse> checkoutRemovalCartItemResponseList = checkoutRemovalResponse.getItems();
        // Assert the values of the first object
        CartItemResponse firstRemovalItem = checkoutRemovalCartItemResponseList.get(0);
        assertEquals("Macbook", firstItem.getProductName());
        assertEquals(6, firstRemovalItem.getQuantity());

        // Assert the values of the second object
        CartItemResponse secondRemovalItem = checkoutRemovalCartItemResponseList.get(1);
        assertEquals("Phone", secondItem.getProductName());
        assertEquals(7, secondRemovalItem.getQuantity());
    }
}
