package edu.bigData.sparkBusters.storage;

import edu.bigData.sparkBusters.model.Tweet;

import java.util.List;

class ConsumedTweets {
    private List<Tweet> tweets;
    private boolean consumptionComplete = false;

    ConsumedTweets(List<Tweet> tweets) {
        this.tweets = tweets;
    }

    void add(Tweet tweet) {
        int tweetCount = tweet.totalTweets;
        tweets.add(tweet);
        System.out.println("Size " + tweets.size());
        consumptionComplete = tweets.size() >= tweetCount;
    }

    boolean isConsumptionComplete() {
        return consumptionComplete;
    }

    int size() {
        return tweets.size();
    }

    List<Tweet> tweets() {
        return tweets;
    }
}
