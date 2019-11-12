package de.maik.failingapp.location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/locations")
public class LocationController {

    private static Logger logger;
    private PopularityService popularityService;
    private TemperatureService temperatureService;

    @Autowired
    public LocationController(PopularityService popularityService, TemperatureService temperatureService) {
        logger = LoggerFactory.getLogger(TemperatureService.class);
        this.popularityService = popularityService;
        this.temperatureService = temperatureService;
    }

    @GetMapping("/{locationId}/popularity")
    public Popularity getPopularityForLocation(@PathVariable int locationId) {
        logger.info("Retrieving popularity for location '{}'.", locationId);
        return popularityService.getPopularity(locationId);
    }

    @GetMapping("/{locationId}/temperature")
    public Temperature getTemperatureForLocationId(@PathVariable int locationId) {
        logger.info("Retrieving temperature for location '{}'.", locationId);
        return temperatureService.getTemperature(locationId);
    }

}
