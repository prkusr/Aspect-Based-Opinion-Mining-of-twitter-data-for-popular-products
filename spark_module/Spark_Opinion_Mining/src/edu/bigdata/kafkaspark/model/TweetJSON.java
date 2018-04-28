package edu.bigdata.kafkaspark.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    public JSONObject opinionJSON(Map<String, List<String>> categoryToWords) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("tweet", tweet());
            jsonObject.put("location", location());

            JSONArray categoryToWordsArray = new JSONArray();
            for (HashMap.Entry<String, List<String>> cat : categoryToWords.entrySet()) {
                LinkedHashMap map = new LinkedHashMap(2);
                map.put("category", cat.getKey());
                map.put("words", cat.getValue());
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
