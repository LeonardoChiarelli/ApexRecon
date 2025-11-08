package com.LcEncode.apexRecon.invoicingService.domain.model.useCase;

import com.LcEncode.apexRecon.invoicingService.domain.exception.DomainException;
import com.LcEncode.apexRecon.invoicingService.domain.exception.ValidateException;

import java.math.BigDecimal;
import java.util.UUID;

public class InvoiceItem {
    private final UUID itemId;
    private final String description;
    private final Integer quantity;
    private final BigDecimal unitPrice;

    private InvoiceItem(UUID itemId, String description, Integer quantity, BigDecimal unitPrice) {
        if (quantity == null || quantity <= 0) {
            throw new DomainException("Item quantity must be greater than 0");
        }
        if (unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainException("Item unit price must be greater than 0");
        }
        if (itemId == null || description.isBlank()) {
            throw new DomainException("All core invoice item must be provided");
        }

        this.itemId = itemId;
        this.description = description;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public static InvoiceItemBuilder builder() {
        return new InvoiceItemBuilder();
    }

    public UUID getItemId() {
        return itemId;
    }

    public String getDescription() {
        return description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getTotal() {
        return this.unitPrice.multiply(new BigDecimal(this.quantity));
    }

    @Override
    public String toString() {
        return String.format("""
                Invoice Item ID: %s
                Description: %s
                Quantity: %d
                Unit Price: %.2f
                """, getItemId(), getDescription(), getQuantity(), getUnitPrice());
    }

    public static class InvoiceItemBuilder {
        private String description;
        private Integer quantity;
        private BigDecimal unitPrice;

        public InvoiceItemBuilder() {}

        public InvoiceItemBuilder description(String description) {
            this.description = description;
            return this;
        }

        public InvoiceItemBuilder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public InvoiceItemBuilder unitPrice(BigDecimal unitPrice) {
            this.unitPrice = unitPrice;
            return this;
        }

        public InvoiceItem build() {
            if (description.isBlank()) { throw new ValidateException("Invoice item description must be provided. "); }
            if (quantity == null || quantity <= 0) { throw new ValidateException("Invoice Item quantity must be provided or must be greater than 0"); }
            if (unitPrice.compareTo(BigDecimal.ZERO) <= 0) { throw new ValidateException("Invoice Item unit price must be provided or must be greater than 0"); }

            return new InvoiceItem(UUID.randomUUID(), this.description, this.quantity, this.unitPrice);
        }
    }
}
