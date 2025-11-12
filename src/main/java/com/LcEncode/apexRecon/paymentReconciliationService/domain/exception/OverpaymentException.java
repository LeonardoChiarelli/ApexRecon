package com.LcEncode.apexRecon.paymentReconciliationService.domain.exception;

public class OverpaymentException extends RuntimeException {
    public OverpaymentException(String message) {
        super(message);
    }
}
