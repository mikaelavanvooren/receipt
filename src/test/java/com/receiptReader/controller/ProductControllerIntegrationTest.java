package com.receiptReader.controller;

import com.receiptReader.model.Product;
import com.receiptReader.repository.ProductRepository;
import com.receiptReader.repository.PriceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
public class ProductControllerIntegrationTest { 

    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private PriceRepository priceRepository;
    
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        priceRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    void getAllProducts_shouldReturnEmptyList_whenNoProducts() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void createProduct_shouldReturnCreatedProduct() throws Exception {
        String productJson = "{\"name\":\"Bread\",\"category\":\"Bakery\"}";

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Bread"))
                .andExpect(jsonPath("$.category").value("Bakery"));

        assert productRepository.findAll().size() == 1;
    }

    @Test
    void getAllProducts_shouldReturnListOfProducts() throws Exception {
        Product product1 = new Product("Eggs", "Dairy");
        productRepository.save(product1);

        Product product2 = new Product("Apples", "Fruit");
        productRepository.save(product2);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Eggs"))
                .andExpect(jsonPath("$[0].category").value("Dairy"))
                .andExpect(jsonPath("$[1].name").value("Apples"))
                .andExpect(jsonPath("$[1].category").value("Fruit"))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void createProductById_shouldReturnProduct() throws Exception {
        Product product = new Product("Bananas", "Fruit");
        Product savedProduct = productRepository.save(product);

        mockMvc.perform(get("/api/products/" + savedProduct.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Bananas"))
                .andExpect(jsonPath("$.category").value("Fruit"));
    }

    @Test 
    void getProductById_shouldReturn404_whenProductNotFound() throws Exception {
        mockMvc.perform(get("/api/products/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Product with ID 999 not found"));
    }

    @Test
    void createProduct_shouldReturn400_whenInvalidInput() throws Exception {
        String productJson = "{\"name\":\"\",\"category\":\"\"}";

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Product name is required"));
    }
}