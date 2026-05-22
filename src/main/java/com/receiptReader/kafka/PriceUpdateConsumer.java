package com.receiptReader.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.receiptReader.dto.PriceUpdateEvent;
import com.receiptReader.service.PriceUpdateService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PriceUpdateConsumer {
    private static final Logger logger = LoggerFactory.getLogger(PriceUpdateConsumer.class);
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PriceUpdateService priceUpdateService;

    public PriceUpdateConsumer(PriceUpdateService priceUpdateService) {
        this.priceUpdateService = priceUpdateService;
    }

    @KafkaListener(topics = "price-updates", groupId = "price-consumer-group")
    public void consumePriceUpdate(ConsumerRecord<String, String> record) {
        logger.info("Received price update message: key={}, value={}, partition={}, offset={}",
                record.key(), record.value(), record.partition(), record.offset());
        try {
            PriceUpdateEvent event = objectMapper.readValue(record.value(), PriceUpdateEvent.class);
            priceUpdateService.processEvent(event);
        } catch (Exception e) {
            logger.error("Failed to to process message at offset {}: {}", record.offset(), record.value(), e);
        }
    }
}