package com.receiptReader.controller;

import com.receiptReader.model.Price;
import com.receiptReader.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/prices")
public class PriceController {
    @Autowired
    private PriceRepository priceRepository;

    @GetMapping
    public List<Price> getAllPrices() {
        return priceRepository.findAll();
    }

    @PostMapping
    public Price createPrice(@RequestBody Price price) {
        return priceRepository.save(price);
    }
}   