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
            throw new RuntimeException("OpenAI API error: " + response.body());
        }

        return response.body();
    }

    private ParsedReceiptDTO parseAIResponse(String apiResponse) throws Exception {
            JsonNode root = objectMapper.readTree(apiResponse);

            String contentText = root
                .get("choices")
                .get(0)
                .get("message")
                .get("content")
                .asText();

            contentText = contentText.replaceAll("```json\\n", "").replaceAll("```\\n", "").trim();

            JsonNode receiptData = objectMapper.readTree(contentText);
            String storeName = receiptData.get("storeName").asText();

            List<ReceiptItemDTO> items = new ArrayList<>();
            JsonNode itemsArray = receiptData.get("items");
            for (JsonNode itemNode : itemsArray) {
                String name = itemNode.get("name").asText();
                BigDecimal price = new BigDecimal(itemNode.get("price").asText());
                items.add(new ReceiptItemDTO(name, price));
            }
            return new ParsedReceiptDTO(storeName, items);
    }
}