package de.maik.resilientapp.recommender;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Service
public class VisitationService {

    private static final String RECOMMENDATION_TEXT_POPULAR = "Popular place. You should give it a try!";
    private static final String RECOMMENDATION_TEXT_UNPOPULAR = "Rather unpopular place. Maybe go somewhere else?";
    private static final String RECOMMENDATION_TEXT_FALLBACK = "Be adventurous and see for yourself!";
    @Value("${location.service.base.url}")
    private String locationServiceBaseUrl;
    private static final String RESOURCE_POPULARITY = "/popularity";
    private RestTemplate restTemplate;
    private Logger logger;

    @PostConstruct
    private void initialize() {
        this.logger = LoggerFactory.getLogger(VisitationService.class);
        logger.info("Initializing component.");
        this.restTemplate = new RestTemplate();
    }

    /**
     * Method secured by Hystrix Proxy, using defaults.
     *
     * @param locationId for which to provide a recommendation
     * @return travel advice for this location
     */
    @HystrixCommand(fallbackMethod = "getFallbackVisitationRecommendation")
    public VisitationRecommendation recommendVisitForLocation(int locationId) {
        VisitationRecommendation recommendation = new VisitationRecommendation();
        recommendation.setLocationId(locationId);
        Popularity popularity = retrievePopularityForLocation(locationId);

        if (popularity.getPopularityScore() > 50) {
            recommendation.setRecommendationText(RECOMMENDATION_TEXT_POPULAR);
        } else {
            recommendation.setRecommendationText(RECOMMENDATION_TEXT_UNPOPULAR);
        }

        return recommendation;
    }

    private Popularity retrievePopularityForLocation(int locationId) {
        String url = locationServiceBaseUrl + "/" + locationId + RESOURCE_POPULARITY;
        logger.info("Performing REST call against Location Information Service: '{}'", url);

        return restTemplate.getForObject(url, Popularity.class);
    }

    private VisitationRecommendation getFallbackVisitationRecommendation(int locationId) {
        logger.info("Retrieving Fallback Recommendation.");
        VisitationRecommendation recommendation = new VisitationRecommendation();
        recommendation.setLocationId(locationId);
        recommendation.setRecommendationText(RECOMMENDATION_TEXT_FALLBACK);

        return recommendation;
    }
}
