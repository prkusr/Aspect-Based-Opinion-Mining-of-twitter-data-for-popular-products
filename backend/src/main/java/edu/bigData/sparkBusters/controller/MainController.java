package edu.bigData.sparkBusters.controller;

import edu.bigData.sparkBusters.service.KafkaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MainController {
    private final KafkaServiceImpl kafkaService;

    @Autowired
    public MainController(KafkaServiceImpl kafkaService) {
        this.kafkaService = kafkaService;
    }

    @GetMapping("/opinion")
    public @ResponseBody
    List<String> getPositions(@RequestParam(name = "product", defaultValue = "") String searchString) {

        if (searchString.isEmpty())
            return null;

        kafkaService.producer(searchString);
        return kafkaService.consume(searchString);
    }
}
