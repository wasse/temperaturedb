package se.okwa.temperaturedb.controller;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

import ch.qos.logback.core.Context;
import reactor.core.publisher.Mono;

import se.okwa.temperaturedb.domain.Weather;
import se.okwa.temperaturedb.domain.WeatherService;
import se.okwa.temperaturedb.model.WeatherReading;
import se.okwa.temperaturedb.repository.WeatherReadingRepository;

@RestController
@RequestMapping("/weather")
public class ApiController {
	
	@Autowired	//important for webflux??
	private final WeatherService service;
//	private final WeatherServiceRestTemplate service;

	// Database
	@Autowired
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
		
		Mono<Weather> resp = this.service.getWeather(country, city);
		
		logger.info(getValue(resp).getCity() + " AT " + getValue(resp).getTemperature());
		
//		WeatherReading reading = new WeatherReading(getValue(resp).getTemperature(), getValue(resp).getCity(), getValue(resp).getDate());
//		readingRepository.save(reading);
		
		return resp;
	}
	
	@Scheduled(fixedRate = 1200000)
	public void getWeatherScheduled() {
		logger.info("Scheduled task performed");
		
		Mono<Weather> resp = this.service.getWeather("Sweden", "Gothenburg");
		
		logger.info(getValue(resp).getCity() + " AT " + getValue(resp).getTemperature());
		
		WeatherReading reading = new WeatherReading(getValue(resp).getTemperature(), getValue(resp).getCity(), getValue(resp).getDate());
		readingRepository.save(reading);
		
	}
	
	Weather getValue(Mono<Weather> mono) {
	    return mono.block();
	}
	
	@Scheduled(fixedRate = 1200000)
	public void getWeatherScheduledOfOsaka() {
		Mono<Weather> resp = this.service.getWeather("Japan", "Osaka");
		logger.info(getValue(resp).getCity() + " at " + getValue(resp).getTemperature());
		WeatherReading reading = new WeatherReading(getValue(resp).getTemperature(), getValue(resp).getCity(), getValue(resp).getDate());
		readingRepository.save(reading);
	}

	@GetMapping("/at-moment/")
	public Mono<Weather> getWeatherByCity(@RequestParam("city") String city) {
		logger.info("City entered.");
		return this.service.getWeatherByCity(city);
	}
	
	@GetMapping("/get-all-by-city/")
	@Transactional
	public List<WeatherReading> getAllByCity(@RequestParam("city") String city) {
		logger.info("Fetching all data from {}", city);
		return readingRepository.findAllByCity(city);
	}
	
	@GetMapping("/compare-yesterday/")
	@Transactional
	public WeatherReading getComparison(@RequestParam("city") String city) {
		Mono<Weather> resp = this.service.getWeatherByCity(city);
		WeatherReading reading = new WeatherReading(getValue(resp).getTemperature(), getValue(resp).getCity(), getValue(resp).getDate());
//		readingRepository.save(reading);
		
		Timestamp today = reading.getDate();
		return readingRepository.findByDate(today, city);
	}
	
	@GetMapping("/now-and-week-ago/")
	public WeatherReading getComparisonNow() {
		return readingRepository.findNow();
	}
	
}
