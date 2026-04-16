// PATH: src/main/java/com/verto/shop/model/CheckoutRequest.java
package com.verto.shop.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

// DTO for the incoming JSON body on checkout POST request
public class CheckoutRequest {

    public static class Item {
        @NotNull
        private String productId;
        @Min(1)
        private int quantity;

        public Item() {}
        public Item(String productId, int quantity) { this.productId = productId; this.quantity = quantity; }
        public String getProductId() { return productId; }
        public void setProductId(String productId) { this.productId = productId; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }

    @NotEmpty // Ensures the cart items list is not empty
    private List<Item> items;

    private User user;

    public static class User {
        private String name;
        private String email;
        public User() {}
        public String getName() { return name; } public void setName(String name) { this.name = name; }
        public String getEmail() { return email; } public void setEmail(String email) { this.email = email; }
    }

    public CheckoutRequest() {}
    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}