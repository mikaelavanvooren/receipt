package com.receiptReader.dto;

import java.math.BigDecimal;

public class ReceiptItemDTO {
    private String name;
    private BigDecimal price;

    public ReceiptItemDTO(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}