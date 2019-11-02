package de.maik.failingApp.location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
class LocationService {

    private static Logger logger = LoggerFactory.getLogger(LocationService.class);
    private static final String SCALE = "Celsius";
    private static final double LOWER_BOUNDARY = -10.0;
    private static final double UPPER_BOUNDARY = 40.0;

    Temperature getTemperature(int locationId) {
        Temperature temperatureReading = createRandomTemperature();
        logger.info("Temperature for location '{}' is '{}' degrees {}.", locationId, temperatureReading.getReading(), temperatureReading.getScale());

        return temperatureReading;
    }

    private Temperature createRandomTemperature() {
        Random random = new Random();
        double reading = LOWER_BOUNDARY + random.nextDouble() * (UPPER_BOUNDARY - LOWER_BOUNDARY);

        return new Temperature(reading, SCALE);
    }
}
