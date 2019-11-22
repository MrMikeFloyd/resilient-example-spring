package de.maik.resilientapp.recommender.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig
class WeatherRepositoryTest {

    @Test
    void loadsDataFromDb(@Autowired WeatherRepository weatherRepository) {
        assertThat(weatherRepository.findMostRecentByLocationId(42)).isNotNull();
    }

}