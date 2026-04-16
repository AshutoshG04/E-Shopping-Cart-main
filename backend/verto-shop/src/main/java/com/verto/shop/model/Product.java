// PATH: src/main/java/com/verto/shop/model/Product.java
package com.verto.shop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
// === NEW: Import Serializable ===
import java.io.Serializable;

@Entity
@Table(name = "products")
// === NEW: Implement Serializable ===
public class Product implements Serializable {

    // === NEW: Add a serial version UID (good practice for serialization) ===
    private static final long serialVersionUID = 1L;

    @Id
    private String id;


    private String name;
    private String description;
    private long priceCents;
    private String imageUrl;
    private String currency;

    public Product() {}

    public Product(String id, String name, String description, long priceCents, String imageUrl, String currency) {
        this.id = id; this.name = name; this.description = description; this.priceCents = priceCents; this.imageUrl = imageUrl; this.currency = currency;
    }

    // ... (All getters and setters remain the same) ...
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public long getPriceCents() { return priceCents; }
    public void setPriceCents(long priceCents) { this.priceCents = priceCents; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}