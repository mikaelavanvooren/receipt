package com.receiptReader.controller;

import com.receiptReader.exception.ResourceNotFoundException;
import com.receiptReader.model.Store;
import com.receiptReader.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/stores")
public class StoreController {

    @Autowired
    private StoreRepository StoreRepository;

    @GetMapping
    public List<Store> getAllStores() {
        return StoreRepository.findAll();
    }

    @GetMapping("/{id}")
    public Store getStoreById(@PathVariable Long id) {
        return StoreRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Store not found with id " + id));
    }

    @PostMapping
    public Store createStore(@RequestBody Store store) {
        return StoreRepository.save(store);
    }
}   