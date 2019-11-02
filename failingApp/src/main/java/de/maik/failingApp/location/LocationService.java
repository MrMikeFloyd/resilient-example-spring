package de.maik.failingApp.location;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
class LocationService {

    private static final double lowerBoundary = -10.0;
    private static final double upperBoundary = 40.0;

    double getTemperature(int locationId) {
        return createRandomTemperature();
    }

    private double createRandomTemperature() {
        Random random = new Random();
        return lowerBoundary + random.nextDouble() * (upperBoundary - lowerBoundary);
    }
}
