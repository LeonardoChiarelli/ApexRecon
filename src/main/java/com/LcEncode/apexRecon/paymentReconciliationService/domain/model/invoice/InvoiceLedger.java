package com.LcEncode.apexRecon.paymentReconciliationService.domain.model.invoice;

import com.LcEncode.apexRecon.paymentReconciliationService.domain.exception.LedgerTransitionException;
import com.LcEncode.apexRecon.paymentReconciliationService.domain.exception.OverpaymentException;
import com.LcEncode.apexRecon.paymentReconciliationService.domain.exception.PaymentAmoutException;
import com.LcEncode.apexRecon.paymentReconciliationService.domain.model.valueObject.LedgerStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class InvoiceLedger {

    private final UUID id;
    private final UUID organizationId;
    private final LocalDate dueDate;
    private BigDecimal amountDue;
    private LedgerStatus status;

    public void applyPayment(BigDecimal amountToApply) {
        if (amountToApply == null || amountToApply.compareTo(BigDecimal.ZERO) <= 0) { throw new PaymentAmoutException("Payment amout cannot be lower than 0 or null"); }
        if (getStatus() == LedgerStatus.PAID) { throw new LedgerTransitionException("It´s not possible to apply payment to an already PAID invoice ledger"); }

        var newAmountDue = getAmountDue().subtract(amountToApply);
        if (newAmountDue.compareTo(BigDecimal.ZERO) < 0) {
            throw new OverpaymentException(String.format("""
                    Invoice Ledger: %s
                    Overpayment Attempt:
                        Due: %.2f
                        Applied: %.2f
                    """, getId(), getAmountDue(),amountToApply));
        }

        setAmountDue(newAmountDue);

        if (newAmountDue.compareTo(BigDecimal.ZERO) == 0) {
            setStatus(LedgerStatus.PAID);
        } else { setStatus(LedgerStatus.PARTIALLY_PAID); }
    }

    public UUID getId() {
        return id;
    }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public BigDecimal getAmountDue() {
        return amountDue;
    }

    public LedgerStatus getStatus() {
        return status;
    }

    private void setAmountDue(BigDecimal amountDue) {
        this.amountDue = amountDue;
    }

    private void setStatus(LedgerStatus status) {
        this.status = status;
    }
}
