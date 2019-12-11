package se.okwa.temperaturedb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import se.okwa.temperaturedb.model.WeatherReading;

@Repository
public interface WeatherReadingRepository extends JpaRepository<WeatherReading, Long> {
//	List<WeatherReading> findByReadingId(Long weatherReadingId);
	List<WeatherReading> findAllByCity(String city);
}
