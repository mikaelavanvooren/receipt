package com.receiptReader.controller;

import com.receiptReader.model.Price;
import com.receiptReader.model.Product;
import com.receiptReader.model.Store;
import com.receiptReader.repository.PriceRepository;
import com.receiptReader.repository.ProductRepository;
import com.receiptReader.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
public class PriceControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Autowired
    private PriceRepository priceRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private StoreRepository storeRepository;
    
    private org.springframework.test.web.servlet.MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        priceRepository.deleteAll();
        productRepository.deleteAll();
        storeRepository.deleteAll();
    }

    @Test
    void getCheapestPrice_shouldReturnLowestPrice_whenMultiplePricesExist() throws Exception {
        Product milk = new Product();
        milk.setName("Milk");
        milk.setCategory("Dairy");
        productRepository.save(milk);

        Store walmart = new Store();
        walmart.setName("Walmart");
        storeRepository.save(walmart);

        Store target = new Store();
        target.setName("Target");
        storeRepository.save(target);

        Price price1 = new Price(milk, walmart, new BigDecimal("3.99"), LocalDate.now());
        Price price2 = new Price(milk, target, new BigDecimal("4.28"), LocalDate.now());
        Price price3 = new Price(milk, target, new BigDecimal("3.50"), LocalDate.now());
        priceRepository.save(price1);
        priceRepository.save(price2);
        priceRepository.save(price3);

        mockMvc.perform(get("/api/prices/cheapest/" + milk.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(milk.getId()))
                .andExpect(jsonPath("$.productName").value("Milk"))
                .andExpect(jsonPath("$.cheapestPrice").value(3.50))
                .andExpect(jsonPath("$.storeName").value("Target"));
    }

    @Test
    void getCheapestPrice_shouldReturn404_whenNoPricesExist() throws Exception {
        Product bread = new Product();
        bread.setName("Bread");
        bread.setCategory("Bakery");
        productRepository.save(bread);

        mockMvc.perform(get("/api/prices/cheapest/" + bread.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No prices found for product with id " + bread.getId()));
    }

    @Test
    void comparePrice_shouldReturnSortedPrices_whenMultiplePricesExist() throws Exception {
        Product eggs = new Product();
        eggs.setName("Eggs");
        eggs.setCategory("Dairy");
        productRepository.save(eggs);

        Store kroger = new Store();
        kroger.setName("Kroger");
        storeRepository.save(kroger);

        Store safeway = new Store();
        safeway.setName("Safeway");
        storeRepository.save(safeway);

        Price price1 = new Price(eggs, kroger, new BigDecimal("2.99"), LocalDate.now());
        Price price2 = new Price(eggs, safeway, new BigDecimal("3.49"), LocalDate.now());
        priceRepository.save(price1);
        priceRepository.save(price2);

        mockMvc.perform(get("/api/prices/compare/" + eggs.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.prices", hasSize(2)))
                .andExpect(jsonPath("$.prices[0].storeName").value("Kroger"))
                .andExpect(jsonPath("$.prices[0].price").value(2.99))
                .andExpect(jsonPath("$.prices[1].storeName").value("Safeway"))
                .andExpect(jsonPath("$.prices[1].price").value(3.49));
    }

    @Test
    void getAllPrices_shouldReturnPricesWithProductAndStoreDetails() throws Exception {
        Product apples = new Product();
        apples.setName("Apples");
        apples.setCategory("Fruit");
        productRepository.save(apples);

        Store wholeFoods = new Store();
        wholeFoods.setName("Whole Foods");
        storeRepository.save(wholeFoods);

        Price price = new Price(apples, wholeFoods, new BigDecimal("1.99"), LocalDate.now());
        priceRepository.save(price);

        mockMvc.perform(get("/api/prices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].productName").value("Apples"))
                .andExpect(jsonPath("$[0].storeName").value("Whole Foods"))
                .andExpect(jsonPath("$[0].price").value(1.99));
    }
}