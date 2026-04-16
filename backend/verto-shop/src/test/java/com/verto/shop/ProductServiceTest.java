// PATH: src/test/java/com/verto/shop/ProductServiceTest.java
package com.verto.shop;

import com.verto.shop.service.ProductService;
import com.verto.shop.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests focused purely on the business logic layer (ProductService).
 * This complements the existing integration test for the controller.
 */
@SpringBootTest
public class ProductServiceTest {

    // Inject the service layer bean for testing
    @Autowired
    private ProductService productService;

    /**
     * Unit Test 1: Verifies the service loads the hardcoded products correctly.
     */
    @Test
    void getAll_shouldReturnFiveProducts() {
        // Assert that the list is not null and contains the expected number of items.
        assertNotNull(productService.getAll());
        assertEquals(5, productService.getAll().size(), "Product service should initialize with exactly 5 hardcoded products.");
    }

    /**
     * Unit Test 2: Verifies the service can correctly find a product by ID (critical for checkout validation).
     */
    @Test
    void findById_shouldReturnCorrectProduct() {
        // Test for a known, existing product
        String existingId = "p_001";
        Product foundProduct = productService.findById(existingId).orElse(null);
        
        assertNotNull(foundProduct, "Product with ID p_001 should be found.");
        assertEquals(existingId, foundProduct.getId(), "The found product ID must match the requested ID.");
        
        // Test for a non-existing product
        assertTrue(productService.findById("p_999").isEmpty(), "Searching for a non-existent ID should return empty.");
    }
}
