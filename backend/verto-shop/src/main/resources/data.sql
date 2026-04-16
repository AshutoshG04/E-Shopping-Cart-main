-- This file will be run automatically by Spring Boot on startup
-- It seeds the database, replacing the old initCatalog() logic.

-- Delete all existing products to start fresh
DELETE FROM products;

-- Insert the 5 products
INSERT INTO products (id, name, description, price_cents, image_url, currency)
VALUES
    ('p_001', 'Wireless Mouse', 'Compact wireless mouse', 5000, '/images/mouse.jpg', 'INR'),
    ('p_002', 'Mechanical Keyboard', 'Tactile mechanical keyboard', 9000, '/images/keyboard.jpg', 'INR'),
    ('p_003', 'USB-C Hub', '5-in-1 hub', 8000, '/images/hub.jpg', 'INR'),
    ('p_004', 'Headphones', 'Noise cancelling headphones', 8999, '/images/headphones.jpg', 'INR'),
    ('p_005', 'Laptop Stand', 'Aluminum stand', 9500, '/images/stand.jpg', 'INR');