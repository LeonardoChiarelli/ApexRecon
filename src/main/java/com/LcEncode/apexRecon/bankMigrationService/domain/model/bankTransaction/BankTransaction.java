package com.LcEncode.apexRecon.bankMigrationService.domain.model.bankTransaction;

import com.LcEncode.apexRecon.bankMigrationService.domain.exception.DomainException;
import com.LcEncode.apexRecon.bankMigrationService.domain.model.bankConnection.valueObject.Provider;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class BankTransaction {
    private final UUID id;
    private final UUID connectionId;
    private final Provider provider;
    private final BigDecimal amount;
    private final Instant transactionDate;
    private final String description;
    private final Instant ingestedAt;
    private Instant processedAt; // Quando foi enviado para o RabbitMQ

    private BankTransaction(UUID id, UUID connectionId, Provider provider, BigDecimal amount, Instant transactionDate, String description, Instant ingestedAt, Instant processedAt) {
        if (id == null || connectionId == null || provider == null || amount.compareTo(BigDecimal.ZERO) <= 0 || transactionDate == null || description.isBlank() || ingestedAt == null || processedAt == null) {
            throw new DomainException("All core bank transaction must be provided.");
        }

        this.id = id;
        this.connectionId = connectionId;
        this.provider = provider;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.description = description;
        this.ingestedAt = ingestedAt;
        this.processedAt = processedAt;
    }

    public static BankTransactionBuilder builder() { return new BankTransactionBuilder(); }

    public void markAsProcessed() {
        if (this.processedAt!= null) {
            throw new DomainException("Transaction was already processed.");
        }
        setProcessedAt(Instant.now());
    }

    public boolean isAlreadyProcessed() {
        return this.processedAt!= null;
    }

    public UUID getId() {
        return id;
    }

    public UUID getConnectionId() {
        return connectionId;
    }

    public Provider getProvider() {
        return provider;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Instant getTransactionDate() {
        return transactionDate;
    }

    public String getDescription() {
        return description;
    }

    public Instant getIngestedAt() {
        return ingestedAt;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(Instant processedAt) {
        this.processedAt = processedAt;
    }

    @Override
    public String toString() {
        return String.format("""
                ID: %s
                Connection ID: %s
                Provider: %s
                Amount: %.2f
                Transaction Date: %s
                Description: %s
                Ingested At: %s
                Processed At: %s
                """, getId(), getConnectionId(),
                getProvider(),
                getAmount(),
                getTransactionDate(),
                getDescription(),
                getIngestedAt(),
                getProcessedAt());
    }

    public static class BankTransactionBuilder {

    }
}
