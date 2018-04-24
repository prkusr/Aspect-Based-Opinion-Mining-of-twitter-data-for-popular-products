package edu.bigData.sparkBusters;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class,WebMvcAutoConfiguration.class })
//@ComponentScan()
public class SparkBusterssApplication {

	public static void main(String[] args) {
		SpringApplication.run(SparkBusterssApplication.class, args);
	}
}
