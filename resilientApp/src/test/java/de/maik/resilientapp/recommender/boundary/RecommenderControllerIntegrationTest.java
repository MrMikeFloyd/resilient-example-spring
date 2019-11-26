package de.maik.resilientapp.recommender.boundary;

import de.maik.resilientapp.recommender.control.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecommenderController.class)
class RecommenderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VisitationService visitationService;

    @MockBean
    private OutfitService outfitService;

    @Test
    void callingOutfitEndpointYields200WithOutfitRecommendation() throws Exception {
        // GIVEN - the outfit service module outputting a recommendation for location 1
        OutfitRecommendation recommendation = new OutfitRecommendation();
        recommendation.setOutfit(Outfit.VERY_WARM);
        recommendation.setLocationId(1);
        given(outfitService.recommendOutfitForLocation(1)).willReturn(recommendation);

        // WHEN/THEN - the controller is invoked for location 1, the expected JSON object should be returned
        mockMvc.perform(get("/recommender/1/outfit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("locationId").value("1"))
                .andExpect(jsonPath("outfit").value("T-Shirt and shorts"));
    }

    @Test
    void callingVisitEndpointYields200WithVisitationRecommendation() throws Exception {
        // GIVEN - the visitation service module outputting a recommendation for location 1
        VisitationRecommendation recommendation = new VisitationRecommendation();
        recommendation.setRecommendationText("Recommended - you should go!");
        recommendation.setLocationId(1);
        given(visitationService.recommendVisitForLocation(1)).willReturn(recommendation);

        // WHEN/THEN - the controller is invoked for location 1, the expected JSON object should be returned
        mockMvc.perform(get("/recommender/1/visit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("locationId").value("1"))
                .andExpect(jsonPath("recommendationText").value("Recommended - you should go!"));
    }

}