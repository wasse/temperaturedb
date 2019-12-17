package se.okwa.temperaturedb.model;

import java.sql.Timestamp;

import javax.persistence.*;

@Entity
@Table(name = "weather_reading")
public class WeatherReading extends AuditModel {
    @Id
//    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @GeneratedValue(generator = "id_generator")
    @SequenceGenerator(
            name = "id_generator",
            sequenceName = "id_sequence",
            initialValue = 1
    )
    private Long id;
    
    @Column
    private Timestamp date;

    @Column
    private Double temperature;

    @Column(columnDefinition = "text")
    private String city;
    
    public WeatherReading() {}   
    
    public WeatherReading(Double temperature, String city) {
    	this.temperature = temperature;
    	this.city = city;
    }
    public WeatherReading(Double temperature, String city, Timestamp date) {
    	this(temperature, city);
    	this.date = date;
    }

	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

	public String getcity() {
		return city;
	}

	public void setcity(String city) {
		this.city = city;
	}

	public Long getId() {
		return id;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

}