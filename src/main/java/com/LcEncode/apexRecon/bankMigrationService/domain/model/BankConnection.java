package com.LcEncode.apexRecon.bankMigrationService.domain.model;

import com.LcEncode.apexRecon.bankMigrationService.domain.exception.DomainException;
import com.LcEncode.apexRecon.bankMigrationService.domain.exception.ValidateException;

import java.time.Instant;
import java.util.UUID;

public class BankConnection {
    private final UUID id;
    private final UUID organizationId;
    private final Provider provider;
    private final String accessTokenSecretArn;
    private final Instant lastSync;
    private final boolean isActive;
    private final String accountName;
    private final String accountMask;

    private BankConnection(UUID id, UUID organizationId, Provider provider, String accessTokenSecretArn, Instant lastSync, boolean isActive, String accountName, String accountMask) {
        if (id == null || organizationId == null || provider == null || accessTokenSecretArn.isBlank() || accountName.isBlank() || accountMask.isBlank()) {
            throw new DomainException("All core Bank Connection must be provided.");
        }

        this.id = id;
        this.organizationId = organizationId;
        this.provider = provider;
        this.accessTokenSecretArn = accessTokenSecretArn;
        this.lastSync = lastSync;
        this.isActive = isActive;
        this.accountName = accountName;
        this.accountMask = accountMask;
    }

    public static BankConnectionBuilder builder() { return new BankConnectionBuilder(); }

    public void updateLastSync(Instant syncTimestamp) {
        if (syncTimestamp != null && syncTimestamp.isAfter(Instant.now())) {
           throw new Ba
        }
    }

    public UUID getId() {
        return id;
    }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public Provider getProvider() {
        return provider;
    }

    public String getAccessTokenSecretArn() {
        return accessTokenSecretArn;
    }

    public Instant getLastSync() {
        return lastSync;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getAccountMask() {
        return accountMask;
    }

    @Override
    public String toString() {
        return String.format("""
                ID: %s
                Organization ID: %s
                Provider: %s
                Access Token Secret Arn: %s
                Last Sync: %s
                Is Active: %s
                Account Name: %s
                Account Mask: %s
                """, getId(),
                getOrganizationId(),
                getProvider(),
                getAccessTokenSecretArn(),
                getLastSync(),
                isActive(),
                getAccountName(),
                getAccountMask());
    }

    public static class BankConnectionBuilder {
        private UUID organizationId;
        private Provider provider;
        private String accessTokenSecretArn;
        private String accountName;
        private String accountMask;

        public BankConnectionBuilder() {}

        public BankConnectionBuilder organizationId(UUID organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public BankConnectionBuilder provider(Provider provider) {
            this.provider = provider;
            return this;
        }

        public BankConnectionBuilder accessTokenSecretArn(String accessTokenSecretArn) {
            this.accessTokenSecretArn = accessTokenSecretArn;
            return this;
        }

        public BankConnectionBuilder accountName(String accountName) {
            this.accountName = accountName;
            return this;
        }

        public BankConnectionBuilder accountMask(String accountMask) {
            this.accountMask = accountMask;
            return this;
        }

        public BankConnection build() {
            if (organizationId == null) { throw new ValidateException("Bank Connection organization ID must be provided."); }
            if (provider == null) { throw new ValidateException("Bank Connection provider must be provided."); }
            if (accessTokenSecretArn.isBlank()) { throw new ValidateException("Bank Connection access Token Secret ARN must be provided."); }
            if (accountName.isBlank()) { throw new ValidateException("Bank Connection account name must be provided."); }
            if (accountMask.isBlank()) { throw new ValidateException("Bank Connection account mask must be provided."); }

            return new BankConnection(UUID.randomUUID(), this.organizationId, this.provider, this.accessTokenSecretArn, null, true, this.accountName, this.accountMask);
        }
    }
}
