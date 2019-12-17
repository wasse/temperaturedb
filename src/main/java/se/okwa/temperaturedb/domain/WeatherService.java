package se.okwa.temperaturedb.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


import reactor.core.publisher.Mono;
import se.okwa.temperaturedb.TemperaturedbApplicationProperties;

@Service
public class WeatherService implements WeatherServiceInterface {
	
	private static final String WEATHER_URL = 
			/*"http://api.openweathermap.org/data/2.5*/"/weather?q={city},{country}&APPID={key}";
	private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);
	private final String API_KEY;
	
	private final WebClient webCli;
	
	public WeatherService (TemperaturedbApplicationProperties properties) {
		this.API_KEY = properties.getApi().getApiKey();
		
		this.webCli = WebClient.create("http://api.openweathermap.org/data/2.5");
	}
	
	@Override
	public Mono<Weather> getWeather(String country, String city) {
		// TODO Auto-generated method stub
		logger.info("Requesting current weather for {}/{}", country, city);
		
		Mono<Weather> weather = this.webCli.get().uri(uriBuilder -> uriBuilder
				.path("/weather")
				.queryParam("q", String.join(",", city, country)).queryParam("APPID", this.API_KEY)
				.build(city, country, this.API_KEY))
				.retrieve().bodyToMono(Weather.class);
		
		return weather;
	}

	@Override
	public Mono<Weather> getWeatherByCity(String city) {
		// TODO Auto-generated method stub
		logger.info("Requesting current weather for {}", city);
		
		Mono<Weather> weather = this.webCli.get().uri(uriBuilder -> uriBuilder
				.path("/weather")
				.queryParam("q", city).queryParam("APPID", this.API_KEY)
				.build(city, this.API_KEY))
				.retrieve().bodyToMono(Weather.class);

		return weather;
	}
	
}
