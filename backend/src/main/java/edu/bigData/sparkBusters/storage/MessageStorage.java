package edu.bigData.sparkBusters.storage;

import edu.bigData.sparkBusters.model.Tweet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class MessageStorage {
    private static final int ITERATION_THRESHOLD = Integer.valueOf(System.getenv("ITERATION_THRESHOLD"));
    private static final int MIN_POLL_IN_MS = 200;
    private static final int MAX_POLL_IN_MS = 1500;
    private static final int POLL_THRESHOLD_IN_MS = 60000;

    private Map<String, ConsumedTweets> searchStrToOpinions = new HashMap<>();
    private final Object storageLock = new Object();


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

    private List<Tweet> getOpinionsAndDeleteFromStorage(String searchString, boolean withoutCompleteConsumption) {
        synchronized (storageLock) {
            if (withoutCompleteConsumption) {
                return searchStrToOpinions.remove(searchString).tweets();
            }
            if (isConsumedCompletely(searchString)) {
                return searchStrToOpinions.remove(searchString).tweets();
            }
        }
        return null;
    }

    public List<Tweet> pollFor(String searchString, int pollInMS) {
        pollInMS = Math.min(Math.max(pollInMS, MIN_POLL_IN_MS), MAX_POLL_IN_MS);
        int max_iterations = POLL_THRESHOLD_IN_MS / pollInMS;
        int noOfIterationWithoutMessages = 0;
        try {
            List<Tweet> consumedOpinions = null;
            do {
                int prevMessageSize = currentMessageSize(searchString);
                Thread.sleep(pollInMS);
                if (--max_iterations == 0) {
                    break;
                }
                consumedOpinions = getOpinionsAndDeleteFromStorage(searchString, false);
                int currentMessageSize = currentMessageSize(searchString);
                if (currentMessageSize != 0 && prevMessageSize == currentMessageSize) {
                    noOfIterationWithoutMessages++;
                    if (noOfIterationWithoutMessages > ITERATION_THRESHOLD)
                        return getOpinionsAndDeleteFromStorage(searchString, true);
                }
                else
                    noOfIterationWithoutMessages = 0;
            } while (consumedOpinions == null);
            return consumedOpinions != null ? consumedOpinions : new ArrayList<>();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    private int currentMessageSize(String searchString) {
        ConsumedTweets consumedTweets = searchStrToOpinions.get(searchString);
        if (consumedTweets != null)
            return consumedTweets.size();
        return 0;
    }
}
