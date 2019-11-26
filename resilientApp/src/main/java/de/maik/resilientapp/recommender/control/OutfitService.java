package de.maik.resilientapp.recommender.control;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import de.maik.resilientapp.recommender.entity.Temperature;
import de.maik.resilientapp.recommender.entity.WeatherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.NavigableMap;
import java.util.TreeMap;

@Service
public class OutfitService {

    @Value("${location.service.base.url}")
    private String locationServiceBaseUrl;
    private static final String RESOURCE_TEMPERATURE = "/temperature";
    private RestTemplate restTemplate;
    private WeatherRepository weatherRepository;
    private NavigableMap<Double, Outfit> suitableOutfits;
    private Logger logger;

    @Autowired
    public OutfitService(WeatherRepository weatherRepository) {
        this();
        this.weatherRepository = weatherRepository;
    }

    OutfitService() {
        this.suitableOutfits = initializeOutfitMapping();
    }

    @PostConstruct
    private void initialize() {
        this.logger = LoggerFactory.getLogger(OutfitService.class);
        logger.info("Initializing component.");
        this.restTemplate = new RestTemplate();
    }

    /**
     * Method secured by Hystrix Proxy, defaults overridden. Would otherwise be:
     * <ul>
     * <li>errorThresholdPercentage: >50%</li>
     * <li>requestVolumeThreshold: 20</li>
     * <li>timeInMilliseconds: 10000</li>
     * </ul>
     *
     * @param locationId for which to provide a recommendation
     * @return recommended outfit for this location
     */
    @HystrixCommand(
            commandProperties = {
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "40"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "15"),
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "20000")},
            fallbackMethod = "getFallbackRecommendation")
    public OutfitRecommendation recommendOutfitForLocation(int locationId) {
        OutfitRecommendation recommendation = new OutfitRecommendation();

        Temperature temperatureReading = retrieveTemperature(locationId);
        recommendation.setLocationId(locationId);
        recommendation.setOutfit(recommendOutfit(temperatureReading));

        return recommendation;
    }

    private Temperature retrieveTemperature(int locationId) {
        String url = locationServiceBaseUrl + "/" + locationId + RESOURCE_TEMPERATURE;
        logger.info("Performing REST call against Location Information Service: '{}'", url);

        return restTemplate.getForObject(url, Temperature.class);
    }

    private Outfit recommendOutfit(Temperature temperature) {
        Outfit recommendedOutfit = temperature != null ? suitableOutfits.floorEntry(temperature.getReading()).getValue() : Outfit.UNKNOWN;
        logger.info("The temperature is {} degrees => recommending outfit '{}'", temperature.getReading(), recommendedOutfit.value);

        return recommendedOutfit;
    }

    private OutfitRecommendation getFallbackRecommendation(int locationId) {
        logger.info("Retrieving Fallback Recommendation from local DB.");

        OutfitRecommendation recommendation = new OutfitRecommendation();
        recommendation.setLocationId(locationId);
        recommendation.setOutfit(recommendOutfit(weatherRepository.findMostRecentTemperatureByLocationId(locationId)));

        return recommendation;
    }

    private NavigableMap<Double, Outfit> initializeOutfitMapping() {
        suitableOutfits = new TreeMap<>();
        suitableOutfits.put(Double.NEGATIVE_INFINITY, Outfit.SUPER_COLD);
        suitableOutfits.put(-10D, Outfit.COLD);
        suitableOutfits.put(10D, Outfit.INTERMEDIATE);
        suitableOutfits.put(20D, Outfit.WARM);
        suitableOutfits.put(30D, Outfit.VERY_WARM);
        suitableOutfits.put(40D, Outfit.SUPER_WARM);

        return suitableOutfits;
    }

}
