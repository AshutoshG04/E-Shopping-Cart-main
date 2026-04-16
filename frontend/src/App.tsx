// frontend/src/App.tsx
import React, { useEffect, useState, useMemo } from 'react';
import currency from 'currency.js';
import type { Product } from './api/apiService';
import { getProducts } from './api/apiService';
import { useCart, CartProvider } from './context/CartContext';
import { ToastProvider, useToast } from './context/ToastContext';
import './App.css';
import { FaShoppingCart, FaTimes, FaCheckCircle, FaExclamationCircle, FaSpinner } from 'react-icons/fa';

// --- Utility Functions ---
const formatPrice = (cents: number, currencyCode: string = 'INR') => {
  return currency(cents / 100, {
    symbol: `${currencyCode} `,
    separator: ',',
    decimal: '.',
    precision: 2
  }).format();
};

// --- Toast Notification Component ---
const ToastNotification: React.FC = () => {
  const { toasts, removeToast } = useToast();

  return (
    <div className="toast-container">
      {toasts.map(toast => (
        <div
          key={toast.id}
          className={`toast ${toast.type} show`}
          onClick={() => removeToast(toast.id)}
          role="alert"
          aria-live="assertive"
        >
          <span className="toast-icon">
            {toast.type === 'success' ? <FaCheckCircle /> : <FaExclamationCircle />}
          </span>
          <span className="toast-message">{toast.message}</span>
        </div>
      ))}
    </div>
  );
};

// --- Skeleton Loader ---
const ProductCardSkeleton: React.FC = () => (
  <div className="skeleton-card">
    <div className="skeleton-img"></div>
    <div className="skeleton-body">
      <div className="skeleton-line title"></div>
      <div className="skeleton-line text-long"></div>
      <div className="skeleton-line text-short"></div>
    </div>
    <div className="skeleton-footer">
      <div className="skeleton-line skeleton-price"></div>
      <div className="skeleton-line skeleton-button"></div>
    </div>
  </div>
);

// --- Product Card ---
const ProductCard: React.FC<{ product: Product }> = React.memo(({ product }) => {
  const { addToCart, cart } = useCart();
  const [isAdding, setIsAdding] = useState(false);

  const priceDisplay = useMemo(() => formatPrice(product.priceCents, product.currency), [product]);

  // Find if in cart
  const itemInCart = useMemo(() => cart.find(item => item.id === product.id), [cart, product.id]);

  const handleAddToCart = () => {
    setIsAdding(true);
    setTimeout(() => {
      addToCart(product);
      setIsAdding(false);
    }, 300);
  };

  // Resolve image URL: if backend returned a relative path, let dev-server proxy handle it.
  const resolvedImageUrl = product.imageUrl && product.imageUrl.startsWith('/')
    ? product.imageUrl
    : product.imageUrl;

  return (
    <div className="product-card">
      {itemInCart && <span className="in-cart-badge">In Cart ({itemInCart.quantity})</span>}
      <img
        src={resolvedImageUrl}
        alt={product.name}
        onError={(e) => {
          // encodeURIComponent for safe text in placeholder
          e.currentTarget.src = `https://placehold.co/300x220/cccccc/333333?text=${encodeURIComponent(product.name)}`;
        }}
        loading="lazy"
      />
      <div className="card-body">
        <h3>{product.name}</h3>
        <p>{product.description}</p>
      </div>
      <div className="card-footer">
        <strong className="price-tag">{priceDisplay}</strong>
        <button
          className="add-to-cart-btn"
          onClick={handleAddToCart}
          disabled={isAdding}
        >
          {isAdding ? (
            <>
              <FaSpinner className="spin" style={{ marginRight: '8px' }} />
              Adding...
            </>
          ) : (
            'Add to Cart'
          )}
        </button>
      </div>
    </div>
  );
});

// --- Product List ---
const ProductList: React.FC = () => {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    getProducts()
      .then(data => {
        if (!data || data.length === 0) {
          setError("No products available or connection failed. Ensure Java backend is running.");
        } else {
          setProducts(data);
        }
      })
      .catch(err => {
        console.error("Error fetching products:", err);
        setError("Failed to load products. Check console and ensure Java API is on http://localhost:8080.");
      })
      .finally(() => setLoading(false));
  }, []);

  if (error) return <div className="error-state">🛑 Error: {error}</div>;

  return (
    <div className="product-grid">
      {loading ? (
        Array.from({ length: 6 }).map((_, index) => <ProductCardSkeleton key={index} />)
      ) : (
        products.map(p => <ProductCard key={p.id} product={p} />)
      )}
    </div>
  );
};

