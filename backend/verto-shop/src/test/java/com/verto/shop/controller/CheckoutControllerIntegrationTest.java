// PATH: src/test/java/com/verto/shop/controller/CheckoutControllerIntegrationTest.java
package com.verto.shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.verto.shop.model.Product;
import com.verto.shop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration Test for CheckoutController using MockMvc.
 * This ensures the API contract (validation, response structure, total calculation) is correct.
 */
@WebMvcTest(CheckoutController.class)
public class CheckoutControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Mock the ProductService as we only want to test the Controller logic
    @MockBean
    private ProductService productService;

    private final Product product1 = new Product("p_001", "Mouse", "desc", 5000, "url", "INR");
    private final Product product2 = new Product("p_002", "Keyboard", "desc", 9000, "url", "INR");

    @BeforeEach
    void setUp() {
        //   behavior of the mocked ProductService
        when(productService.findById("p_001")).thenReturn(Optional.of(product1));
        when(productService.findById("p_002")).thenReturn(Optional.of(product2));
        when(productService.findById("p_999")).thenReturn(Optional.empty()); // Invalid product
    }

    @Test
    void checkout_validCart_shouldReturnOkAndCorrectTotal() throws Exception {
        //  A valid cart request
        Map<String, Object> validRequest = Map.of(
            "items", List.of(
                Map.of("productId", "p_001", "quantity", 2),
                Map.of("productId", "p_002", "quantity", 1)
            ),
            "user", Map.of("name", "Test User", "email", "test@example.com")
        );

        // Expected Total: (2 * 5000) + (1 * 9000) = 19000 cents
        long expectedTotalCents = 19000L;

        // Act & Assert
        mockMvc.perform(post("/api/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.totalCents").value(expectedTotalCents))
            .andExpect(jsonPath("$.orderId").isString());
    }

    @Test
    void checkout_cartWithDuplicates_shouldMergeAndReturnCorrectTotal() throws Exception {
        // Arrange: Cart with duplicate item p_001
        Map<String, Object> duplicateRequest = Map.of(
            "items", List.of(
                Map.of("productId", "p_001", "quantity", 1),
                Map.of("productId", "p_001", "quantity", 3) // Duplicate item
            ),
            "user", Map.of("name", "Test User", "email", "test@example.com")
        );

        // Expected Total: (1 + 3) * 5000 = 20000 cents
        long expectedTotalCents = 20000L;

        // Act & Assert
        mockMvc.perform(post("/api/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicateRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalCents").value(expectedTotalCents));
    }

    @Test
    void checkout_invalidProduct_shouldReturnUnprocessableEntity() throws Exception {
        // Arrange: Cart containing one valid and one invalid product
        Map<String, Object> invalidRequest = Map.of(
            "items", List.of(
                Map.of("productId", "p_001", "quantity", 1),
                Map.of("productId", "p_999", "quantity", 5) // Invalid ID
            ),
            "user", Map.of("name", "Test User", "email", "test@example.com")
        );

        // Act & Assert
        mockMvc.perform(post("/api/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
            .andExpect(status().isUnprocessableEntity()) // HTTP 422
            .andExpect(jsonPath("$.error").value("Invalid productId(s)"))
            .andExpect(jsonPath("$.invalidIds[0]").value("p_999"));
    }

    @Test
    void checkout_emptyCart_shouldReturnBadRequest() throws Exception {
        // Arrange: Cart with empty items list (violates @NotEmpty)
        Map<String, Object> emptyRequest = Map.of(
            "items", List.of(),
            "user", Map.of("name", "Test User", "email", "test@example.com")
        );

        // Act & Assert
        mockMvc.perform(post("/api/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emptyRequest)))
            .andExpect(status().isBadRequest()); // HTTP 400 (due to validation failure)
    }
}