package com.receiptReader.dto;

public class PriceUpdateEvent {
    private String storeName;
    private ProductInfo productInfo;
    private double price;
    private String timestamp;

    public PriceUpdateEvent() {}

    public String getStoreName() {
        return storeName;
    }
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public ProductInfo getProductInfo() {
        return productInfo;
    }
    public void setProductInfo(ProductInfo productInfo) {
        this.productInfo = productInfo;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return String.format("PriceUpdateEvent{storeName='%s', productInfo=%s, category='%s', price=%.2f, timestamp='%s'}",
                storeName, 
                productInfo != null ? productInfo.getName() : "null", 
                productInfo != null ? productInfo.getCategory() : "null",  
                price, 
                timestamp);
    }

    public static class ProductInfo {
        private String name;
        private String category;

        public ProductInfo() {}

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

        public String getCategory() {
            return category;
        }
        public void setCategory(String category) {
            this.category = category;
        }
    }

}