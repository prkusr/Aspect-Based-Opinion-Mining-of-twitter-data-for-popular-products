package edu.bigdata.kafkaspark.model;

import java.util.*;

public class AspectCategories {
    private final Map<String, AspectCategoryPojo> categoryToAspectCategory;

    public AspectCategories(){
        this(new ArrayList<>());
    }

    public AspectCategories(List<AspectCategoryPojo> aspectCategories) {
        categoryToAspectCategory = new HashMap<>();
        for(AspectCategoryPojo aspectCategory : aspectCategories) {
            categoryToAspectCategory.put(aspectCategory.category(), aspectCategory);
        }
    }

    public void addWordToCategory(String category, String word, int sentimentScore) {
        AspectCategoryPojo aspectCategory = categoryToAspectCategory.get(category);

        if (aspectCategory != null)
            aspectCategory.addWord(word, sentimentScore);
        else {
            List<String> describingWords = new LinkedList<>();
            List<Integer> sentimentScores = new LinkedList<>();
            describingWords.add(word);
            sentimentScores.add(sentimentScore);
            AspectCategoryPojo newAspectCat = new AspectCategoryPojo(category, describingWords, sentimentScores);
            categoryToAspectCategory.put(category, newAspectCat);
        }
    }

    boolean isEmpty() {
        return categoryToAspectCategory.isEmpty();
    }

    Collection<AspectCategoryPojo> values() {
        return categoryToAspectCategory.values();
    }
}
