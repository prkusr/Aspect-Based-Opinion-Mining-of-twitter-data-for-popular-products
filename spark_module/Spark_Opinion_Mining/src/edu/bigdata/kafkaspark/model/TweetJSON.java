package edu.bigdata.kafkaspark.model;

import edu.bigdata.kafkaspark.helper.Constants;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedHashMap;

public class TweetJSON {
    private final JSONObject tweetJson;

    private TweetJSON(String tweetJsonString) {
        this.tweetJson = new JSONObject(tweetJsonString);
    }

    private static void printException(Exception e) {
        System.out.println("\n\n");
        System.out.println(e.getMessage());
        System.out.println("\n\n");
    }

    private JSONArray location() {
        return tweetJson.getJSONObject(Constants.JSONKeys.PLACE)
                .getJSONObject(Constants.JSONKeys.BOUNDING_BOX)
                .getJSONArray(Constants.JSONKeys.COORDINATES)
                .getJSONArray(0)
                .getJSONArray(0);
    }

    public String tweet() {
        try {
            return tweetJson.getString(Constants.JSONKeys.TWEET_KEY);
        } catch (Exception e) {
            printException(e);
            return null;
        }
    }

    private String searchString() {
        return tweetJson.getString(Constants.JSONKeys.SEARCH_STRING_KEY);
    }

    private int totalTweets() {
        return tweetJson.getInt(Constants.JSONKeys.TOTAL_TWEETS_KEY);
    }

    private long tweetId() {
        return tweetJson.getLong(Constants.JSONKeys.TWEET_ID_KEY);
    }

    @SuppressWarnings("unchecked")
    public JSONObject opinionJSON(AspectCategories aspectCategories) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.JSONKeys.TWEET_ID_KEY, tweetId());
            jsonObject.put(Constants.JSONKeys.TWEET_KEY, tweet());
            jsonObject.put(Constants.JSONKeys.SEARCH_STRING_KEY, searchString());
            jsonObject.put(Constants.JSONKeys.TOTAL_TWEETS_KEY, totalTweets());
            jsonObject.put(Constants.JSONKeys.LOCATION_KEY, location());

            JSONArray categoryToWordsArray = new JSONArray();
            if (aspectCategories.isEmpty()) {
                jsonObject.put(Constants.JSONKeys.IS_OPINION, false);
            } else {
                jsonObject.put(Constants.JSONKeys.IS_OPINION, true);
                for (AspectCategoryPojo aspectCategoryPojo : aspectCategories.values()) {
                    LinkedHashMap map = new LinkedHashMap(2);
                    map.put(Constants.JSONKeys.CATEGORY, aspectCategoryPojo.category());
                    map.put(Constants.JSONKeys.WORDS, aspectCategoryPojo.describingWords());
                    map.put(Constants.JSONKeys.SENTIMENT, aspectCategoryPojo.sentimentScore());
                    categoryToWordsArray.put(map);
                }
            }
            jsonObject.put(Constants.JSONKeys.ASPECT, categoryToWordsArray);
            return jsonObject;
        } catch (Exception e) {
            printException(e);
            return null;
        }
    }

    public static TweetJSON createTweetJSON(String tweetsJsonString) {
        try {
            return new TweetJSON(tweetsJsonString);
        } catch (Exception e) {
            printException(e);
            return null;
        }
    }
}
