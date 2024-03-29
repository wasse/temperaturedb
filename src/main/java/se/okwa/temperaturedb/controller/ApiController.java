package se.okwa.temperaturedb.controller;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

import se.okwa.temperaturedb.domain.Weather;
import se.okwa.temperaturedb.domain.WeatherService;
import se.okwa.temperaturedb.model.WeatherReading;
import se.okwa.temperaturedb.repository.WeatherReadingRepository;

@RestController
@RequestMapping("/weather")
public class ApiController {
	
	@Autowired
	private final WeatherService service;

	// Database
	@Autowired
	private WeatherReadingRepository readingRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(ApiController.class);
	
	public ApiController(WeatherService service) {
		this.service = service;
	}
	
	@GetMapping("/now/")
	public Mono<Weather> getWeather(@RequestParam("country") String country, @RequestParam("city") String city) {
		logger.info("Country and city entered.");
		
		Mono<Weather> resp = this.service.getWeather(country, city);
		
		logger.info(getValue(resp).getCity() + " AT " + getValue(resp).getTemperature());
		
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
	
	@GetMapping("/yesterday/")
	@Transactional
	public WeatherReading getYesterday(@RequestParam("city") String city) {
		Mono<Weather> resp = this.service.getWeatherByCity(city);
		WeatherReading reading = new WeatherReading(getValue(resp).getTemperature(), getValue(resp).getCity(), getValue(resp).getDate());
		
		Timestamp today = reading.getDate();
		return readingRepository.findByDate(today, city);
	}
	
	@GetMapping("/week-ago/")
	public WeatherReading getWeekBefore(@RequestParam("city") String city) {
		return readingRepository.findNow(city);
	}
	
	@GetMapping("/compareYesterday/")
	public List<WeatherReading> getTheNewestAndYesterday(@RequestParam("city") String city) {
		Mono<Weather> resp = this.service.getWeatherByCity(city);
		WeatherReading reading = new WeatherReading(getValue(resp).getTemperature(), getValue(resp).getCity(), getValue(resp).getDate());
		
		Timestamp today = reading.getDate();
		System.out.println(city);
//		
		WeatherReading yesterday = readingRepository.findByDate(today, city);
		WeatherReading now = readingRepository.findTheNewest(city);
		List<WeatherReading> list = new ArrayList<>();
		list.add(now); list.add(yesterday);
		
		return list;
	}
	
}
