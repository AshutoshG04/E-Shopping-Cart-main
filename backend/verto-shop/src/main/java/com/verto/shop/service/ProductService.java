package com.verto.shop.service;

import com.verto.shop.model.Product;
import com.verto.shop.repository.ProductRepository;
// === NEW: Import Caching annotations ===
import org.springframework.cache.annotation.Cacheable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Value("${app.base-url:}")
    private String baseUrl;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    private String buildImageUrl(String path) {
        // ... (this logic remains the same) ...
        if (baseUrl == null || baseUrl.isBlank()) return path;
        String trimmed = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        return trimmed + path;
    }

    // === NEW: Add Caching ===
    // This result will be cached in a Redis key named "products::all"
    @Cacheable(value = "products", key = "'all'")
    public List<Product> getAll() {
        List<Product> products = productRepository.findAll();
        products.forEach(p -> p.setImageUrl(buildImageUrl(p.getImageUrl())));
        return products;
    }

    // === NEW: Add Caching ===
    // This result will be cached in a key like "products::p_001"
    @Cacheable(value = "products", key = "#id")
    public Optional<Product> findById(String id) {
        Optional<Product> productOpt = productRepository.findById(id);
        productOpt.ifPresent(p -> p.setImageUrl(buildImageUrl(p.getImageUrl())));
        return productOpt;
    }
}