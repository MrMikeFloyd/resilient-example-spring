package de.maik.resilientapp.recommender.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Ordinary Unit Test for Weather Repository
 * Dependencies are mocked - no application context/ database needed
 */
@ExtendWith(MockitoExtension.class)
public class WeatherRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private Logger logger;

    @InjectMocks
    private WeatherRepository weatherRepository;

    @Test
    void databaseReturningEmptyResultSetYieldsNull() {
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class))).thenReturn(new ArrayList<Temperature>());
        assertThat(weatherRepository.findMostRecentTemperatureByLocationId(0)).isNull();
    }

    @Test
    void databaseReturningOneRecordReturnsTemperature() {
        List<Temperature> temperaturesResultSet = new ArrayList<>();
        Temperature expectedTemp = new Temperature(12.34, "whatever");
        temperaturesResultSet.add(expectedTemp);

        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class))).thenReturn(temperaturesResultSet);

        assertThat(weatherRepository.findMostRecentTemperatureByLocationId(0)).isEqualToComparingFieldByField(expectedTemp);
    }

    @Test
    void databaseReturningMoreThanOneRecordReturnsFirstTemperature() {
        List<Temperature> temperaturesResultSet = new ArrayList<>();
        Temperature expectedTemp = new Temperature(12.34, "first");
        temperaturesResultSet.add(expectedTemp);
        temperaturesResultSet.add(new Temperature(00.00, "second"));
        temperaturesResultSet.add(new Temperature(00.00, "third"));

        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class))).thenReturn(temperaturesResultSet);

        assertThat(weatherRepository.findMostRecentTemperatureByLocationId(0)).isEqualToComparingFieldByField(expectedTemp);
    }
}
