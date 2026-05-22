package com.receiptReader.repository;

import com.receiptReader.model.Price;
import com.receiptReader.model.Product;
import com.receiptReader.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDate;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {
    List<Price> findByProductId(Long productId);

    List<Price> findByStoreId(Long storeId);

    List<Price> findByProductIdAndStoreId(Long productId, Long storeId);
    
    boolean existsByProductIdAndStoreIdAndDate(Long productId, Long storeId, LocalDate date);
}   