package com.LcEncode.apexRecon.paymentReconciliationService.domain.model.valueObject;

public enum LedgerStatus {
    OPEN,
    PARTIALLY_PAID,
    PAID,

    UNMATCHED,
    PARTIALLY_MATCHED,
    MATCHED
}
