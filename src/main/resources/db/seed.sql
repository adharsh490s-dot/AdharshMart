-- Passwords are bcrypt hash for: 'Password123!'
MERGE INTO users (name, email, password_hash, role) KEY(email) VALUES
('Admin User', 'admin@adharshmart.com', '$2a$12$FqECP.8cpB3V/1L7/023S.bSAKX9d5S0QJOK0jjKlJ8GeTobuRj7W', 'ADMIN');
MERGE INTO users (name, email, password_hash, role) KEY(email) VALUES
('Alice Seller', 'alice@adharshmart.com', '$2a$12$e88yvVb1aH03zWffYyE43eH2fJ9z2pQ8XGceI4F8JkH6x9UeZz6Ue', 'SELLER');
MERGE INTO users (name, email, password_hash, role) KEY(email) VALUES
('Bob Buyer', 'bob@adharshmart.com', '$2a$12$e88yvVb1aH03zWffYyE43eH2fJ9z2pQ8XGceI4F8JkH6x9UeZz6Ue', 'BUYER');

INSERT INTO products (seller_id, name, description, price, stock_qty, category, image_url)
SELECT 2, 'Cyber Glass Headset', 'Spatial audio with reactive neon accents.', 149.99, 25, 'Electronics', 'https://images.unsplash.com/photo-1546435770-a3e426bf472b?w=500'
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'Cyber Glass Headset');
INSERT INTO products (seller_id, name, description, price, stock_qty, category, image_url)
SELECT 2, 'Holographic Mechanical Keyboard', 'Low-profile wireless crystal keyboard.', 199.50, 15, 'Accessories', 'https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=500'
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'Holographic Mechanical Keyboard');
INSERT INTO products (seller_id, name, description, price, stock_qty, category, image_url)
SELECT 2, 'Neural Smartwatch 3D', 'Health tracking with ambient glass display.', 299.00, 40, 'Wearables', 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500'
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'Neural Smartwatch 3D');