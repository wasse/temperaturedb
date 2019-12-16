package se.okwa.temperaturedb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableConfigurationProperties(TemperaturedbApplicationProperties.class)
public class TemperaturedbApplication {

	public static void main(String[] args) {
		SpringApplication.run(TemperaturedbApplication.class, args);
	}

}
