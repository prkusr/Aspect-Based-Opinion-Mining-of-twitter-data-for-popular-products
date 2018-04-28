package edu.bigdata.kafkaspark.manager;

import edu.bigdata.kafkaspark.helper.Constants;
import edu.bigdata.kafkaspark.helper.TextProcessor;
import edu.bigdata.kafkaspark.model.AspectCategories;
import edu.bigdata.kafkaspark.model.DependencyTriples;
import edu.bigdata.kafkaspark.model.Tuple;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

import java.io.StringReader;
import java.util.*;

public class AspectCategoryCreator {

    private MaxentTagger tagger;
    private DependencyParser parser;
    private TextProcessor textProcessor;
    private StanfordCoreNLP pipeline;

    public AspectCategoryCreator(MaxentTagger tagger, DependencyParser parser, TextProcessor textProcessor, StanfordCoreNLP pipeline) {
        this.textProcessor = textProcessor;
        this.tagger = tagger;
        this.parser = parser;
        this.pipeline = pipeline;
    }


    private int getAspectSentiment(String word) {
        int totalSentiment = 0;
        if (!word.isEmpty()) {
            Annotation annotation = pipeline.process(word);
            for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                int score = RNNCoreAnnotations.getPredictedClass(tree);
                totalSentiment = totalSentiment + (score - 2);
            }
        }
        return totalSentiment;
    }


    private AspectCategories getAspectCategories(Tuple<List<Tuple<String, String>>, Set<String>> aspects) {
        AspectCategories aspectCategories = new AspectCategories();

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
                    String category = wordToCategories.get(describingWord).get(maxSimilarityScoreIndex);
                    aspectCategories.addWordToCategory(category, describingWord, getAspectSentiment(describingWord));
                }
            }
        }
        return aspectCategories;
    }

    public AspectCategories aspectCategoryToWords(String tweet) {
        String cleanedTweet = textProcessor.cleanTweet(tweet);
        DocumentPreprocessor tokenizer = new DocumentPreprocessor(new StringReader(cleanedTweet));
        TokenizerFactory<Word> factory = PTBTokenizer.factory();
        factory.setOptions("untokenizable=noneDelete");
        tokenizer.setTokenizerFactory(factory);
        List<TaggedWord> tagged = tagger.tagSentence(tokenizer.iterator().next());
        GrammaticalStructure gs = parser.predict(tagged);
        DependencyTriples dependencyTriples = DependencyTriples.createDependencyTriples(gs.allTypedDependencies());

        Tuple<List<Tuple<String, String>>, Set<String>> aspects = dependencyTriples.extractAspects();

        if (!aspects.x.isEmpty() || !aspects.y.isEmpty()) {
            return getAspectCategories(aspects);
        }
        return new AspectCategories(new ArrayList<>());
    }
}
