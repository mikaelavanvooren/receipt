package com.receiptReader.service;

import com.receiptReader.dto.PriceUpdateEvent;
import com.receiptReader.model.Price;
import com.receiptReader.model.Product;
import com.receiptReader.model.Store;
import com.receiptReader.repository.PriceRepository;
import com.receiptReader.repository.ProductRepository;
import com.receiptReader.repository.StoreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class PriceUpdateService {
    private static final Logger logger = LoggerFactory.getLogger(PriceUpdateService.class);

    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final PriceRepository priceRepository;

    public PriceUpdateService(StoreRepository storeRepository, ProductRepository productRepository, PriceRepository priceRepository) {
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
        this.priceRepository = priceRepository;
    }   

    @Transactional
    public void processEvent(PriceUpdateEvent event) {
        if(event.getPrice() <= 0) {
            logger.warn("Rejected event - price must be a positive price: {}", event);
            return;
        }
        if (event.getStoreName() == null || event.getStoreName().isBlank()) {
            logger.warn("Rejected event - store name is required: {}", event);
            return;
        }
        if (event.getProductInfo() == null || event.getProductInfo().getName() == null || event.getProductInfo().getName().isBlank()) {
            logger.warn("Rejected event - product name is required: {}", event);
            return;
        }

        Store store = storeRepository.findByName(event.getStoreName())
            .orElseGet(() -> {
                logger.info("Store '{}' not found, creating new store", event.getStoreName());
                return storeRepository.save(new Store(event.getStoreName()));
            });
        Product product = productRepository.findByName(event.getProductInfo().getName())
            .orElseGet(() -> {
                logger.info("Product '{}' not found, creating new product", event.getProductInfo().getName());
                return productRepository.save(new Product(event.getProductInfo().getName(), event.getProductInfo().getCategory()));
            });

        LocalDate date = parseDate(event.getTimestamp());

        boolean alreadyExists = priceRepository.existsByProductIdAndStoreIdAndDate(product.getId(), store.getId(), date);
        if (alreadyExists) {
            logger.warn("Price for product '{}' at store '{}' on date {} already exists, skipping event: {}", product.getName(), event.getStoreName(), date, event);
            return;
        }

        BigDecimal priceValue = BigDecimal.valueOf(event.getPrice());
        Price price = new Price(product, store, priceValue, date);
        priceRepository.save(price);

        logger.info("Saved - {} at {} for ${} on {}", product.getName(), event.getStoreName(), event.getPrice(), date);
    }
    
    private LocalDate parseDate(String timestamp) {
        try {
            return LocalDateTime.parse(timestamp).toLocalDate();
        } catch (Exception e) {
            logger.warn("Failed to parse timestamp '{}', defaulting to today's date", timestamp);
            return LocalDate.now();
        }
    }
}