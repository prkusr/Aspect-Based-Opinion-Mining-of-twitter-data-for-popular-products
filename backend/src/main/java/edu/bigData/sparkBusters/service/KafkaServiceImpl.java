package edu.bigData.sparkBusters.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class KafkaServiceImpl implements KafkaService {

    private final KafkaProducer<String, String > producer;
    private final KafkaConsumer<String, String> consumer;

    private static final String RECEIVER_TOPIC_NAME = "opinions";
    private static final String SENDER_TOPIC_NAME = "search_string";
    private static final String SEARCH_STRING = "searchString";
    private static final String ID = "id";
    private static final String TOTAL_TWEETS = "totalTweets";
    private static final String IS_OPINION = "isOpinion";

    @Autowired
    public KafkaServiceImpl(KafkaProducer<String, String> producer, KafkaConsumer<String, String> consumer) {
        this.producer = producer;
        this.consumer = consumer;
    }

    @Override
    public List<String> consume(String searchString) {
        consumer.subscribe(Collections.singletonList(RECEIVER_TOPIC_NAME));
        System.out.println("Subscribed to topic " + RECEIVER_TOPIC_NAME);

        int tweetCount = Integer.MAX_VALUE;
        int receivedTweetCount = 0;
        Map<Long, String> idToOpinionTweet = new HashMap<>();

        while (receivedTweetCount < tweetCount) {
            ConsumerRecords<String, String> records = consumer.poll(10);

            for (ConsumerRecord<String, String> record : records) {
                try {
                    String tweetJsonString = record.value();
                    JSONObject tweetsJson = new JSONObject(tweetJsonString);

                    String receivedSearchString = tweetsJson.getString(SEARCH_STRING);
                    Long receivedTweetId = tweetsJson.getLong(ID);
                    if (!idToOpinionTweet.containsKey(receivedTweetId) && receivedSearchString.equals(searchString)) {
                        receivedTweetCount ++;
                        tweetCount = tweetsJson.getInt(TOTAL_TWEETS);
                        consumer.commitAsync();
                        if (tweetsJson.getBoolean(IS_OPINION))
                            idToOpinionTweet.put(receivedTweetId, tweetJsonString);
                    }
                } catch (Exception e) {
                    System.out.printf("\n\n\n\nException for the tweet : %s \nMessage: %s\n\n\n", record.value(), e.getMessage());
                }
            }
        }
        return new ArrayList<>(idToOpinionTweet.values());
    }

    @Override
    public void producer(String searchString) {
        try {
            producer.send(new ProducerRecord<>(SENDER_TOPIC_NAME, null, searchString));
            producer.close();
        } catch (Exception e) {
            System.out.printf("\n\n\n\nCannot connect to Kafka. Exception message: %s\n\n\n", e.getMessage());
        }
    }
}
