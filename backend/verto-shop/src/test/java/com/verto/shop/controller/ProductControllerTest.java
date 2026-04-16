//path: src/test/java/com/verto/shop/controller/ProductControllerTest.java

package com.verto.shop.controller;

import com.verto.shop.model.Product;
import com.verto.shop.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Mock the ProductService so we don’t rely on the real hardcoded data
    @MockBean
    private ProductService productService;

    @Test
    void getProducts_shouldReturnListOfProducts() throws Exception {
        Product p1 = new Product("p_001", "Wireless Mouse", "Compact wireless mouse", 5000, "/images/mouse.jpg", "INR");
        Product p2 = new Product("p_002", "Mechanical Keyboard", "Tactile mechanical keyboard", 9000, "/images/keyboard.jpg", "INR");

        when(productService.getAll()).thenReturn(List.of(p1, p2));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                // Verify list size = 2
                .andExpect(jsonPath("$.length()").value(2))
                // Verify first product fields
                .andExpect(jsonPath("$[0].id").value("p_001"))
                .andExpect(jsonPath("$[0].name").value("Wireless Mouse"))
                .andExpect(jsonPath("$[0].priceCents").value(5000))
                // Verify second product fields
                .andExpect(jsonPath("$[1].id").value("p_002"))
                .andExpect(jsonPath("$[1].currency").value("INR"));
    }
}
