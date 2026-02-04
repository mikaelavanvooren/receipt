package com.receiptReader.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.receiptReader.dto.ParsedReceiptDTO;
import com.receiptReader.dto.ReceiptItemDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class AIReceiptParserService {
    @Value ("${openai.api.key}")
    private String apiKey;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /** Parses receipt text using OpenAI's GPT model
    * @param receiptText The raw text of the receipt
    * @return ParsedReceiptDTO containing store name and list of items
    */
    public ParsedReceiptDTO parseReceiptWithAI(String receiptText) {
        try {
            String prompt = buildPrompt(receiptText);
            String response = callOpenAIAPI(prompt);
            return parseAIResponse(response);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse receipt with AI", e);
        }
    }

    private String buildPrompt(String receiptText) {
        return """
            You are a receipt parser. Extract the following from this receipt:
            1. Store name
            2. List of items with their prices (exclude tax, subtotal, total)
            
            Return ONLY valid JSON in this exact format (no markdown, no explanation, no ```json):
            {
              "storeName": "store name here",
              "items": [
                {"name": "item name", "price": 3.99},
                {"name": "item name", "price": 2.49}
              ]
            }
            
            Receipt text:
            %s
            """.formatted(receiptText);
    }

    private String callOpenAIAPI(String prompt) throws Exception {
        String requestBody = """
            {
              "model": "gpt-4o-mini",
              "messages": [
                {
                  "role": "system",
                  "content": "You are a receipt parser that returns only valid JSON."
                },
                {
                  "role": "user",
                  "content": %s
                }
              ],
              "temperature": 0.1
            }
            """.formatted(objectMapper.writeValueAsString(prompt));

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.openai.com/v1/chat/completions"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + apiKey)
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 200) {
            throw new RuntimeException("OpenAI API returned status " + response.statusCode() + ": " + response.body());
        }

        return response.body();
    }

    private ParsedReceiptDTO parseAIResponse(String apiResponse) throws Exception {
            JsonNode root = objectMapper.readTree(apiResponse);

            JsonNode contentNode = root.path("choices").path(0).path("message").path("content");
            if (contentNode.isMissingNode() || contentNode.isNull()) {
                throw new Exception("Unexpected OpenAI API response: missing choices[0].message.content");
            }
            String contentText = contentNode.asText();

            contentText = contentText.replaceAll("```json\\n", "").replaceAll("```\\n", "").trim();

            JsonNode receiptData = objectMapper.readTree(contentText);
            JsonNode receiptData = objectMapper.readTree(contentText);

            if (receiptData == null
                || !receiptData.hasNonNull("storeName")
                || !receiptData.has("items")
                || !receiptData.get("items").isArray()) {
                throw new RuntimeException("Invalid AI response: missing required fields 'storeName' or 'items'.");
                JsonNode priceNode = itemNode.get("price");
                if (priceNode == null || !priceNode.isNumber()) {
                    throw new IllegalArgumentException("Invalid or missing price for item: " + name);
                }
                BigDecimal price = priceNode.decimalValue();

            String storeName = receiptData.get("storeName").asText();

            List<ReceiptItemDTO> items = new ArrayList<>();
            JsonNode itemsArray = receiptData.get("items");
            for (JsonNode itemNode : itemsArray) {
                if (itemNode == null
                    || !itemNode.hasNonNull("name")
                    || !itemNode.hasNonNull("price")) {
                    throw new RuntimeException("Invalid AI response: each item must have non-null 'name' and 'price'.");
                }

            List<ReceiptItemDTO> items = new ArrayList<>();
            JsonNode itemsArray = receiptData.path("items");
            if (!itemsArray.isArray()) {
                throw new Exception("Parsed receipt JSON has no valid 'items' array");
            }
            for (JsonNode itemNode : itemsArray) {
                JsonNode nameNode = itemNode.path("name");
                JsonNode priceNode = itemNode.path("price");

                if (nameNode.isMissingNode() || nameNode.isNull() || priceNode.isMissingNode() || priceNode.isNull()) {
                    throw new Exception("Parsed receipt item is missing 'name' or 'price' field");
                }

                String name = nameNode.asText();
                String priceText = priceNode.asText();
                BigDecimal price = new BigDecimal(priceText);
                items.add(new ReceiptItemDTO(name, price));
            }
            return new ParsedReceiptDTO(storeName, items);
    }
}