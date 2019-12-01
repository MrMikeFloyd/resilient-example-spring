package de.maik.resilientapp;

import com.github.tomakehurst.wiremock.client.WireMock;
import de.maik.resilientapp.recommender.control.Outfit;
import de.maik.resilientapp.recommender.control.OutfitRecommendation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * API Testing with full application context up and running.
 * Web Environment allows us to use TestRestTemplate straight
 * out the box. For testing we need to:
 * <ul>
 * <li>Mock web services we depend on</li>
 * <li>Create and fill a test db</li>
 * </ul>
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 8081)
@Sql({"classpath:datasets/weather/schema.sql", "classpath:datasets/weather/data.sql"})
public class ApiTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void returns404ForNonExistentResource() {
        assertThat(testRestTemplate.getForEntity("/notavailable", OutfitRecommendation.class).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void returnsOutfitRecommendationBasedOnResponseFromLocationService() throws Exception {
        // GIVEN
        // - a location with ID '1'
        // - an external web service that reports 25 degrees Celsius for that location
        stubFor(WireMock.get(urlEqualTo("/locations/1/temperature"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody("{\"reading\":25.17914934790766,\"scale\":\"Celsius\"}")
                )
        );

        // WHEN - Requesting a recommended outfit for that location
        ResponseEntity<OutfitRecommendation> responseEntity = testRestTemplate.getForEntity("/recommender/1/outfit", OutfitRecommendation.class);

        // THEN - the returned Recommendation should reflect 'warm'
        OutfitRecommendation expectedOutfitRecommendation = new OutfitRecommendation();
        expectedOutfitRecommendation.setLocationId(1);
        expectedOutfitRecommendation.setOutfit(Outfit.WARM);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualToComparingFieldByField(expectedOutfitRecommendation);
    }

    @Test
    void returnsFallbackOutfitRecommendationBasedOnRecordFoundInDbWhenLocationServiceFails() {
        // GIVEN
        // - a location with ID '1'
        // - a failing web service
        stubFor(WireMock.get(urlEqualTo("/locations/1/temperature"))
                .willReturn(aResponse()
                        .withStatus(500)
                )
        );

        // WHEN - Requesting a recommended outfit for that location
        // - the given location having a fallback db record
        ResponseEntity<OutfitRecommendation> responseEntity = testRestTemplate.getForEntity("/recommender/1/outfit", OutfitRecommendation.class);

        // THEN - the returned Recommendation should reflect the Db record ('cold')
        OutfitRecommendation expectedOutfitRecommendation = new OutfitRecommendation();
        expectedOutfitRecommendation.setLocationId(1);
        expectedOutfitRecommendation.setOutfit(Outfit.COLD);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualToComparingFieldByField(expectedOutfitRecommendation);
    }

    @Test
    void returnsFallbackDefaultOutfitRecommendationWhenLocationServiceIsDelayedAndNoRecordFoundInDb() {
        // GIVEN
        // - a location with ID '2'
        // - a delayed response by the location service
        stubFor(WireMock.get(urlEqualTo("/locations/2/temperature"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withLogNormalRandomDelay(10000, 0.1)
                        .withBody("{\"reading\":25.17914934790766,\"scale\":\"Celsius\"}")
                )
        );

        // WHEN - Requesting a recommended outfit for that location
        // - the given location NOT having a fallback db record
        ResponseEntity<OutfitRecommendation> responseEntity = testRestTemplate.getForEntity("/recommender/2/outfit", OutfitRecommendation.class);

        // THEN - the returned Recommendation should reflect 'unknown'
        OutfitRecommendation expectedOutfitRecommendation = new OutfitRecommendation();
        expectedOutfitRecommendation.setLocationId(2);
        expectedOutfitRecommendation.setOutfit(Outfit.UNKNOWN);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualToComparingFieldByField(expectedOutfitRecommendation);
    }
}
