package com.receiptReader.controller;

import com.receiptReader.dto.ParsedReceiptDTO;
import com.receiptReader.service.ReceiptParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/receipts")
public class ReceiptController {

    @Autowired
    private ReceiptParserService receiptParserService;

    @PostMapping("/parse")
    public ParsedReceiptDTO parseReceiptText(@RequestBody String receiptText) {
        return receiptParserService.parseReceiptText(receiptText);
    }
}