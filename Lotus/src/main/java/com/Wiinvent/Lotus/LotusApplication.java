package com.Wiinvent.Lotus;

import com.Wiinvent.Lotus.core.config.LotusProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(LotusProperties.class)
public class LotusApplication {

	public static void main(String[] args) {
		SpringApplication.run(LotusApplication.class, args);
	}

}
