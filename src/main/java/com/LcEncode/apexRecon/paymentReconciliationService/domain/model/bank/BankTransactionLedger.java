package com.LcEncode.apexRecon.paymentReconciliationService.domain.model.bank;

import com.LcEncode.apexRecon.paymentReconciliationService.domain.exception.AllocateException;
import com.LcEncode.apexRecon.paymentReconciliationService.domain.exception.LedgerTransitionException;
import com.LcEncode.apexRecon.paymentReconciliationService.domain.exception.OverpaymentException;
import com.LcEncode.apexRecon.paymentReconciliationService.domain.model.valueObject.LedgerStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class BankTransactionLedger {
    private final UUID bankTransactionId;
    private final UUID organizationId;
    private final BigDecimal amount;
    private final Instant transactionDate;
    private final String description;
    private BigDecimal amountUnmatched;
    private LedgerStatus status;

    public void applyAllocation(BigDecimal amountToAllocate) {
        if (amountToAllocate == null || amountToAllocate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new AllocateException("The amount to be allocated must be positive");
        }
        if (getStatus() == LedgerStatus.MATCHED) {
            throw new LedgerTransitionException("Was not possible to allocated value from transaction ledger that has already been reconciled (MATCHED)");
        }

        var newAmountUnmatched = getAmountUnmatched().subtract(amountToAllocate);
        if (newAmountUnmatched.compareTo(BigDecimal.ZERO) < 0) {
            throw new OverpaymentException(String.format("""
                    Transaction Ledger: %s
                    Attempted over-allocation:
                        Not reconciled: %.2f
                        Allocated: %.2f
                    """, getBankTransactionId(), getAmountUnmatched(), amountToAllocate));
        }

        setAmountUnmatched(newAmountUnmatched);

        if (newAmountUnmatched.compareTo(BigDecimal.ZERO) == 0) {
            setStatus(LedgerStatus.MATCHED);
        } else { setStatus(LedgerStatus.PARTIALLY_PAID); }
    }

    public UUID getBankTransactionId() {
        return bankTransactionId;
    }

    public UUID getOrganizationId() {
        return organizationId;
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

    public BigDecimal getAmountUnmatched() {
        return amountUnmatched;
    }

    public LedgerStatus getStatus() {
        return status;
    }

    private void setAmountUnmatched(BigDecimal amountUnmatched) {
        this.amountUnmatched = amountUnmatched;
    }

    private void setStatus(LedgerStatus status) {
        this.status = status;
    }
}
