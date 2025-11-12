package com.LcEncode.apexRecon.paymentReconciliationService.domain.exception;

public class LedgerTransitionException extends RuntimeException {
    public LedgerTransitionException(String message) {
        super(message);
    }
}
