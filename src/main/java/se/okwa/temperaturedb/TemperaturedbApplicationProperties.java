package se.okwa.temperaturedb;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties("app.weather")
public class TemperaturedbApplicationProperties {
	
	private final Api api = new Api();
	
	public Api getApi() {
		return this.api;
	}
	
	public static class Api {
		@NotNull
		private String apiKey;
		
		public String getApiKey() {
			return this.apiKey;
		}
		
		public void setApiKey(String apiKey) {
			this.apiKey = apiKey;
		}
	}
}
