package edu.bigdata.kafkaspark.spark;

import edu.bigdata.kafkaspark.helper.Constants;
import edu.bigdata.kafkaspark.manager.AspectCategory;
import edu.bigdata.kafkaspark.model.TweetJSON;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.JSONObject;
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

    @SuppressWarnings("unchecked")
    public void process(Tuple2<String, String> record) {
        System.out.println("Spark Module Sending Opinions!");
        String tweetJsonString = record._2;
        TweetJSON tweetJSON = TweetJSON.createTweetJSON(tweetJsonString);

        if (tweetJSON != null) {
            Map<String, List<String>> opinion = aspectCategory.aspectCategoryToWords(tweetJSON.tweet());
            if (!opinion.isEmpty()) {
                JSONObject opinionJSON = tweetJSON.opinionJSON(opinion);
                if (opinionJSON != null) {
                    System.out.println(opinionJSON);
                    this.kafkaProducer.send(new ProducerRecord<>(topicName, null, opinionJSON.toString()));
                }
            }
        }
    }
}
