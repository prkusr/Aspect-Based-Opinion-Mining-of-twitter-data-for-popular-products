package edu.bigdata.kafkaspark.model;

import java.util.List;

class AspectCategoryPojo {
    private final String category;
    private final List<String> describingWords;
    private final List<Integer> sentimentScoresPerWord;

    AspectCategoryPojo(String category, List<String> describingWords, List<Integer> sentimentScoresPerWord) {
        this.category = category;
        this.describingWords = describingWords;
        this.sentimentScoresPerWord = sentimentScoresPerWord;
    }

    String category() {
        return category;
    }

    List<String> describingWords() {
        return describingWords;
    }

    float sentimentScore() {
        float avg = 0;
        for (Integer score : sentimentScoresPerWord) {
            avg += score;
        }
        return avg / sentimentScoresPerWord.size();
    }

    void addWord(String word, int sentimentScore) {
        describingWords.add(word);
        sentimentScoresPerWord.add(sentimentScore);
    }
}


