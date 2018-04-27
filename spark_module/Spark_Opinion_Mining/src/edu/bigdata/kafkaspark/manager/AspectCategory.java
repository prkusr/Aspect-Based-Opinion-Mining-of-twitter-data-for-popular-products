package edu.bigdata.kafkaspark.manager;

import edu.bigdata.kafkaspark.helper.Constants;
import edu.bigdata.kafkaspark.helper.TextProcessor;
import edu.bigdata.kafkaspark.model.DependencyTriples;
import edu.bigdata.kafkaspark.model.Tuple;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.GrammaticalStructure;

import java.io.Serializable;
import java.io.StringReader;
import java.util.*;

public class AspectCategory {

    private MaxentTagger tagger;
    private DependencyParser parser;
    private TextProcessor textProcessor;

    public AspectCategory(MaxentTagger tagger, DependencyParser parser, TextProcessor textProcessor) {
        this.textProcessor = textProcessor;
        this.tagger = tagger;
        this.parser = parser;
    }

    private Map<String, List<String>> getAspectCategory(Tuple<List<Tuple<String, String>>, Set<String>> aspects) {
        Map<String, List<String>> categoryToDescribingWords = new HashMap<>();

        Map<String, List<String>> wordToCategories = Constants.wordToCategories();

        Set<String> adjectiveAspects = aspects.y;
        List<Tuple<String, String>> filteredNounAspects = new ArrayList<>();
        for (Tuple<String, String> nounAspect : aspects.x) {
            String describingWord = nounAspect.y;
            if (adjectiveAspects.contains(describingWord))
                filteredNounAspects.add(nounAspect);
        }

        for (Tuple<String, String> nounAspect : filteredNounAspects) {
            String aspect = nounAspect.x;
            String describingWord = nounAspect.y;

            List<String> categories = wordToCategories.get(describingWord);
            if (categories != null && !categories.isEmpty()) {
                int maxSimilarityScoreIndex = textProcessor.maxSimilarityScoreIndex(aspect, categories);
                if (maxSimilarityScoreIndex != -1) {
                    String aspectCategory = wordToCategories.get(describingWord).get(maxSimilarityScoreIndex);
                    List<String> words = categoryToDescribingWords.get(aspectCategory);
                    if (words != null) {
                        words.add(describingWord);
                        categoryToDescribingWords.put(aspectCategory, words);
                    } else {
                        categoryToDescribingWords.put(aspectCategory, Collections.singletonList(describingWord));
                    }
                }
            }
        }
        return categoryToDescribingWords;
    }

    public Map<String, List<String>> aspectCategoryToWords(String tweet) {
        String cleanedTweet = textProcessor.cleanTweet(tweet);
        DocumentPreprocessor tokenizer = new DocumentPreprocessor(new StringReader(cleanedTweet));
        List<TaggedWord> tagged = tagger.tagSentence(tokenizer.iterator().next());
        GrammaticalStructure gs = parser.predict(tagged);
        DependencyTriples dependencyTriples = DependencyTriples.createDependencyTriples(gs.allTypedDependencies());

        Tuple<List<Tuple<String, String>>, Set<String>> aspects = dependencyTriples.extractAspects();

        if (!aspects.x.isEmpty() || !aspects.y.isEmpty()) {
            return getAspectCategory(aspects);
        }
        return new HashMap<>();
    }
}
