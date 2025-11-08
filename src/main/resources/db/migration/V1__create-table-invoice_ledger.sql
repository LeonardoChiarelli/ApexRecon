CREATE TABLE invoices_ledger (
    invoice_id BINARY(16) NOT NULL PRIMARY KEY,
    organization_id BINARY(16) NOT NULL,
    customer_id BINARY(16) NOT NULL,
    total_amount DECIMAL(19, 4) NOT NULL,
    amount_due DECIMAL(19, 4) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'OPEN',
    due_date DATE,

    INDEX idx_org_customer (organization_id, customer_id),
    INDEX idx_status_amount_due (status, amount_due)
) ENGINE=InnoDB;
