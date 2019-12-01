package de.maik.resilientapp.recommender.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;

@Repository
public class WeatherRepository {

    private static final String SQL_STATEMENT_WEATHER = "SELECT temperature, scale FROM weather WHERE location_id = ? ORDER BY created DESC FETCH FIRST 1 ROWS ONLY";
    private JdbcTemplate jdbcTemplate;
    private Logger logger;

    @Autowired
    public WeatherRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void initialize() {
        this.logger = LoggerFactory.getLogger(WeatherRepository.class);
        logger.info("Initializing component.");
    }

    /**
     * Returns the most recent temperature reading for a given Location.
     *
     * @param locationId the location for which to get the most recent temperature reading
     * @return the most recent temperature reading; null if no records are found
     */
    public Temperature findMostRecentTemperatureByLocationId(Integer locationId) {
        List<Temperature> tempRecords = jdbcTemplate.query(
                SQL_STATEMENT_WEATHER,
                new Object[]{locationId},
                (rs, rowNum) -> new Temperature(rs.getDouble("temperature"), rs.getString("scale"))
        );

        return tempRecords.isEmpty() ? null : tempRecords.get(0);
    }

}
