package com.verto.shop.controller;

import com.verto.shop.model.Product;
import com.verto.shop.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService svc;

    public ProductController(ProductService svc) {
        this.svc = svc;
    }

    @GetMapping
    public List<Product> list() {
        return svc.getAll();
    }
}