CREATE TABLE bank_transactions_ledger (
    bank_transaction_id BINARY(16) NOT NULL PRIMARY KEY,
    organization_id BINARY(16) NOT NULL,
    amount DECIMAL(19, 4) NOT NULL,
    amount_unmatched DECIMAL(19, 4) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'UNMATCHED',
    transaction_date TIMESTAMP NOT NULL,

    INDEX idx_org_status_unmatched (organization_id, status, amount_unmatched)
) ENGINE=InnoDB;
