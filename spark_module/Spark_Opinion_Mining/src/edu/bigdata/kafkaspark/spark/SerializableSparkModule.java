package edu.bigdata.kafkaspark.spark;

import edu.bigdata.kafkaspark.helper.Constants;
import edu.bigdata.kafkaspark.manager.AspectCategory;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import scala.Tuple2;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class SerializableSparkModule implements Serializable {
    private final Producer<String, String> kafkaProducer;
    private final AspectCategory aspectCategory;
    private static final String topicName = Constants.SENDING_TOPIC;

    public SerializableSparkModule(Producer<String, String> kafkaProducer, AspectCategory aspectCategory) {
        this.kafkaProducer = kafkaProducer;
        this.aspectCategory = aspectCategory;
    }

    public void process(Tuple2<String, String> record) {
        System.out.println("Spark Module Sending Opinions!");
        String tweet = record._2;

        Map<String, List<String>> opinion = aspectCategory.aspectCategoryToWords(tweet);

        if (!opinion.isEmpty()) {
            // TODO: Convert dictionary to JSON
            System.out.println(opinion.toString());
            this.kafkaProducer.send(new ProducerRecord<>(topicName, null, opinion.toString()));
        }
    }
}
