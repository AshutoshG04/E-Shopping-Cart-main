// PATH: src/main/java/com/verto/shop/controller/CheckoutController.java
package com.verto.shop.controller;

import com.verto.shop.model.CheckoutRequest;
import com.verto.shop.model.OrderResponse;
import com.verto.shop.model.Product;
import com.verto.shop.service.ProductService;
 
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.Instant;
import java.util.*;
import org.slf4j.Logger; 
import org.slf4j.LoggerFactory; 

@RestController
@RequestMapping("/api/checkout")
@Validated
public class CheckoutController {

    //   logger
    private static final Logger log = LoggerFactory.getLogger(CheckoutController.class);

    private final ProductService productService;

    public CheckoutController(ProductService productService) {
        this.productService = productService;
    }

    // Core Feature: POST /api/checkout
    // Accepts cart data, validates items, calculates total, and logs the order.
    @PostMapping
    public ResponseEntity<?> checkout(@Valid @RequestBody CheckoutRequest request) {

        // ... [Item merging and validation logic] ...
        Map<String, Integer> merged = new HashMap<>();
        for (CheckoutRequest.Item it : request.getItems()) {
            merged.merge(it.getProductId(), it.getQuantity(), Integer::sum);
        }

        List<String> invalid = new ArrayList<>();
        long total = 0;
        List<Map<String, Object>> orderItems = new ArrayList<>();

        for (Map.Entry<String, Integer> e : merged.entrySet()) {
            String pid = e.getKey();
            int qty = e.getValue();
            Optional<Product> pOpt = productService.findById(pid);

            if (pOpt.isEmpty()) {
                invalid.add(pid);
                continue;
            }

            Product p = pOpt.get();
            long subtotal = p.getPriceCents() * qty;
            total += subtotal;

            Map<String, Object> oi = new HashMap<>();
            oi.put("productId", pid);
            oi.put("name", p.getName());
            oi.put("unit_price_cents", p.getPriceCents());
            oi.put("quantity", qty);
            oi.put("subtotal_cents", subtotal);
            orderItems.add(oi);
        }

        if (!invalid.isEmpty()) {
            Map<String,Object> body = Map.of("error", "Invalid productId(s)", "invalidIds", invalid);
            log.warn("Checkout failed due to invalid product IDs: {}", invalid); // NEW: Log failure
            return ResponseEntity.unprocessableEntity().body(body);
        }

        //  standard UUID for professional ID generation
        String orderId = UUID.randomUUID().toString().toUpperCase().replace("-", "").substring(0, 12);
        
        Map<String,Object> order = new LinkedHashMap<>();
        order.put("orderId", orderId);
        order.put("items", orderItems);
        order.put("total_cents", total);
        order.put("user", request.getUser());
        order.put("created_at", Instant.now().toString());

        // Log Order using professional logger
        log.info("=== NEW ORDER LOGGED ===");
        log.info("Order Details: {}", order);

        // Returns a success response to the frontend
        return ResponseEntity.ok(new OrderResponse(true, orderId, total));
    }
}