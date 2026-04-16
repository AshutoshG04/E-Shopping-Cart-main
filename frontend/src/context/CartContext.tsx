import React, { createContext, useState, useContext, useEffect, useCallback, useMemo } from 'react';
import type { ReactNode } from 'react';
import type { Product } from '../api/apiService';
import { checkoutOrder } from '../api/apiService';
import type { CheckoutItem } from '../api/apiService';
import { useToast } from './ToastContext';

// --- Types ---
interface CartItem extends Product {
  quantity: number;
}

interface CartContextType {
  cart: CartItem[];
  totalPriceCents: number;
  isCartOpen: boolean;
  isCheckingOut: boolean;
  lastOrderId?: string | null;
  toggleCart: () => void;
  addToCart: (product: Product) => void;
  updateQuantity: (productId: string, newQuantity: number) => void;
  removeItem: (productId: string) => void;
  checkout: () => Promise<void>;
  clearLastOrder: () => void;
}

const CartContext = createContext<CartContextType | undefined>(undefined);

// --- LocalStorage Helper (Persistence) ---
const getInitialCart = (): CartItem[] => {
  try {
    const storedCart = localStorage.getItem('shoppingCart');
    return storedCart ? JSON.parse(storedCart) : [];
  } catch (e) {
    console.error("Could not parse cart from localStorage:", e);
    return [];
  }
};

export const CartProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [cart, setCart] = useState<CartItem[]>(getInitialCart);
  const [isCartOpen, setIsCartOpen] = useState(false);
  const [isCheckingOut, setIsCheckingOut] = useState(false);
  const [lastOrderId, setLastOrderId] = useState<string | null>(null);

  const { showToast } = useToast();

  // Persist cart to localStorage whenever it changes
  useEffect(() => {
    localStorage.setItem('shoppingCart', JSON.stringify(cart));
  }, [cart]);

  // Use useMemo for efficient total price calculation
  const totalPriceCents = useMemo(
    () => cart.reduce((total, item) => total + item.priceCents * item.quantity, 0),
    [cart]
  );

  const toggleCart = useCallback(() => {
    setIsCartOpen(prev => !prev);
  }, []);

  const addToCart = useCallback((product: Product) => {
    let message = `${product.name} added to cart!`;

    setCart(prevCart => {
      const existingItem = prevCart.find(item => item.id === product.id);
      if (existingItem) {
        // Item exists: increase quantity
        message = `Increased quantity of ${product.name} to ${existingItem.quantity + 1}.`;
        return prevCart.map(item =>
          item.id === product.id ? { ...item, quantity: item.quantity + 1 } : item
        );
      } else {
        // New item: add to cart
        return [...prevCart, { ...product, quantity: 1 }];
      }
    });

    showToast('success', message);
  }, [showToast]);

  const updateQuantity = useCallback((productId: string, newQuantity: number) => {
    setCart(prevCart => {
      // If new quantity is 0 or less, remove the item
      if (newQuantity <= 0) {
        const itemToRemove = prevCart.find(item => item.id === productId);
        if (itemToRemove) showToast('success', `${itemToRemove.name} removed from cart.`);
        return prevCart.filter(item => item.id !== productId);
      }
      // Update the quantity of the specific item
      return prevCart.map(item =>
        item.id === productId ? { ...item, quantity: newQuantity } : item
      );
    });
  }, [showToast]);

  const removeItem = useCallback((productId: string) => {
    setCart(prevCart => {
      const itemToRemove = prevCart.find(item => item.id === productId);
      if (itemToRemove) showToast('success', `${itemToRemove.name} removed from cart.`);
      return prevCart.filter(item => item.id !== productId);
    });
  }, [showToast]);

  const checkout = async () => {
    if (cart.length === 0 || isCheckingOut) {
      console.warn("Cart is empty or checkout is already in progress.");
      return;
    }

    setIsCheckingOut(true);

    const checkoutItems: CheckoutItem[] = cart.map(item => ({
      productId: item.id,
      quantity: item.quantity,
    }));

    const payload = {
      items: checkoutItems,
      user: { name: "ASE Challenger", email: "candidate@verto.com" }
    };

    try {
      const response = await checkoutOrder(payload);
      // response: { success, orderId, totalCents }
      setLastOrderId(response.orderId || null);

      showToast('success', `Order Placed! Order ID: ${response.orderId || '—'}`);

      // Clear cart and localStorage on success
      setCart([]);
      localStorage.removeItem('shoppingCart');
      setIsCartOpen(false);
    } catch (error) {
      console.error("Checkout failed:", error);
      showToast('error', "Checkout failed. Check console and API.");
    } finally {
      setIsCheckingOut(false);
    }
  };

  const clearLastOrder = useCallback(() => setLastOrderId(null), []);

  return (
    <CartContext.Provider value={{
      cart,
      totalPriceCents,
      isCartOpen,
      isCheckingOut,
      lastOrderId,
      toggleCart,
      addToCart,
      updateQuantity,
      removeItem,
      checkout,
      clearLastOrder
    }}>
      {children}
    </CartContext.Provider>
  );
};

// eslint-disable-next-line react-refresh/only-export-components
export const useCart = () => {
  const context = useContext(CartContext);
  if (context === undefined) {
    throw new Error('useCart must be used within a CartProvider');
  }
  return context;
};
