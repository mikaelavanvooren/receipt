package com.receiptReader.controller;

import com.receiptReader.dto.CheapestPriceDTO;
import com.receiptReader.dto.PriceMapper;
import com.receiptReader.dto.PriceResponseDTO;
import com.receiptReader.dto.PriceComparisonDTO;
import com.receiptReader.model.Price;
import com.receiptReader.repository.PriceRepository;
import com.receiptReader.service.PriceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/prices")
public class PriceController {

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private PriceService priceService;

    @GetMapping
    public List<PriceResponseDTO> getAllPrices() {
        return priceRepository.findAll()
                .stream()
                .map(PriceMapper::toDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    public Price createPrice(@Valid @RequestBody Price price) {
        return priceRepository.save(price);
    }

    @GetMapping("/product/{productId}")
    public List<PriceResponseDTO> getPricesByProductId(@PathVariable Long productId) {
        return priceRepository.findByProductId(productId)
                .stream()
                .map(PriceMapper::toDTO)
                .collect(Collectors.toList());
    }   

    @GetMapping("/store/{storeId}")
    public List<PriceResponseDTO> getPricesByStoreId(@PathVariable Long storeId) {
        return priceRepository.findByStoreId(storeId)
                .stream()
                .map(PriceMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/product/{productId}/store/{storeId}")
    public List<PriceResponseDTO> getPricesByProductIdAndStoreId(@PathVariable Long productId, @PathVariable Long storeId) {
        return priceRepository.findByProductIdAndStoreId(productId, storeId)
                .stream()
                .map(PriceMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/cheapest/{productId}")
    public CheapestPriceDTO getCheapestPriceForProduct(@PathVariable Long productId) {
        return priceService.getCheapestPriceForProduct(productId);
    }

    @GetMapping("/compare/{productId}")
    public PriceComparisonDTO comparePricesForProduct(@PathVariable Long productId) {
        return priceService.comparePricesForProduct(productId);
    }
}