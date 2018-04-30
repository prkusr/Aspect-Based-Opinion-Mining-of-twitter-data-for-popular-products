package edu.bigData.sparkBusters.service;

import edu.bigData.sparkBusters.model.Tweet;
import edu.bigData.sparkBusters.storage.MessageStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KafkaService {
    private KafkaTemplate<String, String> kafkaTemplate;
    private final MessageStorage messageStorage;

    private static final String RECEIVER_TOPIC_NAME = "opinions";
    private static final String SENDER_TOPIC_NAME = "search_string";

    @Autowired
    public KafkaService(KafkaTemplate<String, String> kafkaTemplate, MessageStorage messageStorage) {
        this.kafkaTemplate = kafkaTemplate;
        this.messageStorage = messageStorage;
    }

    @KafkaListener(topics = RECEIVER_TOPIC_NAME)
    public void listenWithHeaders(@Payload Tweet message, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
        System.out.println("\n\n\nReceived Message: " + message + "from partition: " + partition);
        messageStorage.add(message);
    }

    public void producer(String searchString) {
        try {
            kafkaTemplate.send(SENDER_TOPIC_NAME, searchString);
        } catch (Exception e) {
            System.out.printf("\n\n\n\nCannot connect to Kafka. Exception message: %s\n\n\n", e.toString());
        }
    }

    public List<Tweet> consume(String searchString) {
        return messageStorage.pollFor(searchString, 100);
    }
}
