package edu.bigData.sparkBusters.storage;

import edu.bigData.sparkBusters.model.Tweet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class MessageStorage {
    private Map<String, ConsumedTweets> searchStrToOpinions = new HashMap<>();
    private final Object storageLock = new Object();

    private static final int MIN_POLL_IN_MS = 500;
    private static final int MAX_POLL_IN_MS = 1500;
    private static final int POLL_THRESHOLD_IN_MS = 60000;

    public void add(Tweet tweet) {
        synchronized (storageLock) {
            String searchString = tweet.searchString;
            if (!searchStrToOpinions.containsKey(searchString))
                searchStrToOpinions.put(searchString, new ConsumedTweets(new ArrayList<>()));
            searchStrToOpinions.get(searchString).add(tweet);
        }
    }

    private boolean isConsumedCompletely(String searchString) {
        ConsumedTweets tweetsOfProduct = searchStrToOpinions.get(searchString);
        return tweetsOfProduct != null && tweetsOfProduct.isConsumptionComplete();
    }

    private List<Tweet> getOpinionsAndDeleteFromStorage(String searchString) {
        synchronized (storageLock) {
            if (isConsumedCompletely(searchString)) {
                return searchStrToOpinions.remove(searchString).tweets();
            }
        }
        return null;
    }

    public List<Tweet> pollFor(String searchString, int pollInMS) {
        pollInMS = Math.min(Math.min(pollInMS, MIN_POLL_IN_MS), MAX_POLL_IN_MS);
        int max_iterations = POLL_THRESHOLD_IN_MS / pollInMS;
        try {
            List<Tweet> consumedOpinions = null;
            do {
                Thread.sleep(pollInMS);
                if (--max_iterations == 0) {
                    break;
                }
                consumedOpinions = getOpinionsAndDeleteFromStorage(searchString);
            } while (consumedOpinions == null);
            return consumedOpinions != null ? consumedOpinions : new ArrayList<>();
        } catch (Exception e) {
            System.out.println(e.toString());
            return new ArrayList<>();
        }
    }
}
