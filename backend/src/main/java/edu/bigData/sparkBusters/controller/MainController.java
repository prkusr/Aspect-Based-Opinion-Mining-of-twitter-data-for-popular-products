package edu.bigData.sparkBusters.controller;

import edu.bigData.sparkBusters.model.Tweet;
import edu.bigData.sparkBusters.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MainController {
    private final KafkaService kafkaService;

    @Autowired
    public MainController(KafkaService kafkaService) {
        this.kafkaService = kafkaService;
    }

    @GetMapping("/opinion")
    @Cacheable(value = "resultCache", key = "#searchString.toLowerCase()",
               unless = "#result == null or #result.size() == 0")
    public @ResponseBody
    List<Tweet> getPositions(@RequestParam(name = "product", defaultValue = "") String searchString,
                             @RequestParam(name = "location", required = false) String location) {
        if (searchString.isEmpty())
            return new ArrayList<>();

        kafkaService.producer(searchString, location);
        return kafkaService.consume(searchString);
    }
}
