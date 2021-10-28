package com.example.BIService;

import com.example.BIService.models.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.retrytopic.DestinationTopic.Properties;
import org.springframework.web.client.RestTemplate;
import java.util.function.Consumer;

@SpringBootApplication
public class BIApplication {

	public static void main(String[] args) {
		SpringApplication.run(BIApplication.class, args);
	}

	private static final Logger log = LoggerFactory.getLogger(BIApplication.class);
	
	@Bean
	public Consumer<cProductTotal> consume() {
		return input -> log.info(input.toString());
	}


	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
/*
	@Bean
	public CommandLineRunner run(StreamBridge streamBridge) throws Exception {
		return args -> {
			Long i = 1L;
			try {
				while (i < 20L){
					cOrder order = new cOrder(i, i, "Hammer", 2);
					log.info(order.toString());
					//The binder name "appliance-outbound" is defined in the application.yml.
					//streamBridge.send("appliance-outbound", order.getProductName(), order);
					streamBridge.send("appliance-outbound", order);
					
					i = i + 1L;
				}
			}
			catch(Exception ignored){}
			return;
		}; 
	}*/

}
