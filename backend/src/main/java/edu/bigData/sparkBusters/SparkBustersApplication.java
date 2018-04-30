package edu.bigData.sparkBusters;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("edu.bigData.sparkBusters")
public class SparkBustersApplication {

	public static void main(String[] args) {
		SpringApplication.run(SparkBustersApplication.class, args);
	}
}
