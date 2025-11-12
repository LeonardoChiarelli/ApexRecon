package com.LcEncode.apexRecon.paymentReconciliationService.domain.model.payment;

import com.LcEncode.apexRecon.paymentReconciliationService.domain.model.valueObject.ReconciliationAllocation;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Payment {
    private final UUID paymentId;
    private final UUID organizationId;
    private final Instant paymentDate;
    private final BigDecimal totalAmount;
    private final List<ReconciliationAllocation> allocations = new ArrayList<>();



    public UUID getPaymentId() {
        return paymentId;
    }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public Instant getPaymentDate() {
        return paymentDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public List<ReconciliationAllocation> getAllocations() {
        return allocations;
    }
}
