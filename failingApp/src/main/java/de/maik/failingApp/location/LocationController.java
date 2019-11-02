package de.maik.failingApp.location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/location")
public class LocationController {

    private static Logger logger;
    private LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        logger = LoggerFactory.getLogger(LocationService.class);
        this.locationService = locationService;
    }

    @GetMapping("/{locationId}/temperature")
    public double getTemperatureForLocationId(@PathVariable int locationId) {
        logger.info("Retrieving temperature for location '{}'.", locationId);
        return locationService.getTemperature(locationId);
    }

}
