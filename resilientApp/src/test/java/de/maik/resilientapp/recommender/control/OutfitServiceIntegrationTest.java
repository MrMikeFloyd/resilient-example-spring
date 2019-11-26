package de.maik.resilientapp.recommender.control;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql({"classpath:datasets/weather/schema.sql", "classpath:datasets/weather/data.sql"})
public class OutfitServiceIntegrationTest {

    @Test
    void retrievesRecommendationFromRemoteRestService(@Autowired OutfitService outfitService) {
        // TODO: Test Happy Day Scenario against mocked REST service
    }

    @Test
    void retrievesFallbackRecommendationFromLocalDatabaseWhenLocationFound(@Autowired OutfitService outfitService) {
        OutfitRecommendation expectedRecommendation = new OutfitRecommendation();
        int expectedLocationId = 42;
        expectedRecommendation.setLocationId(expectedLocationId);
        expectedRecommendation.setOutfit(Outfit.WARM);

        assertThat(outfitService.recommendOutfitForLocation(expectedLocationId)).isEqualToComparingFieldByField(expectedRecommendation);
    }

    @Test
    void retrievesFallbackRecommendationUnknownWhenNoLocationFoundInDatabase(@Autowired OutfitService outfitService) {
        OutfitRecommendation expectedRecommendation = new OutfitRecommendation();
        int expectedLocationId = 123;
        expectedRecommendation.setLocationId(expectedLocationId);
        expectedRecommendation.setOutfit(Outfit.UNKNOWN);

        assertThat(outfitService.recommendOutfitForLocation(expectedLocationId)).isEqualToComparingFieldByField(expectedRecommendation);
    }

}
