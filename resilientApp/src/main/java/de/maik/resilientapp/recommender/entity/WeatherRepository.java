package de.maik.resilientapp.recommender.entity;

import de.maik.resilientapp.recommender.control.VisitationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class WeatherRepository {

    private static final String SQL_STATEMENT_WEATHER = "SELECT temperature, scale, MAX(created) FROM weather WHERE location_id = ? GROUP BY temperature, scale";
    private JdbcTemplate jdbcTemplate;
    private Logger logger;

    @Autowired
    public WeatherRepository(JdbcTemplate jdbcTemplate) {
        this.logger = LoggerFactory.getLogger(WeatherRepository.class);
        logger.info("Initializing component.");
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Returns the most recent temperature reading for a given Location.
     *
     * @param locationId the location for which to get the most recent temperature reading
     * @return the most recent temperature reading; null of no records are found
     */
    public Temperature findMostRecentByLocationId(Integer locationId) {
        return jdbcTemplate.query(
                SQL_STATEMENT_WEATHER,
                new Object[]{locationId},
                (rs, rowNum) -> new Temperature(rs.getDouble("temperature"), rs.getString("scale"))
        ).get(0);
    }

}
