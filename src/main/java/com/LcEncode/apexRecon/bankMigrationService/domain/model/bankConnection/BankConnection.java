package com.LcEncode.apexRecon.bankMigrationService.domain.model.bankConnection;

import com.LcEncode.apexRecon.bankMigrationService.domain.exception.DomainException;
import com.LcEncode.apexRecon.bankMigrationService.domain.exception.ValidateException;
import com.LcEncode.apexRecon.bankMigrationService.domain.model.bankConnection.valueObject.Provider;

import java.time.Instant;
import java.util.UUID;

public class BankConnection {
    private final UUID id;
    private final UUID organizationId;
    private final Provider provider;
    private String accessTokenSecretArn;
    private Instant lastSync;
    private boolean isActive;

    private BankConnection(UUID id, UUID organizationId, Provider provider, String accessTokenSecretArn, Instant lastSync, boolean isActive) {
        if (id == null || organizationId == null || provider == null || accessTokenSecretArn.isBlank()) {
            throw new DomainException("All core Bank Connection must be provided.");
        }

        this.id = id;
        this.organizationId = organizationId;
        this.provider = provider;
        this.accessTokenSecretArn = accessTokenSecretArn;
        this.lastSync = lastSync;
        this.isActive = isActive;
    }

    public static BankConnectionBuilder builder() { return new BankConnectionBuilder(); }

    public void updateLastSync(Instant syncTimestamp) {
        if (syncTimestamp != null && syncTimestamp.isAfter(Instant.now())) {
           throw new DomainException("Sync Timestamp cannot be future.");
        }

        this.setLastSync(syncTimestamp);
    }

    public void revokeAccess() {
        this.setActive(false);
        // Dispara um Evento de Domínio: BankConnectionRevokedEvent(this.id)
    }

    public void reactivate(String newAccessTokenSecretArn) {
        this.setActive(true);
        setAccessTokenSecretArn(newAccessTokenSecretArn);
        this.setLastSync(null);
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

    private void setLastSync(Instant lastSync) {
        this.lastSync = lastSync;
    }

    private void setActive(boolean active) {
        isActive = active;
    }

    private void setAccessTokenSecretArn(String accessTokenSecretArn) {
        this.accessTokenSecretArn = accessTokenSecretArn;
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
                """, getId(),
                getOrganizationId(),
                getProvider(),
                getAccessTokenSecretArn(),
                getLastSync(),
                isActive());
    }

    public static class BankConnectionBuilder {
        private UUID organizationId;
        private Provider provider;
        private String accessTokenSecretArn;

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


        public BankConnection build() {
            if (organizationId == null) { throw new ValidateException("Bank Connection organization ID must be provided."); }
            if (provider == null) { throw new ValidateException("Bank Connection provider must be provided."); }
            if (accessTokenSecretArn.isBlank()) { throw new ValidateException("Bank Connection access Token Secret ARN must be provided."); }

            return new BankConnection(UUID.randomUUID(), this.organizationId, this.provider, this.accessTokenSecretArn, null, true);
        }
    }
}
