package edu.bigdata.kafkaspark.helper;

import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.util.WS4JConfiguration;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;

import java.io.StringReader;
import java.util.List;
import java.util.Map;

public class TextProcessor {

    private RelatednessCalculator relatednessCalculator;
    private final Map<String, String> typosToCorrections;

    TextProcessor(RelatednessCalculator relatednessCalculator, Map<String, String> typosToCorrections) {
        this.relatednessCalculator = relatednessCalculator;
        this.typosToCorrections = typosToCorrections;
    }

    private double similarityScore(String aspect, String category) {
        WS4JConfiguration.getInstance().setMFS(true);
        return relatednessCalculator.calcRelatednessOfWords(category, aspect);
    }

    public int maxSimilarityScoreIndex(String aspect, List<String> categories) {
        double maxScore = 0;
        int maxScoreIndex = -1;

        for (int i = 0; i < categories.size(); i++) {
            double score = similarityScore(aspect, categories.get(i));
            if (score > maxScore) {
                maxScore = score;
                maxScoreIndex = i;
            }
        }
        return maxScoreIndex;
    }

    public String cleanTweet(String tweet) {
        String cleanedTweet = tweet.replaceAll("^https?://.*[\\r\\n]*", "")
                .replace("!", ".")
                .replace("?", ".")
                .replaceAll("#(\\w+)", "")
                .replaceAll("\\.\\.+", ".");
        Tokenizer<Word> tokenizer = PTBTokenizer.factory().getTokenizer(new StringReader(cleanedTweet));
        return tokenizer.tokenize().stream()
                                   .map(word -> typosToCorrections.getOrDefault(word.word(), word.word()))
                                   .reduce("", (a, b) -> a + " " + b);
    }
}
