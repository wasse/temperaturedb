package se.okwa.temperaturedb.controller;


import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

import se.okwa.temperaturedb.domain.Weather;
import se.okwa.temperaturedb.domain.WeatherService;
import se.okwa.temperaturedb.repository.WeatherReadingRepository;

@RestController
@RequestMapping("/weather")
public class ApiController {
	
	@Autowired	//important for webflux??
	private final WeatherService service;
//	private final WeatherServiceRestTemplate service;

	// Database
	private WeatherReadingRepository readingRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(ApiController.class);
	
	public ApiController(WeatherService service/*WeatherServiceRestTemplate service*/) {
		this.service = service;
	}
	
	@GetMapping("/test")
	public String getA() {
		logger.info("Test!");
		return "Hello";
	}
	
	@GetMapping("/test2")
	public ResponseEntity<String> getTest2(@RequestParam("int") int someNumber) {
		return new ResponseEntity<>("The number is " + someNumber, HttpStatus.OK); 
	}
	
	@GetMapping("/test3")
	public Publisher<String> getTest3() {
		return Mono.just("Hej hej!");
	}
	
	@GetMapping("/now/")
	public Mono<Weather> getWeather(@RequestParam("country") String country, @RequestParam("city") String city) {
		logger.info("Country and city entered.");
//		readingRepository.save(this.service.getWeather(country, city));
		return this.service.getWeather(country, city);
	}

	@GetMapping("/at-moment/")
	public Mono<Weather> getWeatherByCity(@RequestParam("city") String city) {
		logger.info("City entered.");
		return this.service.getWeatherByCity(city);
	}
	
	
}
