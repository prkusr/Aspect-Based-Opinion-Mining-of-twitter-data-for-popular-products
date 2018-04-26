package edu.bigdata.kafkaspark;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Constants implements Serializable {
    private final Properties props;
    private final Map<String, String> kafkaSparkStreamConf;
    private final Producer<String, String> kafkaProducer;

    private static Constants singleton = null;
    private Constants() {
        props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        kafkaSparkStreamConf = new HashMap<>();
        kafkaSparkStreamConf.put("metadata.broker.list", "localhost:9092");

        kafkaProducer = new KafkaProducer<>(props);
    }

    private static Constants getSingleton() {
        if (singleton == null)
            singleton = new Constants();
        return singleton;
    }

    public static Properties kafkaConsumerProperties() {
        return getSingleton().props;
    }

    public static Map<String, String> kafkaSparkStreamConfig() {
        return getSingleton().kafkaSparkStreamConf;
    }

    public static Producer<String, String> kafkaProducer() {
        return getSingleton().kafkaProducer;
    }

    public final static String SENDING_TOPIC = "opinions";
    public static final String RECEIVING_TOPIC = "tweets";
}
