package com.LcEncode.apexRecon.paymentReconciliationService.domain.model.valueObject;

import java.math.BigDecimal;
import java.util.UUID;

public record ReconciliationAllocation(
        UUID allocationId,
        UUID paymentId,
        UUID invoiceId,
        UUID bankTransactionId,
        BigDecimal amount
) {
    public ReconciliationAllocation(UUID paymentId,
                                    UUID invoiceId,
                                    UUID bankTransactionId,
                                    BigDecimal amount) {
        this(UUID.randomUUID(), paymentId, invoiceId, bankTransactionId, amount);
    }
}