// --- Cart Overlay ---
const CartOverlay: React.FC = () => {
  const { cart, totalPriceCents, updateQuantity, removeItem, checkout, isCartOpen, toggleCart, isCheckingOut } = useCart();

  const totalDisplay = useMemo(() => formatPrice(totalPriceCents, 'INR'), [totalPriceCents]);

  return (
    <>
      <div className={`cart-overlay-backdrop ${isCartOpen ? 'open' : ''}`} onClick={toggleCart} />
      <div className={`cart-view ${isCartOpen ? 'open' : ''}`} role="dialog" aria-modal="true" aria-labelledby="cart-header">
        <button className="cart-close-btn" onClick={toggleCart} aria-label="Close Shopping Cart">
          <FaTimes />
        </button>
        <h2 className="cart-header" id="cart-header">Shopping Cart</h2>

        {cart.length === 0 ? (
          <div className="empty-cart">
            <p>Your cart is empty. Start adding products!</p>
          </div>
        ) : (
          <>
            <div className="cart-items">
              {cart.map(item => (
                <div key={item.id} className="cart-item">
                  <div className="item-details">
                    <div className="item-name-wrapper">
                      <img
                        src={item.imageUrl}
                        alt={item.name}
                        onError={(e) => {
                          e.currentTarget.src = `https://placehold.co/50x50/cccccc/333333?text=IMG`;
                        }}
                      />
                      <span className="item-name">{item.name}</span>
                    </div>
                    <strong className="item-price">{formatPrice(item.priceCents * item.quantity, item.currency)}</strong>
                  </div>

                  <div className="item-actions-row">
                    <div className="quantity-controls">
                      <button
                        className="qty-btn"
                        onClick={() => updateQuantity(item.id, item.quantity - 1)}
                        disabled={item.quantity <= 1}
                        aria-label={`Decrease quantity of ${item.name}`}
                      >
                        –
                      </button>
                      <span className="qty-count">{item.quantity}</span>
                      <button
                        className="qty-btn"
                        onClick={() => updateQuantity(item.id, item.quantity + 1)}
                        aria-label={`Increase quantity of ${item.name}`}
                      >
                        +
                      </button>
                    </div>
                    <button
                      className="remove-btn"
                      onClick={() => removeItem(item.id)}
                      aria-label={`Remove ${item.name} from cart`}
                    >
                      Remove
                    </button>
                  </div>
                </div>
              ))}
            </div>

            <div className="cart-summary">
              <h3 className="summary-total">
                <span>Order Total:</span>
                <span>{totalDisplay}</span>
              </h3>
              <button
                className="checkout-btn"
                onClick={checkout}
                disabled={isCheckingOut || cart.length === 0}
              >
                {isCheckingOut ? (
                  <>
                    <FaSpinner className="spin" style={{ marginRight: '8px' }} />
                    Processing...
                  </>
                ) : (
                  'Proceed to Checkout'
                )}
              </button>
              <p className="checkout-note">(Order will be logged to the Java backend console)</p>
            </div>
          </>
        )}
      </div>
    </>
  );
};

// --- Order Confirmation Modal ---
const OrderConfirmationModal: React.FC<{ orderId: string | null, onClose: () => void }> = ({ orderId, onClose }) => {
  if (!orderId) return null;
  return (
    <div className="cart-overlay-backdrop open" onClick={onClose} style={{ zIndex: 2000 }}>
      <div style={{
        position: 'fixed',
        left: '50%',
        top: '50%',
        transform: 'translate(-50%, -50%)',
        background: 'white',
        padding: '1.5rem',
        borderRadius: 12,
        boxShadow: '0 10px 30px rgba(0,0,0,0.15)',
        zIndex: 2001,
        width: 380,
        maxWidth: '90%'
      }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 12 }}>
          <h3 style={{ margin: 0 }}>Order Confirmed</h3>
          <button onClick={onClose} style={{ background: 'none', border: 'none', fontSize: 18, cursor: 'pointer' }}>
            <FaTimes />
          </button>
        </div>
        <p style={{ marginBottom: 10 }}>Thanks — your order has been placed successfully.</p>
        <p style={{ fontWeight: 700 }}>Order ID: <span style={{ color: '#2563eb' }}>{orderId}</span></p>
        <div style={{ marginTop: 16 }}>
          <button onClick={onClose} style={{
            width: '100%',
            padding: '10px 12px',
            background: '#10b981',
            color: 'white',
            border: 'none',
            borderRadius: 8,
            cursor: 'pointer',
            fontWeight: 700
          }}>
            Close
          </button>
        </div>
      </div>
    </div>
  );
};

// --- Main App Content ---
const AppContent: React.FC = () => {
  const { cart, toggleCart, lastOrderId, clearLastOrder } = useCart();
  const totalItems = useMemo(() => cart.reduce((t, i) => t + i.quantity, 0), [cart]);

  return (
    <>
      <ToastNotification />
      <div className="app-container">
        <header className="site-header">
          <div className="header-content">
            <h1>Verto E-SHOP</h1>
            <p></p>
          </div>
          <button className="cart-toggle-btn" onClick={toggleCart} aria-label="Open Shopping Cart">
            <FaShoppingCart />
            {totalItems > 0 && <span className="cart-badge">{totalItems}</span>}
          </button>
        </header>

        <div className="main-content">
          <div className="product-area">
            <h2>Product Catalog</h2>
            <ProductList />
          </div>
        </div>

        <CartOverlay />

        <OrderConfirmationModal orderId={lastOrderId ?? null} onClose={() => clearLastOrder()} />

        <footer>
          <p>@Ashutosh</p>
        </footer>
      </div>
    </>
  );
};

// --- Wrapped App with Providers ---
const WrappedApp: React.FC = () => (
  <ToastProvider>
    <CartProvider>
      <AppContent />
    </CartProvider>
  </ToastProvider>
);

export default WrappedApp;
