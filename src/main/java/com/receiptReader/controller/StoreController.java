package com.receiptReader.controller;

import com.receiptReader.model.Store;
import com.receiptReader.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/stores")
public class StoreController {

    @Autowired
    private StoreRepository StoreRepository;

    @GetMapping
    public List<Store> getAllStores() {
        return StoreRepository.findAll();
    }

    @PostMapping
    public Store createStore(@RequestBody Store store) {
        return StoreRepository.save(store);
    }
}   