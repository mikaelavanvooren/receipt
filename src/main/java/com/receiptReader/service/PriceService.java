package com.receiptReader.service;

import com.receiptReader.dto.CheapestPriceDTO;
import com.receiptReader.model.Price;
import com.receiptReader.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Comparator;

@Service
public class PriceService {

    @Autowired
    private PriceRepository priceRepository;

    public CheapestPriceDTO getCheapestPriceForProduct(Long productId) {
        List<Price> prices = priceRepository.findByProductId(productId);

        if(prices.isEmpty()) {
            return null;
        }

        Price getCheapestPrice = prices.stream()
                .min(Comparator.comparing(Price::getPrice))
                .orElseThrow();

        return new CheapestPriceDTO(
                getCheapestPrice.getProduct().getId(),
                getCheapestPrice.getProduct().getName(),
                getCheapestPrice.getPrice(),
                getCheapestPrice.getStore().getName(),
                getCheapestPrice.getDate()
        );
    }
}
