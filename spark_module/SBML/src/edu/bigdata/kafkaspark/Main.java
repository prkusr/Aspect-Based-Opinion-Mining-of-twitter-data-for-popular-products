package edu.bigdata.kafkaspark;

import edu.bigdata.kafkaspark.helper.Constants;
import edu.bigdata.kafkaspark.helper.TextProcessor;
import edu.bigdata.kafkaspark.manager.AspectCategory;
import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        String modelPath = DependencyParser.DEFAULT_MODEL;
        String taggerPath = "edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger";
        TextProcessor textProcessor = new TextProcessor(Constants.wuPalmer());
        MaxentTagger tagger = new MaxentTagger(taggerPath);
        DependencyParser parser = DependencyParser.loadFromModelFile(modelPath);

        String text = "I can almost always tell when movies use fake dinosaurs.";
        String cleanedTweet = textProcessor.cleanTweet(text);
        AspectCategory aspectCategory = new AspectCategory(tagger, parser, textProcessor);

        Map<String, List<String>> aspectCategoryToWords = aspectCategory.aspectCategoryToWords(cleanedTweet);
        System.out.print(aspectCategoryToWords);
    }
}
