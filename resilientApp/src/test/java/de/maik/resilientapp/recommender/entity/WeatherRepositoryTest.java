package de.maik.resilientapp.recommender.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class WeatherRepositoryTest {

    @Test
    void loadsDataFromDb(@Autowired WeatherRepository weatherRepository) {
        assertThat(weatherRepository.findMostRecentByLocationId(42)).isNotNull();
    }

}