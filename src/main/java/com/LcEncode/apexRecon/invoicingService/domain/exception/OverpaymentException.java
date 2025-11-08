package com.LcEncode.apexRecon.invoicingService.domain.exception;

public class OverpaymentException extends RuntimeException {
    public OverpaymentException(String message) {
        super(message);
    }
}
