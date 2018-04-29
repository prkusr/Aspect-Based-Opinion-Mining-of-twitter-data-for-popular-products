package edu.bigData.sparkBusters.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.JSONObject;

import java.util.*;

public class KafkaService {

    private static final String RECEIVER_TOPIC_NAME = "opinions";
    private static final String SENDER_TOPIC_NAME = "search_string";
    private static final String SEARCH_STRING = "searchString";
    private static final String ID = "id";
    private static final String TOTAL_TWEETS = "totalTweets";
    private static final String IS_OPINION = "isOpinion";

    public List<String> consume(String searchString) {
        Properties props = new Properties();

        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "false");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

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
                    System.out.println(""+ record.value());
                    System.out.printf("\n\n\n\nException for the tweet : %s \nMessage: %s\n\n\n", record.value(), e.getMessage());
                }
            }
        }
        return new ArrayList<>(idToOpinionTweet.values());
    }

    public void producer(String searchString) {

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);

        producer.send(new ProducerRecord<>(SENDER_TOPIC_NAME, null, searchString));
        producer.close();
    }
}
