package de.maik.resilientApp.recommender.outfit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.NavigableMap;
import java.util.TreeMap;

@Service
public class OutfitService {

    @Value("${temperature.service.base.url}")
    private String temperatureServiceBaseUrl;
    private static final String RESOURCE_TEMPERATURE = "/temperature";
    private RestTemplate restTemplate;
    private NavigableMap<Double, Outfit> suitableOutfits;
    private Logger logger;

    public OutfitService() {
        this.logger = LoggerFactory.getLogger(OutfitService.class);
        this.restTemplate = new RestTemplate();
        this.suitableOutfits = initializeOutfitMapping();
    }

    public String recommendForLocation(int locationId) {
        Temperature temperatureReading = retrieveTemperature(locationId);
        Outfit recommendedOutfit = recommendOutfit(temperatureReading);

        return recommendedOutfit.value;
    }

    private Temperature retrieveTemperature(int locationId) {
        String url = temperatureServiceBaseUrl + "/" + locationId + RESOURCE_TEMPERATURE;
        logger.info("Performing REST call against Temperature Service: '{}'", url);

        return restTemplate.getForObject(url, Temperature.class);
    }

    private Outfit recommendOutfit(Temperature temperature) {
        Outfit recommendedOutfit;
        if (temperature != null) {
            recommendedOutfit = suitableOutfits.floorEntry(temperature.getReading()).getValue();
        } else {
            recommendedOutfit = Outfit.UNKNOWN;
        }

        logger.info("The temperature is {} degrees => recommending outfit '{}'", temperature.getReading(), recommendedOutfit.value);

        return recommendedOutfit;
    }

    private NavigableMap<Double, Outfit> initializeOutfitMapping() {
        suitableOutfits = new TreeMap<>();
        suitableOutfits.put(Double.NEGATIVE_INFINITY, Outfit.SUPER_COLD);
        suitableOutfits.put(-10D, Outfit.COLD);
        suitableOutfits.put(10D, Outfit.INTERMEDIATE);
        suitableOutfits.put(20D, Outfit.WARM);
        suitableOutfits.put(30D, Outfit.VERY_WARM);
        suitableOutfits.put(Double.POSITIVE_INFINITY, Outfit.SUPER_WARM);

        return suitableOutfits;
    }

}
