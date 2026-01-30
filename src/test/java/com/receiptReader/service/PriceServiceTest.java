package com.receiptReader.service;

import com.receiptReader.dto.CheapestPriceDTO;
import com.receiptReader.exception.ResourceNotFoundException;
import com.receiptReader.model.Price;
import com.receiptReader.model.Product;
import com.receiptReader.model.Store;
import com.receiptReader.repository.PriceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PriceServiceTest {

    @Mock
    private PriceRepository priceRepository;
    
    @InjectMocks
    private PriceService priceService;

    @Test
    void findCheapestPrice_shouldReturnLowestPrice_whenMultiplePricesExist() {
        Long productId = 1L;

        Product milk = new Product();
        milk.setId(productId);
        milk.setName("Milk");
        milk.setCategory("Dairy");

        Store walmart = new Store();
        walmart.setName("Walmart");
        walmart.setId(1L);

        Store target = new Store();
        target.setName("Target");
        target.setId(2L);

        Price price1 = new Price(milk, walmart, new BigDecimal("3.99"), LocalDate.now());
        Price price2 = new Price(milk, target, new BigDecimal("4.28"), LocalDate.now());
        Price price3 = new Price(milk, target, new BigDecimal("3.50"), LocalDate.now());
        when(priceRepository.findByProductId(productId)).thenReturn(Arrays.asList(price1, price2, price3));

        CheapestPriceDTO cheapestPriceDTO = priceService.getCheapestPriceForProduct(productId);

        assertNotNull(cheapestPriceDTO);
        assertEquals(productId, cheapestPriceDTO.getProductId());
        assertEquals("Milk", cheapestPriceDTO.getProductName());
        assertEquals(new BigDecimal("3.50"), cheapestPriceDTO.getCheapestPrice());
        assertEquals("Target", cheapestPriceDTO.getStoreName());
    }

    @Test
    void findCheapestPrice_shouldThrowException_whenNoPricesExist() {

        when(priceRepository.findByProductId(999L)).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> {
            priceService.getCheapestPriceForProduct(999L);
        });

        verify(priceRepository, times(1)).findByProductId(999L);
    }
}