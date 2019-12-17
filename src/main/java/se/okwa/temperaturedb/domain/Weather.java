package se.okwa.temperaturedb.domain;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Weather {
	
	
	@JsonProperty("country")
	private String country;
	@JsonProperty("city")
	private String city;
	@JsonProperty("temperature")
	private double temperature;
	@JsonProperty("weather ID")
	private Integer weatherId;
	private String timestampString;
	private Timestamp fetchedDate;
	
	
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public double getTemperature() {
		return temperature;
	}
	public void setTemperature(double temperature) {
		this.temperature = getTemperatureInCelsius(temperature);
	}
	public Integer getWeatherId() {
		return weatherId;
	}
	public void setWeatherId(Integer weatherId) {
		this.weatherId = weatherId;
	}
	public String getTimestamp() {
		return timestampString;
	}
	public void setTimestamp(String timestamp) {
		this.timestampString = timestamp;
	}
	public Timestamp getDate() {
		return fetchedDate;
	}
	public void setDate(Timestamp date) {
		this.fetchedDate = date;
	}
	
	public double getTemperatureInCelsius(double temperature) {
		double celTemp = temperature - 273.15;
		
		NumberFormat nFormatter = NumberFormat.getInstance(Locale.US);
		DecimalFormat dFormatter = (DecimalFormat)nFormatter;
		dFormatter.applyPattern("#.##");
		
		return Double.parseDouble(dFormatter.format(celTemp));
	}
	
	@JsonProperty("weather")
	public void setWeather(List<Map<String, Object>> weatherEntries) {
		Map<String, Object> weather = weatherEntries.get(0);
		setWeatherId((Integer) weather.get("id"));
	}
	@JsonProperty("main")
	public void setMain(Map<String, Object> main) {
		setTemperature(Double.parseDouble(main.get("temp").toString()));
	}
	@JsonProperty("name")
	public void setCityName(String name) {
		setCity(name);
	}
	@JsonProperty("dt")
	public void setTheTimestamp(long dt) {
		long millis = dt * 1000;
		Date date = new Date(millis);
		Timestamp timestampSQL = new Timestamp(millis);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd, HH:mm:ss");
		setTimestamp(dateFormat.format(date));
		setDate(timestampSQL);
	}

}
