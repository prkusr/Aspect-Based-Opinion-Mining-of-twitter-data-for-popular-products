package edu.bigdata.kafkaspark;

import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.impl.WuPalmer;

import java.util.*;

class Constants {
    private final  Map<String, List<String>> wordToCategories;
    private final WuPalmer wuPalmer;
    private static Constants singleton = null;

    private Constants() {
        ILexicalDatabase db = new NictWordNet();
        wuPalmer = new WuPalmer(db);

        wordToCategories = new HashMap<>();
        wordToCategories.put("word", Arrays.asList("cat1", "cat2"));

    }

    private static Constants getSingleton() {
        if (singleton == null)
            singleton = new Constants();
        return singleton;
    }

    static Map<String, List<String>> wordToCategories() {
        return getSingleton().wordToCategories;
    }

    static WuPalmer wuPalmer() {
        return getSingleton().wuPalmer;
    }
}
