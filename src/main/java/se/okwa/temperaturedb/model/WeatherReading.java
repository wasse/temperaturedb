package se.okwa.temperaturedb.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "weather_reading")
public class WeatherReading extends AuditModel {
    @Id
    @GeneratedValue(generator = "id_generator")
    @SequenceGenerator(
            name = "id_generator",
            sequenceName = "id_sequence",
            initialValue = 1
    )
    private Long id;

    @Column
    private Double temperature;

    @Column(columnDefinition = "text")
    private String city;

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

    	
    
}