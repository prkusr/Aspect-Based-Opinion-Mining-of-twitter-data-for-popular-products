package edu.bigdata.kafkaspark.model;

import edu.stanford.nlp.trees.TypedDependency;

import java.util.*;

public class DependencyTriples {

    private List<DependencyTriple> dependencyTriples;

    private DependencyTriples(List<DependencyTriple> dependencyTriples) {
        this.dependencyTriples = dependencyTriples;
    }

    private Set<String> extractAdjAspectForConjunction(List<DependencyTriple> dependencyTriples, Set<String> currentAdjectiveAspects) {
        for (DependencyTriple triple : dependencyTriples) {
            triple.appendAdjAspectForConjunction(currentAdjectiveAspects);
        }
        return currentAdjectiveAspects;
    }

    public Tuple<List<Tuple<String, String>>, Set<String>> extractAspects() {
        List<Tuple<String, String>> nounAspects = new ArrayList<>();
        Set<String> adjectiveAspects = new HashSet<>();

        for (DependencyTriple dependencyTriple : dependencyTriples) {
            Tuple<List<Tuple<String, String>>, Set<String>> nounAndAdjAspects = dependencyTriple.extractAspect();
            nounAspects.addAll(nounAndAdjAspects.x);
            adjectiveAspects.addAll(nounAndAdjAspects.y);
        }

        adjectiveAspects = extractAdjAspectForConjunction(dependencyTriples, adjectiveAspects);

        return new Tuple<>(nounAspects, adjectiveAspects);
    }

    public static DependencyTriples createDependencyTriples(Collection<TypedDependency> typedDependencies) {
        List<DependencyTriple> dependencyTriples = new ArrayList<>();
        for (TypedDependency dependency : typedDependencies) {
            String relation = dependency.reln().getShortName();
            String headWord = dependency.gov().word();
            String headTag = dependency.gov().tag();
            String dependencyWord = dependency.dep().word();
            String dependencyTag = dependency.dep().tag();
            dependencyTriples.add(new DependencyTriple(headWord, headTag, relation, dependencyWord, dependencyTag));
        }
        return new DependencyTriples(dependencyTriples);
    }
}
