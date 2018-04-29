package edu.bigData.sparkBusters.service;

import java.util.List;

public interface KafkaService {
    void producer(String searchString);
    List<String> consume(String searchString);
}
