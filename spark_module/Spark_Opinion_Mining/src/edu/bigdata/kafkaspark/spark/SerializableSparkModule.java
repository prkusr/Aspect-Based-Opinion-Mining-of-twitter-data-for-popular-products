package edu.bigdata.kafkaspark.spark;

import edu.bigdata.kafkaspark.helper.Constants;
import edu.bigdata.kafkaspark.manager.AspectCategoryCreator;
import edu.bigdata.kafkaspark.model.AspectCategories;
import edu.bigdata.kafkaspark.model.TweetJSON;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.JSONObject;
import scala.Tuple2;

import java.io.Serializable;

public class SerializableSparkModule implements Serializable {
    private final Producer<String, String> kafkaProducer;
    private final AspectCategoryCreator aspectCategoryCreator;
    private static final String topicName = Constants.SENDING_TOPIC;

    public SerializableSparkModule(Producer<String, String> kafkaProducer, AspectCategoryCreator aspectCategoryCreator) {
        this.kafkaProducer = kafkaProducer;
        this.aspectCategoryCreator = aspectCategoryCreator;
    }

    @SuppressWarnings("unchecked")
    public void process(Tuple2<String, String> record) {
        System.out.print("Spark Module : ");
        String tweetJsonString = record._2;
        TweetJSON tweetJSON = TweetJSON.createTweetJSON(tweetJsonString);

        if (tweetJSON != null && tweetJSON.tweet() != null) {
            AspectCategories opinion = aspectCategoryCreator.aspectCategoryToWords(tweetJSON.tweet());
            JSONObject opinionJSON = tweetJSON.opinionJSON(opinion);
            if (opinionJSON != null) {
                System.out.printf("Sent data! - %s\n\n", opinionJSON);
                try {
                    this.kafkaProducer.send(new ProducerRecord<>(topicName, null, opinionJSON.toString()));
                } catch (Exception e) {
                    System.out.println(e.toString());
                    System.out.println(e.getMessage());
            }
        }
    }
}
