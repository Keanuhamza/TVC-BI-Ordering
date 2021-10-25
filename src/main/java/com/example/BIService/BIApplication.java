package com.example.BIService;

import com.example.BIService.models.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class BIApplication {

	public static void main(String[] args) {
		SpringApplication.run(BIApplication.class, args);
	}

	private static final Logger log = LoggerFactory.getLogger(BIApplication.class);


	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	public CommandLineRunner run(RestTemplate restTemplate, StreamBridge streamBridge) throws Exception {
		return args -> {
			Long i = 0L;
			try {
				while (!Thread.currentThread().isInterrupted()){
	
					//The binder name "appliance-outbound" is defined in the application.yml.
					streamBridge.send("order-outbound", new cOrder(i++, i++, "Hammer", 1));
					Thread.sleep(1200);
				}
			}
			catch(InterruptedException ignored){}
		};
	}

}
