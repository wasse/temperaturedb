package se.okwa.temperaturedb.domain;

import reactor.core.publisher.Mono;

public interface WeatherServiceInterface {
	
	public Mono<Weather> getWeather(String country, String city);
	
	public Mono<Weather> getWeatherByCity(String city);

}
