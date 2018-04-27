package edu.bigdata.kafkaspark;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import scala.Tuple2;

import java.io.Serializable;

public class SerializableSparkModule implements Serializable {
    private final Producer<String, String> kafkaProducer;
    private final String topicName;

    SerializableSparkModule(Producer<String, String> kafkaProducer, String topicName) {
        this.kafkaProducer = kafkaProducer;
        this.topicName = topicName;
    }

    public void process(Tuple2<String, String> record) {
        System.out.println("Spark Module Sending Opinions!");
        String message = record._2;
        this.kafkaProducer.send(new ProducerRecord<>(this.topicName, null, message));
    }
}
