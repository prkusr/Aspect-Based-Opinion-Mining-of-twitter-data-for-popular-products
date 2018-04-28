package edu.bigdata.kafkaspark.helper;

import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.util.WS4JConfiguration;

import java.util.List;

public class TextProcessor {

    private RelatednessCalculator relatednessCalculator;

    TextProcessor(RelatednessCalculator relatednessCalculator) {
        this.relatednessCalculator = relatednessCalculator;
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
        return tweet.replaceAll("^https?://.*[\\r\\n]*", "")
                .replace("!", ".")
                .replace("?", ".")
                .replaceAll("#(\\w+)", "")
                .replaceAll("\\.\\.+", ".");
    }
}
