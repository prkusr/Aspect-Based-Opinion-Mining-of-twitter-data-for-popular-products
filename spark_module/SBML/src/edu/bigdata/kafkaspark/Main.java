package edu.bigdata.kafkaspark;

import edu.cmu.lti.ws4j.util.WS4JConfiguration;
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

    private static Map<String, List<String>> getAspectCategory(Tuple<List<Tuple<String, String>>, Set<String>> aspects) {
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
                int maxSimilarityScoreIndex = maxSimilarityScoreIndex(aspect, categories);
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

    private static int maxSimilarityScoreIndex(String aspect, List<String> categories) {
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

    private static double similarityScore(String aspect, String category) {
        WS4JConfiguration.getInstance().setMFS(true);
        return Constants.wuPalmer().calcRelatednessOfWords(category, aspect);
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


    private static Tuple<List<Tuple<String, String>>, Set<String>> extractAspects(List<DependencyTriple> dependencyTriples) {
        List<Tuple<String, String>> nounAspects = new ArrayList<>();
        Set<String> adjectiveAspects = new HashSet<>();

        for (DependencyTriple dependencyTriple : dependencyTriples) {
            Tuple<List<Tuple<String, String>>, Set<String>> nounAndAdjAspects = dependencyTriple.extractAspect();
            nounAspects.addAll(nounAndAdjAspects.x);
            adjectiveAspects.addAll(nounAndAdjAspects.y);
        }

        return new Tuple<>(nounAspects, adjectiveAspects);
    }

    private static Set<String> extractAdjAspectForConjunction(List<DependencyTriple> dependencyTriples, Set<String> currentAdjectiveAspects) {
        for (DependencyTriple triple : dependencyTriples) {
            triple.appendAdjAspectForConjunction(currentAdjectiveAspects);
        }
        return currentAdjectiveAspects;
    }

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

            Tuple<List<Tuple<String, String>>, Set<String>> aspects = extractAspects(dependencyTriples);
            Set<String> adjectiveAspects = extractAdjAspectForConjunction(dependencyTriples, aspects.y);

            aspects = new Tuple<>(aspects.x, adjectiveAspects);

            if (!aspects.x.isEmpty() || !aspects.y.isEmpty()) {
                Map<String, List<String>> aspectCategory = getAspectCategory(aspects);
                System.out.println(aspectCategory);
            }
        }
    }
}
