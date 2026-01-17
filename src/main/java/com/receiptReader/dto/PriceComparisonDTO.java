package com.receiptReader.dto;

import java.util.List;

public class PriceComparisonDTO {
    private Long productId;
    private String productName;
    private List<StorePriceDTO> prices;

    public PriceComparisonDTO(Long productId, String productName, List<StorePriceDTO> prices) {
        this.productId = productId;
        this.productName = productName;
        this.prices = prices;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public List<StorePriceDTO> getPrices() {
        return prices;
    }
}