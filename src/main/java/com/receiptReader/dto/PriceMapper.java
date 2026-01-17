package com.receiptReader.dto;

import com.receiptReader.model.Price;

public class PriceMapper {

    public static PriceResponseDTO toDTO(Price price) {
        return new PriceResponseDTO(
            price.getId(),
            price.getProduct().getId(),
            price.getProduct().getName(),
            price.getStore().getId(),
            price.getStore().getName(),
            price.getPrice(),
            price.getDate()
        );
    }
}
