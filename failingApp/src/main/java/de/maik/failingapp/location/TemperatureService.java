package de.maik.failingapp.location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
class TemperatureService {

    private static Logger logger = LoggerFactory.getLogger(TemperatureService.class);
    private static final String SCALE = "Celsius";
    private static final double LOWER_BOUNDARY = -10.0;
    private static final double UPPER_BOUNDARY = 40.0;

    Temperature getTemperature(int locationId) {
        invokeLatencyMonkey();
        invokeChaosMonkey();

        Temperature temperatureReading = createRandomTemperature();
        logger.info("Temperature for location '{}' is '{}' degrees {}.", locationId, temperatureReading.getReading(), temperatureReading.getScale());

        return temperatureReading;
    }

    private Temperature createRandomTemperature() {
        return new Temperature(LOWER_BOUNDARY + new Random().nextDouble() * (UPPER_BOUNDARY - LOWER_BOUNDARY), SCALE);
    }

    /**
     * Sleeps for a while, or does nothing.
     */
    private void invokeLatencyMonkey() {
        if (isActionNecessary()) {
            logger.info("Latency Monkey decided to wait for a while.");
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                logger.error("Thread interrupted", e);
            }
        } else {
            logger.info("Latency Monkey decided to do nothing.");
        }
    }

    /**
     * Throws Temperature Reader Exception, or does nothing.
     */
    private void invokeChaosMonkey() {
        if (isActionNecessary()) {
            throw new TemperatureReaderException("Chaos Monkey decided to kill the Temperature Reader.");
        } else {
            logger.info("Chaos Monkey decided to do nothing.");
        }
    }

    private boolean isActionNecessary() {
        return new Random().nextBoolean();
    }

}
