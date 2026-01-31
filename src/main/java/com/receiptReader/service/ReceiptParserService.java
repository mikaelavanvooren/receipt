package com.receiptReader.service;

import com.receiptReader.dto.ParsedReceiptDTO;
import com.receiptReader.dto.ReceiptItemDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service layer for receipt parsing
 * Handles the extraction of store names and item details (store, date, price) from raw receipt text
 */
@Service
public class ReceiptParserService {
    private static final Pattern ITEM_PATTERN = Pattern.compile(
        "^([A-Za-z\\s]+?)\\s+\\$(\\d+\\.\\d{2})$"
    );

    public ParsedReceiptDTO parseReceiptText(String receiptText) {
        String[] lines = receiptText.split("\n");

        String storeName = extractStoreName(lines);
        List<ReceiptItemDTO> items = extractItems(lines);

        return new ParsedReceiptDTO(storeName, items);
    }

    private String extractStoreName(String[] lines) {
        for (String line : lines) {
            String trim = line.trim();
            if (!trim.isEmpty() && trim.length() > 2) {
                return trim;
            }
        }
        return "Unknown Store";
    }

    /**
     * Extracts item names and prices from receipt lines using regex pattern matching
     * @param lines The lines of text from the receipt
     * @return A list of ReceiptItemDTOs containing item names and prices
     */
    private List<ReceiptItemDTO> extractItems(String[] lines) {
        List<ReceiptItemDTO> items = new ArrayList<>();

        for (String line : lines) {
            String trimmed = line.trim();

            System.out.println("Processing line: '" + trimmed + "'");

            if (trimmed.isEmpty() || trimmed.toLowerCase().contains("total")) {
                System.out.println("  -> Skipped (empty or total)");
                continue;
            }

            Matcher matcher = ITEM_PATTERN.matcher(trimmed);
            if (matcher.matches()) {
                String itemName = matcher.group(1).trim();
                String priceStr = matcher.group(2);
                BigDecimal price = new BigDecimal(priceStr);

                items.add(new ReceiptItemDTO(itemName, price));
            } else {
                System.out.println("  -> Did not match pattern");
            }
        }
        return items;
    }
}