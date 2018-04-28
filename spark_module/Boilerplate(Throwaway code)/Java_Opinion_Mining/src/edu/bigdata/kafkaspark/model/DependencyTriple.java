package edu.bigdata.kafkaspark.model;

import java.util.*;

public class DependencyTriple {
    private static final String XCOMP = "xcomp";
    private final static String NOUN = "NN";
    private final static String ADJECTIVE = "JJ";
    private static final String AMOD = "amod";
    private static final String ADVMOD = "advmod";
    private static final String ADVERB = "RB";
    private static final String DOBJ = "dobj";
    private static final String NSUB = "nsubj";
    private static final String ADVCL = "advcl";
    private static final String CONJUNCTION = "conj";
    private static final String VERB_PRESENT = "VBP";
    private static final String NMOD = "nmod";
    private static final String VERB = "VB";

    private String headWord;
    private String headTag;
    private String relation;
    private String dependencyWord;
    private String dependencyTag;

    DependencyTriple(String headWord, String headTag, String relation, String dependencyWord, String dependencyTag) {
        this.headWord = headWord;
        this.headTag = headTag;
        this.relation = relation;
        this.dependencyWord = dependencyWord;
        this.dependencyTag = dependencyTag;
    }

    Tuple<List<Tuple<String, String>>, Set<String>> extractAspect() {
        List<Tuple<String, String>> nounAspect = new ArrayList<>();
        Set<String> adjectiveAspect = new HashSet<>();

        if (AMOD.equals(relation) || ADVMOD.equals(relation)) {
            if (ADVERB.equals(dependencyTag) || ADVERB.equals(headTag)) {
                adjectiveAspect.add(headWord);
            } else {
                nounAspect.add(new Tuple<>(dependencyWord, headWord));
                adjectiveAspect.add(headWord);
            }
        }

        else if (NMOD.equals(relation)) {
            if (NOUN.equals(dependencyTag) && NOUN.equals(headTag)) {
                nounAspect.add(new Tuple<>(headWord, dependencyWord));
            }
            if (VERB.equals(headTag) && NOUN.equals(dependencyTag)) {
                adjectiveAspect.add(headWord);
                nounAspect.add(new Tuple<>(dependencyWord, headWord));
            }
        }

        else if (NSUB.equals(relation)) {
            if (NOUN.equals(dependencyTag) && ADJECTIVE.equals(headTag)) {
                nounAspect.add(new Tuple<>(dependencyWord, headWord));
                adjectiveAspect.add(headWord);
            }
            else if (ADJECTIVE.equals(dependencyTag) && NOUN.equals(headTag)) {
                nounAspect.add(new Tuple<>(headWord, dependencyWord));
                adjectiveAspect.add(dependencyWord);
            }
        }

        else if (DOBJ.equals(relation) && VERB_PRESENT.equals(headTag)) {
            adjectiveAspect.add(headWord);
            nounAspect.add(new Tuple<>(dependencyWord, headWord));
        }

        else if (ADVCL.equals(relation))
            adjectiveAspect.add(dependencyWord);

        else if (XCOMP.equals(relation)) {
            adjectiveAspect.add(headWord);
            adjectiveAspect.add(dependencyWord);
            nounAspect.add(new Tuple<>(dependencyWord, headWord));
            nounAspect.add(new Tuple<>(headWord, dependencyWord));
        }

        return new Tuple<>(nounAspect, adjectiveAspect);
    }

    void appendAdjAspectForConjunction(Set<String> currentAdjectiveAspects) {
        if (CONJUNCTION.equals(relation) && ADJECTIVE.equals(headTag) && currentAdjectiveAspects.contains(headWord)) {
            currentAdjectiveAspects.add(dependencyWord);
        }
    }

    @Override
    public String toString() {
        return String.format("((%s, %s), %s, (%s, %s))", headWord, headTag, relation, dependencyWord, dependencyTag);
    }
}
