CREATE TABLE transactions(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_id BIGINT NOT NULL,
    amount DECIMAL(19,4) NOT NULL,
    type VARCHAR(100) NOT NULL, -- INCOME | EXPENSE | TRANSFER
    category_id BIGINT NULL,
    description TEXT,
    currency CHAR(3) NOT NULL DEFAULT 'BRL',
    occurred_at TIMESTAMP NOT NULL,
    create_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP DEFAULT NULL,

    CONSTRAINT fk_transactions_account_id FOREIGN KEY(account_id) REFERENCES accounts(id),
    CONSTRAINT fk_transactions_category_id FOREIGN KEY(category_id) REFERENCES categories(id),

    INDEX idx_transactions_account_occurred (account_id, occurred_at),
    INDEX idx_transactions_occurred_at (occurred_at)
);