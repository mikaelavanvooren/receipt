package com.receiptReader.dto;

import java.util.List;

public class ParsedReceiptDTO {
    private String storeName;
    private List<ReceiptItemDTO> items;

    public ParsedReceiptDTO(String storeName, List<ReceiptItemDTO> items) {
        this.storeName = storeName;
        this.items = items;
    }

    public String getStoreName() {
        return storeName;
    }

    public List<ReceiptItemDTO> getItems() {
        return items;
    }
}