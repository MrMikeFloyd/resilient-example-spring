package de.maik.resilientapp.recommender.control;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.jdbc.Sql;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for OutfitService
 * <ul>
 *  <li>Uses WireMock for stubbing the remote REST service</li>
 *  <li>Uses InMem DB for fallback calls</li>
 * </ul>
 */
@SpringBootTest
@AutoConfigureWireMock(port = 8081)
@Sql({"classpath:datasets/weather/schema.sql", "classpath:datasets/weather/data.sql"})
public class OutfitServiceIntegrationTest {

    @Autowired
    private OutfitService outfitService;

    @Test
    void retrievesRecommendationFromRemoteRestService() {
        // GIVEN - a remote service that responds 25 degrees for location 1
        stubFor(get(urlEqualTo("/locations/1/temperature"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody("{\"reading\":25.17914934790766,\"scale\":\"Celsius\"}")
                )
        );

        // WHEN calling the outfit recommender for location 1
        int expectedLocationId = 1;
        OutfitRecommendation actualRecommendation = outfitService.recommendOutfitForLocation(expectedLocationId);

        // THEN the recommended outfit should say 'warm'
        OutfitRecommendation expectedRecommendation = new OutfitRecommendation();
        expectedRecommendation.setLocationId(expectedLocationId);
        expectedRecommendation.setOutfit(Outfit.WARM);
        assertThat(actualRecommendation).isEqualToComparingFieldByField(expectedRecommendation);
    }

    @Test
    void retrievesUnknownOutfitRecommendationForNonExistentLocationInRemoteRestService() {
        // GIVEN - a remote service that reports 25 degrees for location 1
        stubFor(get(urlEqualTo("/locations/1/temperature"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody("{\"reading\":25.17914934790766,\"scale\":\"Celsius\"}")
                )
        );

        // WHEN calling the outfit recommender for a different location ('2')
        int expectedLocationId = 2;
        OutfitRecommendation actualRecommendation = outfitService.recommendOutfitForLocation(expectedLocationId);

        // THEN the recommended outfit should say 'unknown'
        OutfitRecommendation expectedRecommendation = new OutfitRecommendation();
        expectedRecommendation.setLocationId(expectedLocationId);
        expectedRecommendation.setOutfit(Outfit.UNKNOWN);
        assertThat(actualRecommendation).isEqualToComparingFieldByField(expectedRecommendation);
    }

    @Test
    void retrievesFallbackRecommendationFromLocalDatabaseWhenLocationFound() {
        OutfitRecommendation expectedRecommendation = new OutfitRecommendation();
        int expectedLocationId = 42;
        expectedRecommendation.setLocationId(expectedLocationId);
        expectedRecommendation.setOutfit(Outfit.WARM);

        assertThat(outfitService.recommendOutfitForLocation(expectedLocationId)).isEqualToComparingFieldByField(expectedRecommendation);
    }

    @Test
    void retrievesFallbackRecommendationUnknownWhenNoLocationFoundInDatabase() {
        OutfitRecommendation expectedRecommendation = new OutfitRecommendation();
        int expectedLocationId = 123;
        expectedRecommendation.setLocationId(expectedLocationId);
        expectedRecommendation.setOutfit(Outfit.UNKNOWN);

        assertThat(outfitService.recommendOutfitForLocation(expectedLocationId)).isEqualToComparingFieldByField(expectedRecommendation);
    }

}
