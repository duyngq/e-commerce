package com.store.security;

import com.store.BaseTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class AAATests extends BaseTest {

    @Test
    void testLoginAsAdmin() throws Exception {
        String adminToken = login("admin", "admin123");
        assertTrue(adminToken.contains("token"));
    }

    @Test
    void testAddNewAdmin() throws Exception {
        String addedUser = addUser("test", "test123", "admin");
        assertTrue(addedUser.equals("User registered successfully"));
        String adminToken = login("test", "test123");
        assertTrue(adminToken.contains("token"));
    }

    @Test
    void testAddNewCustomer() throws Exception {
        String addedUser = addUser("custnew", "custnew123", "customer");
        assertTrue(addedUser.equals("User registered successfully"));
        String adminToken = login("custnew", "custnew123");
        assertTrue(adminToken.contains("token"));
    }

    @Test
    void testLoginAsCustomer() throws Exception {
        String custToken = login("customer", "cust123");
        assertTrue(custToken.contains("token"));
    }
}
