// PATH: src/main/java/com/verto/shop/model/OrderResponse.java
package com.verto.shop.model;

// DTO for the successful response after a checkout
public class OrderResponse {
    private boolean success;
    private String orderId;
    private long totalCents;

    public OrderResponse() {}
    public OrderResponse(boolean success, String orderId, long totalCents) {
        this.success = success; this.orderId = orderId; this.totalCents = totalCents;
    }
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public long getTotalCents() { return totalCents; }
    public void setTotalCents(long totalCents) { this.totalCents = totalCents; }
}