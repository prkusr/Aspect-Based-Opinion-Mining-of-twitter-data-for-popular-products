package edu.bigData.sparkBusters.controllers;

import edu.bigData.sparkBusters.service.KafkaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MainController {

    @GetMapping("/ping")
    public @ResponseBody
    List<String> getPositions(@RequestParam(name = "search", defaultValue = "") String searchString) {

        if (searchString.isEmpty())
            return null;

        KafkaService kafkaService = new KafkaService();
        kafkaService.producer(searchString);
        return kafkaService.consume(searchString);
    }
}
