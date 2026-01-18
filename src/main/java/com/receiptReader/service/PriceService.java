package com.receiptReader.service;

import com.receiptReader.dto.CheapestPriceDTO;
import com.receiptReader.dto.PriceComparisonDTO;
import com.receiptReader.dto.StorePriceDTO;
import com.receiptReader.exception.ResourceNotFoundException;
import com.receiptReader.model.Price;
import com.receiptReader.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
public class PriceService {

    @Autowired
    private PriceRepository priceRepository;

    public CheapestPriceDTO getCheapestPriceForProduct(Long productId) {
        List<Price> prices = priceRepository.findByProductId(productId);

        if(prices.isEmpty()) {
            throw new ResourceNotFoundException("No prices found for product with id " + productId);
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

    public PriceComparisonDTO comparePricesForProduct(Long productId) {
        List<Price> prices = priceRepository.findByProductId(productId);

        if(prices.isEmpty()) {
            throw new ResourceNotFoundException("No prices found for product with id " + productId);
        }

        String productName = prices.get(0).getProduct().getName();

        List<StorePriceDTO> storePrices = prices.stream()
                .map(price -> new StorePriceDTO(
                        price.getStore().getName(),
                        price.getPrice(),
                        price.getDate()
                ))
                .sorted(Comparator.comparing(StorePriceDTO::getPrice))
                .collect(Collectors.toList());

        return new PriceComparisonDTO(
                productId,
                productName,
                storePrices
        );
    }
}
