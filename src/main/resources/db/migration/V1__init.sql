CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    status VARCHAR(50),
    created_at TIMESTAMP,
    total_amount DECIMAL
);