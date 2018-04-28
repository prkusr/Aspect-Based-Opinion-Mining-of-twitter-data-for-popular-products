package edu.bigdata.kafkaspark.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedHashMap;

public class TweetJSON {
    private static final String TWEET_KEY = "text";
    private final JSONObject tweetJson;

    private TweetJSON(String tweetJsonString) {
        System.out.println(tweetJsonString);
        this.tweetJson = new JSONObject(tweetJsonString);
    }

    public String tweet() {
        return tweetJson.getString(TWEET_KEY);
    }

    private JSONArray location() {
        return tweetJson.getJSONObject("place")
                        .getJSONObject("bounding_box")
                        .getJSONArray("coordinates")
                        .getJSONArray(0)
                        .getJSONArray(0);
    }

    @SuppressWarnings("unchecked")
    public JSONObject opinionJSON(AspectCategories aspectCategories) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("tweet", tweet());
            jsonObject.put("location", location());

            JSONArray categoryToWordsArray = new JSONArray();
            for (AspectCategoryPojo aspectCategoryPojo : aspectCategories.values()) {
                LinkedHashMap map = new LinkedHashMap(2);
                map.put("category", aspectCategoryPojo.category());
                map.put("words", aspectCategoryPojo.describingWords());
                map.put("sentiment", aspectCategoryPojo.sentimentScore());
                categoryToWordsArray.put(map);
            }

            jsonObject.put("aspects", categoryToWordsArray);
            return jsonObject;
        } catch (Exception e) {
            return null;
        }
    }

    public static TweetJSON createTweetJSON(String tweetsJsonString) {
        try {
            return new TweetJSON(tweetsJsonString);
        }
        catch(Exception e) {
            return null;
        }
    }
}
