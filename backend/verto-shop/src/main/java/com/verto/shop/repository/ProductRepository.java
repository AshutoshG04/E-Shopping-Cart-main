// PATH: src/main/java/com/verto/shop/repository/ProductRepository.java
package com.verto.shop.repository;

import com.verto.shop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    // This file should be empty inside the interface.
    // Spring Data JPA creates all the methods for you.
}