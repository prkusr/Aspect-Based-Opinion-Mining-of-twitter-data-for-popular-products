package edu.bigdata.kafkaspark;

import java.util.*;

public class DependencyTriple {
    private static final String XCOMP = "xcomp";
    private final static String NOUN = "NN";
    private final static String ADJECTIVE = "JJ";
    private static final String AMOD = "amod";
    private static final String ADVMOD = "advmod";
    private static final String ADVERB = "RB";
    private static final String DOBJ = "dobj";
    private static final String NSUB = "nsub";
    private static final String ADVCL = "advcl";
    private static final String CONJUNCTION = "conj";

    DependencyTriple(String headWord, String headTag, String relation, String dependencyWord, String dependencyTag) {
        this.headWord = headWord;
        this.headTag = headTag;
        this.relation = relation;
        this.dependencyWord = dependencyWord;
        this.dependencyTag = dependencyTag;
    }


    private String headWord;
    private String headTag;
    private String relation;
    private String dependencyWord;
    private String dependencyTag;

    Tuple<List<Tuple<String, String>>, Set<String>> extractAspect() {
        List<Tuple<String, String>> nounAspect = new ArrayList<>();
        Set<String> adjectiveAspect = new HashSet<>();

        if (AMOD.equals(relation) || ADVMOD.equals(relation)) {
            if (ADJECTIVE.equals(dependencyTag) && !ADVERB.equals(headTag)) {
                nounAspect.add(new Tuple<>(headWord, dependencyWord));
                adjectiveAspect.add(dependencyWord);
            } else {
                nounAspect.add(new Tuple<>(dependencyWord, headWord));
                adjectiveAspect.add(headWord);
            }
        }

        if (DOBJ.equals(relation) || NSUB.equals(relation)) {
            if (NOUN.equals(headTag))
                nounAspect.add(new Tuple<>(headWord, dependencyWord));
            else if (ADJECTIVE.equals(headTag))
                adjectiveAspect.add(headWord);

            if (NOUN.equals(dependencyTag))
                nounAspect.add(new Tuple<>(dependencyTag, headWord));
            else if (ADJECTIVE.equals(dependencyTag))
                adjectiveAspect.add(dependencyWord);
        }

        if (ADVCL.equals(relation))
            adjectiveAspect.add(dependencyWord);

        if (XCOMP.equals(relation)) {
            adjectiveAspect.add(headWord);
            adjectiveAspect.add(dependencyWord);
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
