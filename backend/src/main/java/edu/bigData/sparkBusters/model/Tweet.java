package edu.bigData.sparkBusters.model;

import java.util.List;

public class Tweet {
    public String searchString;
    public boolean isOpinion;
    public int totalTweets;
    public List<Aspect> aspects;
    public List<Double> location;
    public String text;
}

