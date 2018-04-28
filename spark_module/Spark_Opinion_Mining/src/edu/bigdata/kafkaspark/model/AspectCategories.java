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
            AspectCategoryPojo newAspectCat = new AspectCategoryPojo(category, Collections.singletonList(word),
                                                                    Collections.singletonList(sentimentScore));
            categoryToAspectCategory.put(category, newAspectCat);
        }
    }

    public boolean isEmpty() {
        return categoryToAspectCategory.isEmpty();
    }

    Collection<AspectCategoryPojo> values() {
        return categoryToAspectCategory.values();
    }
}
