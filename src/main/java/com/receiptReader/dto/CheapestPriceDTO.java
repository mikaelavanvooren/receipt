package com.receiptReader.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CheapestPriceDTO {
    private Long productId;
    private String productName;
    private BigDecimal cheapestPrice;
    private String storeName;
    private LocalDate date;

    public CheapestPriceDTO(Long productId, String productName, BigDecimal cheapestPrice, String storeName, LocalDate date) {
        this.productId = productId;
        this.productName = productName;
        this.cheapestPrice = cheapestPrice;
        this.storeName = storeName;
        this.date = date;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getStoreName() {
        return storeName;
    }

    public BigDecimal getCheapestPrice() {
        return cheapestPrice;
    }

    public LocalDate getDate() {
        return date;
    }
}