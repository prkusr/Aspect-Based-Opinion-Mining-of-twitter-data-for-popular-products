package edu.bigdata.kafkaspark.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.bigdata.kafkaspark.manager.AspectCategoryCreator;
import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.impl.WuPalmer;
import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Constants {
    public static final Integer NUMBER_OF_PARTITIONS_TO_CONSUME = 4;
    private Map<String, List<String>> wordToCategories;
    private final KafkaProducer<String, String> kafkaProducer;
    private final AspectCategoryCreator aspectCategoryCreator;
    private static Constants singleton = null;

    public static final String SENDING_TOPIC = "opinions";
    public static final String RECEIVING_TOPIC = "tweets";
    private static final String TAGGER_PATH = "edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger";

    public static class JSONKeys {
        public static final String TWEET_KEY = "text";
        public static final String SEARCH_STRING_KEY = "searchString";
        public static final String TOTAL_TWEETS_KEY = "totalTweets";
        public static final String LOCATION_KEY = "location";
        public static final String IS_OPINION = "isOpinion";
        public static final String CATEGORY = "category";
        public static final String WORDS = "words";
        public static final String SENTIMENT = "sentiment";
        public static final String ASPECT = "aspects";
        public static final String PLACE = "place";
        public static final String BOUNDING_BOX = "bounding_box";
        public static final String COORDINATES = "coordinates";
    }

    private Constants() {
        ILexicalDatabase wordNet = new NictWordNet();

        Properties kakfaProducerProps = new Properties();
        String hostName = System.getenv("KAFKA_IP") + ":" + System.getenv("KAFKA_PORT");
        kakfaProducerProps.put("bootstrap.servers", hostName);
        kakfaProducerProps.put("acks", "all");
        kakfaProducerProps.put("retries", Integer.valueOf(System.getenv("RETRIES")));
        kakfaProducerProps.put("batch.size", 16384);
        kakfaProducerProps.put("linger.ms", 1);
        kakfaProducerProps.put("buffer.memory", 33554432);
        kakfaProducerProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kakfaProducerProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        kafkaProducer = new KafkaProducer<>(kakfaProducerProps);

        WuPalmer wuPalmer = new WuPalmer(wordNet);

        MaxentTagger tagger = new MaxentTagger(TAGGER_PATH);
        DependencyParser parser = DependencyParser.loadFromModelFile(DependencyParser.DEFAULT_MODEL);

        Properties coreNLPProps = new Properties();
        coreNLPProps.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        StanfordCoreNLP coreNLPPipeline = new StanfordCoreNLP(coreNLPProps);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> typosToCorrections;
        try {
            wordToCategories = mapper.readValue(new File("result.json"),
                                                new TypeReference<Map<String, List<String>>>() {});
            typosToCorrections = mapper.readValue(new File("normalization.json"),
                                                  new TypeReference<Map<String, String>>() {});
            System.out.printf("\n\n\n\nLoaded Categories and Typos. Size %s and %s\n\n\n\n", wordToCategories.size(),
                                                                                            typosToCorrections.size());
        } catch (IOException ignored) {
            wordToCategories = new HashMap<>();
            typosToCorrections = new HashMap<>();
        }

        TextProcessor textProcessor = new TextProcessor(wuPalmer, typosToCorrections);
        aspectCategoryCreator = new AspectCategoryCreator(tagger, parser, textProcessor, coreNLPPipeline);
    }

    private static Constants getSingleton() {
        if (singleton == null)
            singleton = new Constants();
        return singleton;
    }

    public static String zooKeeperHostName() {
        return System.getenv("KAFKA_IP") + ":" + System.getenv("ZOO_KEEPER_PORT");
    }

    public static String consumerGroupId() {
        return "sparkNodes";
    }

    public static Map<String, List<String>> wordToCategories() {
        return getSingleton().wordToCategories;
    }

    public static Producer<String, String> kafkaProducer() {
        return getSingleton().kafkaProducer;
    }

    public static AspectCategoryCreator aspectCategory() {
        return getSingleton().aspectCategoryCreator;
    }
}
