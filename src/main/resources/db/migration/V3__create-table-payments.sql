CREATE TABLE reconciliation_allocations (
    allocation_id BINARY(16) NOT NULL PRIMARY KEY,
    payment_id BINARY(16) NOT NULL,
    invoice_id BINARY(16) NOT NULL,
    bank_transaction_id BINARY(16) NOT NULL,
    allocated_amount DECIMAL(19, 4) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_reconciliation_allocations_payment_id FOREIGN KEY (payment_id) REFERENCES payments(payment_id),
    CONSTRAINT fk_reconciliation_allocations_invoice_id FOREIGN KEY (invoice_id) REFERENCES invoices_ledger(invoice_id),
    CONSTRAINT fk_reconciliation_allocations_bank_transaction_id FOREIGN KEY (bank_transaction_id) REFERENCES bank_transactions_ledger(bank_transaction_id),

    INDEX idx_invoice (invoice_id),
    INDEX idx_bank_tx (bank_transaction_id)
) ENGINE=InnoDB;
