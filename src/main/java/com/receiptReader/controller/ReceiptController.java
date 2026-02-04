package com.receiptReader.controller;

import com.receiptReader.dto.ParsedReceiptDTO;
import com.receiptReader.service.ReceiptParserService;
import com.receiptReader.service.AIReceiptParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/receipts")
public class ReceiptController {

    // @Autowired
    // private ReceiptParserService receiptParserService;

    // @PostMapping("/parse")
    // public ParsedReceiptDTO parseReceiptText(@RequestBody String receiptText) {
    //     return receiptParserService.parseReceiptText(receiptText);
    // }

    @Autowired
    private AIReceiptParserService aiReceiptParserService;
    
    /**
     * Parse receipt using AI (handles messy receipts)
     */
    @PostMapping("/parse")
    public ParsedReceiptDTO parseReceipt(@RequestBody String receiptText) {
        return aiReceiptParserService.parseReceiptWithAI(receiptText);
    }
}