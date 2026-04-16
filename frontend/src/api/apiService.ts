// PATH: frontend/src/api/apiService.ts
// This file handles all communication with the Spring Boot Backend

import axios from 'axios';

// --- API Configuration ---
const API_BASE_URL = 'http://localhost:8080/api';

// --- Types based on Java Backend Models ---
export interface Product {
  id: string;
  name: string;
  description: string;
  priceCents: number;
  imageUrl: string;
  currency: string;
}

export interface CheckoutItem {
  productId: string;
  quantity: number;
}

export interface CheckoutPayload {
  items: CheckoutItem[];
  user: {
    name: string;
    email: string;
  };
}

// --- API Functions ---

/**
 * Fetches the hardcoded list of products from the Spring Boot API (GET /api/products).
 */
export const getProducts = async (): Promise<Product[]> => {
  try {
    // Note: The /api/products endpoint must be available and CORS configured on the Java backend
    const response = await axios.get<Product[]>(`${API_BASE_URL}/products`);
    return response.data;
  } catch (error) {
    console.error("Failed to fetch products from backend. Is the Java API running on :8080?", error);
    return []; 
  }
};

/**
 * Submits the cart for checkout (POST /api/checkout).
 * Note: The Java backend returns an OrderResponse (success, orderId, totalCents).
 */
export interface CheckoutResponse {
  success: boolean;
  orderId: string;
  totalCents: number;
}

export const checkoutOrder = async (payload: CheckoutPayload): Promise<CheckoutResponse> => {
  // Post request to POST /api/checkout endpoint
  const response = await axios.post<CheckoutResponse>(`${API_BASE_URL}/checkout`, payload);
  return response.data; 
};
