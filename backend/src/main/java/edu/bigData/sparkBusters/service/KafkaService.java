package edu.bigData.sparkBusters.service;

import edu.bigData.sparkBusters.model.Tweet;
import edu.bigData.sparkBusters.storage.MessageStorage;
import org.json.JSONObject;
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
    private static final String SEARCH_STRING_KEY = "searchString";
    private static final String LOCATION_KEY = "location";
    private static final String RECEIVER_TOPIC_NAME = "opinions";
    private static final String SENDER_TOPIC_NAME = "search_string";

    private KafkaTemplate<String, String> kafkaTemplate;
    private final MessageStorage messageStorage;

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

    private JSONObject getJsonObject(String searchString, String location) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(SEARCH_STRING_KEY, searchString);
        if (location != null && !location.isEmpty())
            jsonObject.put(LOCATION_KEY, location);
        return jsonObject;
    }

    public void producer(String searchString, String location) {
        try {
            JSONObject jsonObject = getJsonObject(searchString, location);
            kafkaTemplate.send(SENDER_TOPIC_NAME, jsonObject.toString());
        } catch (Exception e) {
            System.out.printf("\n\n\n\nCannot connect to Kafka. Exception message: %s\n\n\n", e.toString());
        }
    }

    public List<Tweet> consume(String searchString) {
        return messageStorage.pollFor(searchString, 100);
    }
}
