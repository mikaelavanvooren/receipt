package com.receiptReader.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class StorePriceDTO {
    private String storeName;
    private BigDecimal price;
    private LocalDate date;

    public StorePriceDTO(String storeName, BigDecimal price, LocalDate date) {
        this.storeName = storeName;
        this.price = price;
        this.date = date;
    }

    public String getStoreName() {
        return storeName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public LocalDate getDate() {
        return date;
    }
}