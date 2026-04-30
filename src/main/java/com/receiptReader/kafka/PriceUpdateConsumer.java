package com.receiptReader.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.receiptReader.dto.PriceUpdateEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PriceUpdateConsumer {
    private static final Logger logger = LoggerFactory.getLogger(PriceUpdateConsumer.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "price-updates", groupId = "price-consumer-group")
    public void consumePriceUpdate(ConsumerRecord<String, String> record) {
        logger.info("Received price update message: key={}, value={}, partition={}, offset={}",
                record.key(), record.value(), record.partition(), record.offset());
        try {
            PriceUpdateEvent event = objectMapper.readValue(record.value(), PriceUpdateEvent.class);

            logger.info("Parsed PriceUpdateEvent: {}", event);
            logger.info("Store: {}, Product: {}, Category: {}, Price: {}, Timestamp: {}",
                    event.getStoreName(),
                    event.getProductInfo() != null ? event.getProductInfo().getName() : "null",
                    event.getProductInfo() != null ? event.getProductInfo().getCategory() : "null",
                    event.getPrice(),
                    event.getTimestamp());
        } catch (Exception e) {
            logger.error("Failed to parse price update message", e);
        }
    }
}