package se.okwa.temperaturedb.repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import se.okwa.temperaturedb.model.WeatherReading;

@Repository
public interface WeatherReadingRepository extends JpaRepository<WeatherReading, Long> {
	List<WeatherReading> findAllByCity(String city);
	
	@Query(value="select * from weather_reading"
			+ " where date >= :thisDay\\:\\:timestamp - '24 hour 20 minutes'\\:\\:interval"
			+ " and date < :thisDay\\:\\:timestamp - '24 hour 0 minutes'\\:\\:interval"
			+ " and city = :city"
			+ " limit 1",
			nativeQuery=true)
	WeatherReading findByDate(@Param("thisDay") Timestamp thisDay, @Param("city") String city);
	
	@Query(value="select * from weather_reading"
			+ " where date >= now() - '7 days 0 hour 20 minutes'\\:\\:interval"
			+ " and date < now() - '7 days 0 hour 0 minutes'\\:\\:interval"
			+ " and city = :city"
			+ " limit 1",
			nativeQuery=true)
	WeatherReading findNow(@Param("city") String city);
	
	@Query(value=" select * from weather_reading"
			+ " where date >= now() - '20 minutes'\\:\\:interval"
			+ " and city = :city"
			+ " LIMIT 1",
			nativeQuery=true)
	WeatherReading findTheNewest(@Param("city") String city);
}