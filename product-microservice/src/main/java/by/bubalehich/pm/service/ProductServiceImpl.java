package by.bubalehich.pm.service;


import by.bubalehich.pm.dto.CreateProductDto;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import by.bubalehich.core.ProductCreatedEvent;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    private KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

    public ProductServiceImpl(KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public String createProduct(CreateProductDto dto) throws ExecutionException, InterruptedException {
        //TODO database save
        String productId = UUID.randomUUID().toString();

        ProductCreatedEvent event = new ProductCreatedEvent(productId, dto.getTitle(), dto.getPrice(), dto.getQuantity());


//        CompletableFuture<SendResult<String, ProductCreatedEvent>> future = kafkaTemplate
//                .send("product-created-events-topic", productId, event);
//        future.whenComplete((result, exception) -> {
//            if ((exception != null)) {
//                LOGGER.error("Failed to send message: {}", exception.getMessage());
//            } else {
//                LOGGER.info("Message send successfully: {}", result.getRecordMetadata());
//            }
//        });

        SendResult<String, ProductCreatedEvent> result = kafkaTemplate
                .send("product-created-events-topic", productId, event).get();

        log.info("Topic: {}", result.getRecordMetadata().topic());
        log.info("Partition: {}", (result.getRecordMetadata().partition()));
        log.info("Offset: {}", result.getRecordMetadata().offset());

        log.info("Return: {}", productId);

        return productId;
    }
}
