package de.maik.resilientapp.recommender.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration Test for Weather Repository.
 * <ul>
 *  <li>Loads a Spring Application Context</li>
 *  <li>Starts and fills a H2 InMem DB before running tests</li>
 * </ul>
 */
@SpringBootTest
@Sql({"classpath:datasets/weather/schema.sql", "classpath:datasets/weather/data.sql"})
class WeatherRepositoryIntegrationTest {

    @Test
    void retrievesMostRecentTemperatureRecordForExistingLocation(@Autowired WeatherRepository weatherRepository) {
        Temperature expectedTemperatureRecord = new Temperature();
        expectedTemperatureRecord.setReading(26.2);
        expectedTemperatureRecord.setScale("Celsius");

        Temperature actualTemperatureRecord = weatherRepository.findMostRecentTemperatureByLocationId(42);

        assertThat(actualTemperatureRecord).isEqualToComparingFieldByField(expectedTemperatureRecord);
    }

    @Test
    void returnsNullForNonExistingLocation(@Autowired WeatherRepository weatherRepository) {
        assertThat(weatherRepository.findMostRecentTemperatureByLocationId(0)).isNull();
    }

}