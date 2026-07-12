-- Seed admin user (password: Admin@123)
-- BCrypt hash generated for Admin@123
INSERT INTO users (email, password_hash, role, full_name, phone) VALUES
  ('admin@hospital.com', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/X4.G2oQzqKxqHhqKe', 'ADMIN', 'System Administrator', '+919000000001');
