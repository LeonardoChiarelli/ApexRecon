package com.LcEncode.apexRecon.invoicingService.domain.model;

import com.LcEncode.apexRecon.invoicingService.domain.exception.*;
import com.LcEncode.apexRecon.invoicingService.domain.model.useCase.InvoiceItem;
import com.LcEncode.apexRecon.invoicingService.domain.model.useCase.InvoiceStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Invoice {
    private final UUID invoice_id;
    private final UUID organization_id;
    private final UUID customer_id;
    private BigDecimal totalAmount;
    private BigDecimal amountDue;
    private InvoiceStatus status;
    private final LocalDate dueDate;
    private final LocalDate issueDate;
    private LocalDate paymentDate;
    private final List<InvoiceItem> items = new ArrayList<>();
    private String voidReason;

    private Invoice(UUID invoice_id, UUID organization_id, UUID customer_id, BigDecimal totalAmount, BigDecimal amountDue, InvoiceStatus status, LocalDate dueDate, LocalDate issueDate,List<InvoiceItem> items) {
        if (invoice_id == null || organization_id == null || customer_id == null || totalAmount == null || amountDue == null || status == null || dueDate == null || issueDate == null || items.isEmpty()) {
            throw new ValidateException("All core Invoice must be provided.");
        }

        this.invoice_id = invoice_id;
        this.organization_id = organization_id;
        this.customer_id = customer_id;
        this.totalAmount = totalAmount;
        this.amountDue = amountDue;
        this.status = status;
        this.dueDate = dueDate;
        this.issueDate = issueDate;
        this.items.addAll(items);
    }

    public static InvoiceBuilder builder() {
        return new InvoiceBuilder();
    }


    public void markAsSent() {
        if (getStatus() != InvoiceStatus.DRAFT) {
            throw new InvoiceTransitionException(String.format("""
                    Only invoices in DRAFT status can be sent.
                    Current status: %s
                    """, getStatus()));
        }

        setStatus(InvoiceStatus.SENT);
    }

    public void markAsPaid() {
        if (getStatus() == InvoiceStatus.PAID || getStatus() == InvoiceStatus.VOID) {
            throw new InvoiceAlreadyPaidException(String.format("""
                    Invoice ID: %s
                    Status: %s
                    """, getInvoice_id(), getStatus()));
        }

        setStatus(InvoiceStatus.PAID);
        setAmountDue(BigDecimal.ZERO);
        setPaymentDate(LocalDate.now());
    }

    public void applyPartialPayment(BigDecimal amountPaid) {
        if (getStatus() == InvoiceStatus.PAID || getStatus() == InvoiceStatus.VOID) {
            throw new InvoiceAlreadyPaidException(String.format("""
                    Invoice ID: %s
                    Status: %s
                    """, getInvoice_id(), getStatus()));
        }

        BigDecimal newAmountDue = this.amountDue.subtract(amountPaid);
        if (newAmountDue.compareTo(BigDecimal.ZERO) < 0) {
            throw new OverpaymentException(String.format("""
                    Invoice ID: %s
                    Amount Due: %.2f
                    Amount Paid: %.2f
                    """, getInvoice_id(), getAmountDue(), amountPaid));
        }

        setAmountDue(newAmountDue);
        setStatus(InvoiceStatus.PARTIALLY_PAID);
        setPaymentDate(LocalDate.now());
    }

    public void markAsOverdue() {
        if (LocalDate.now().isAfter(getDueDate()) && getStatus() == InvoiceStatus.SENT || getStatus() == InvoiceStatus.PARTIALLY_PAID) {
            setStatus(InvoiceStatus.OVERDUE);
        }
    }

    public void voidInvoice(String reason) {
        if (getStatus() == InvoiceStatus.PAID) { throw new InvoiceTransitionException("Paid invoices cannot be void"); }
        if (reason == null || reason.isBlank()) { throw new DomainException("A reason to void the invoice must be provided."); }

        setStatus(InvoiceStatus.VOID);
        setAmountDue(BigDecimal.ZERO);
        setVoidReason(reason);
    }

    public void addItem(InvoiceItem item) {
        if (getStatus() != InvoiceStatus.DRAFT) { throw new InvoiceTransitionException("Items only can be add in DRAFT status"); }

        this.items.add(item);
        recalculateTotals();
    }

    public void removeItem(UUID itemId) {
        if (getStatus() != InvoiceStatus.DRAFT) { throw new InvoiceTransitionException("Items only can be remove in DRAFT status"); }

        this.items.removeIf(item -> item.getItemId().equals(itemId));
        recalculateTotals();
    }

    private void recalculateTotals() {
        setTotalAmount(this.items.stream()
                .map(InvoiceItem::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        if (getStatus() == InvoiceStatus.DRAFT) { setAmountDue(getTotalAmount()); }
    }

    public UUID getInvoice_id() {
        return invoice_id;
    }

    public UUID getOrganization_id() {
        return organization_id;
    }

    public UUID getCustomer_id() {
        return customer_id;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public BigDecimal getAmountDue() {
        return amountDue;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public List<InvoiceItem> getItems() {
        return items;
    }

    public String getVoidReason() {
        return voidReason;
    }

    private void setAmountDue(BigDecimal amountDue) {
        this.amountDue = amountDue;
    }

    private void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    private void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    private void setVoidReason(String voidReason) {
        this.voidReason = voidReason;
    }

    private void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return String.format("""
                Invoice ID: %s
                Organization ID: %s
                Customer ID: %s
                Total Amount: %.2f
                Amount Due: %.2f
                Status: %s
                Due Date: %s
                """, getInvoice_id(), getOrganization_id(), getCustomer_id(), getTotalAmount(), getAmountDue(), getStatus(), getDueDate());
    }

    public static class InvoiceBuilder {
        private BigDecimal totalAmount;
        private BigDecimal amountDue;
        private LocalDate dueDate;
        private List<InvoiceItem> items = new ArrayList<>();

        public InvoiceBuilder() {}

        public InvoiceBuilder amountDue(BigDecimal amountDue) {
            this.amountDue = amountDue;
            return this;
        }

        public InvoiceBuilder localDate(LocalDate dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public InvoiceBuilder items(List<InvoiceItem> items) {
            this.items.addAll(items);
            this.totalAmount = this.items.stream()
                    .map(InvoiceItem::getTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            return this;
        }

        public Invoice build() {
            if (amountDue == null) { throw new ValidateException("Invoice amount due must be provided."); }
            if (dueDate == null) { throw new ValidateException("Invoice due date must be provided."); }
            if (items.isEmpty()) { throw new ValidateException("An invoice must be have at least one item."); }

            return new Invoice(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), this.totalAmount, this.amountDue, InvoiceStatus.DRAFT, this.dueDate, LocalDate.now(), this.items);
        }
    }
}
