package de.maik.resilientapp;

import de.maik.resilientapp.recommender.control.OutfitRecommendation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class E2eTest {

    @Autowired
    TestRestTemplate testRestTemplate;

    @Test
    void returnsOutfitRecommendation() {
        ResponseEntity<OutfitRecommendation> responseEntity = testRestTemplate.getForEntity("/recommender/1/outfit", OutfitRecommendation.class);
        //TODO: Add TestRestTemplate Tests for testing against the outermost layer
    }
}
