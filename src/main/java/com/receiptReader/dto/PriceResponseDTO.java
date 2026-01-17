package com.receiptReader.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PriceResponseDTO {
    private Long id;
    private Long productId;
    private String productName;
    private Long storeId;
    private String storeName;
    private BigDecimal price;
    private LocalDate date;

    public PriceResponseDTO(Long id, Long productId, String productName, Long storeId, String storeName, BigDecimal price, LocalDate date) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.storeId = storeId;
        this.storeName = storeName;
        this.price = price;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Long getStoreId() {
        return storeId;
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