package de.maik.resilientapp.recommender;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OutfitServiceTest {

    private static final int LOCATION_ID = 0;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private Logger logger;

    @InjectMocks
    OutfitService outfitService;

    @Test
    void minus20YieldsSuperColdOutfit() {
        Temperature minus20 = new Temperature();
        minus20.setReading(-20D);

        when(restTemplate.getForObject(anyString(), eq(Temperature.class))).thenReturn(minus20);

        OutfitRecommendation expectedRecommendation = new OutfitRecommendation();
        expectedRecommendation.setOutfit(Outfit.SUPER_COLD);
        assertThat(outfitService.recommendOutfitForLocation(LOCATION_ID)).isEqualToComparingFieldByField(expectedRecommendation);
    }

    @Test
    void minus5YieldsColdOutfit() {
        Temperature minus5 = new Temperature();
        minus5.setReading(-10D);

        when(restTemplate.getForObject(anyString(), eq(Temperature.class))).thenReturn(minus5);

        OutfitRecommendation expectedRecommendation = new OutfitRecommendation();
        expectedRecommendation.setOutfit(Outfit.COLD);
        assertThat(outfitService.recommendOutfitForLocation(LOCATION_ID)).isEqualToComparingFieldByField(expectedRecommendation);
    }

    @Test
    void positive15YieldsIntermediateOutfit() {
        Temperature positive15 = new Temperature();
        positive15.setReading(15D);

        when(restTemplate.getForObject(anyString(), eq(Temperature.class))).thenReturn(positive15);

        OutfitRecommendation expectedRecommendation = new OutfitRecommendation();
        expectedRecommendation.setOutfit(Outfit.INTERMEDIATE);
        assertThat(outfitService.recommendOutfitForLocation(LOCATION_ID)).isEqualToComparingFieldByField(expectedRecommendation);
    }

    @Test
    void positive25YieldsWarmOutfit() {
        Temperature positive25 = new Temperature();
        positive25.setReading(25D);

        when(restTemplate.getForObject(anyString(), eq(Temperature.class))).thenReturn(positive25);

        OutfitRecommendation expectedRecommendation = new OutfitRecommendation();
        expectedRecommendation.setOutfit(Outfit.WARM);
        assertThat(outfitService.recommendOutfitForLocation(LOCATION_ID)).isEqualToComparingFieldByField(expectedRecommendation);
    }

    @Test
    void positive35YieldsVeryWarmOutfit() {
        Temperature positive35 = new Temperature();
        positive35.setReading(35D);

        when(restTemplate.getForObject(anyString(), eq(Temperature.class))).thenReturn(positive35);

        OutfitRecommendation expectedRecommendation = new OutfitRecommendation();
        expectedRecommendation.setOutfit(Outfit.VERY_WARM);
        assertThat(outfitService.recommendOutfitForLocation(LOCATION_ID)).isEqualToComparingFieldByField(expectedRecommendation);
    }

    @Test
    void positive45YieldsSuperWarmOutfit() {
        Temperature positive45 = new Temperature();
        positive45.setReading(45D);

        when(restTemplate.getForObject(anyString(), eq(Temperature.class))).thenReturn(positive45);

        OutfitRecommendation expectedRecommendation = new OutfitRecommendation();
        expectedRecommendation.setOutfit(Outfit.SUPER_WARM);
        assertThat(outfitService.recommendOutfitForLocation(LOCATION_ID)).isEqualToComparingFieldByField(expectedRecommendation);
    }

}