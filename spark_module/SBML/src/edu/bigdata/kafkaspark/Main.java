package edu.bigdata.kafkaspark;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.TypedDependency;

import java.io.StringReader;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        String modelPath = DependencyParser.DEFAULT_MODEL;
        String taggerPath = "edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger";

        String text = "I can almost always tell when movies use fake dinosaurs.";
        String cleanedTweet = cleanTweet(text);

        MaxentTagger tagger = new MaxentTagger(taggerPath);
        DependencyParser parser = DependencyParser.loadFromModelFile(modelPath);

        DocumentPreprocessor tokenizer = new DocumentPreprocessor(new StringReader(cleanedTweet));
        for (List<HasWord> sentence : tokenizer) {
            List<TaggedWord> tagged = tagger.tagSentence(sentence);
            GrammaticalStructure gs = parser.predict(tagged);
            List<DependencyTriple> dependencyTriples = createDependencyTriples(gs.allTypedDependencies());
            Tuple<List<Tuple<String, String>>, List<String>> aspects = extractAspects(dependencyTriples);

            if (!aspects.x.isEmpty() || !aspects.y.isEmpty()) {
                Map<String, List<String>> aspectCategory = getAspectCategory(aspects);
            }


            System.out.println(tagged);
            System.out.println(dependencyTriples);
        }
    }

    private static Map<String, List<String>> getAspectCategory(Tuple<List<Tuple<String, String>>, List<String>> aspects) {
        Map<String, List<String>> aspectCategory = new HashMap<>();
        List<Tuple<String, String>> nounAspects = aspects.x;
        List<String> adjectiveAspects = aspects.y;

        for (String adjAspect : adjectiveAspects) {
            for (Tuple<String, String> nounAspect: nounAspects) {
                int aspectCategoryFlag = -100;
                String overallAspectCategory = "";
                if (nounAspect.isNotNull() && nounAspect.y.equals(adjAspect)) {
                    String aspect = nounAspect.x;
                    System.out.printf("Aspect, word %s; %s", aspect, nounAspect.y);
                }
            }
        }


        return aspectCategory;
    }

    private static List<DependencyTriple> createDependencyTriples(Collection<TypedDependency> typedDependencies) {
        List<DependencyTriple> dependencyTriples = new ArrayList<>();
        for (TypedDependency dependency : typedDependencies) {
            String relation = dependency.reln().getShortName();
            String headWord = dependency.gov().word();
            String headTag = dependency.gov().tag();
            String dependencyWord = dependency.dep().word();
            String dependencyTag = dependency.dep().tag();
            dependencyTriples.add(new DependencyTriple(headWord, headTag, relation, dependencyWord, dependencyTag));
        }
        return dependencyTriples;
    }

    private static String cleanTweet(String tweet) {
        return tweet.replaceAll("^https?://.*[\\r\\n]*", "")
                    .replace("!", ".")
                    .replace("?", ".")
                    .replaceAll("#(\\w+)", "")
                    .replaceAll("\\.\\.+", ".");
    }


    private static Tuple<List<Tuple<String, String>>, List<String>> extractAspects(List<DependencyTriple> dependencyTriples) {
        List<Tuple<String, String>> nounAspects = new ArrayList<>();
        List<String> adjectiveAspects = new ArrayList<>();

        for (DependencyTriple dependencyTriple: dependencyTriples) {
            Tuple<List<Tuple<String, String>>, Set<String>> nounAndAdjAspects = dependencyTriple.extractAspect();
            nounAspects.addAll(nounAndAdjAspects.x);
            adjectiveAspects.addAll(nounAndAdjAspects.y);
        }

        return new Tuple<>(nounAspects, adjectiveAspects);
    }

}
