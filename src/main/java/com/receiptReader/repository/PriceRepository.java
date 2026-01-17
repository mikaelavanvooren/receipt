package com.receiptReader.repository;

import com.receiptReader.model.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {
    List<Price> findByProductId(Long productId);

    List<Price> findByStoreId(Long storeId);

    List<Price> findByProductIdAndStoreId(Long productId, Long storeId);
}   