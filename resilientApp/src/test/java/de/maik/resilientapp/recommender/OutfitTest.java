package de.maik.resilientapp.recommender;

import de.maik.resilientapp.recommender.control.Outfit;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OutfitTest {

    private static final String EXPECTED_OUTFIT_SUPER_COLD = "Polar expedition outfit";
    private static final String EXPECTED_OUTFIT_COLD = "Winter coat";
    private static final String EXPECTED_OUTFIT_INTERMEDIATE = "Light jacket";
    private static final String EXPECTED_OUTFIT_WARM = "Sweater and long pants";
    private static final String EXPECTED_OUTFIT_VERY_WARM = "T-Shirt and shorts";
    private static final String EXPECTED_OUTFIT_SUPER_WARM = "Swimsuit/trunks";
    private static final String EXPECTED_OUTFIT_UNKNOWN = "Be prepared for anything";

    @Test
    void superColdYieldsPolarOutfit() {
        Outfit outfit = Outfit.SUPER_COLD;
        assertThat(outfit.getValue()).isEqualTo(EXPECTED_OUTFIT_SUPER_COLD);
    }

    @Test
    void coldYieldsWinterCoat() {
        Outfit outfit = Outfit.COLD;
        assertThat(outfit.getValue()).isEqualTo(EXPECTED_OUTFIT_COLD);
    }

    @Test
    void intermediateYieldsWinterCoat() {
        Outfit outfit = Outfit.INTERMEDIATE;
        assertThat(outfit.getValue()).isEqualTo(EXPECTED_OUTFIT_INTERMEDIATE);
    }

    @Test
    void warmYieldsSweater() {
        Outfit outfit = Outfit.WARM;
        assertThat(outfit.getValue()).isEqualTo(EXPECTED_OUTFIT_WARM);
    }

    @Test
    void veryWarmYieldsWinterCoat() {
        Outfit outfit = Outfit.VERY_WARM;
        assertThat(outfit.getValue()).isEqualTo(EXPECTED_OUTFIT_VERY_WARM);
    }

    @Test
    void superWarmYieldsSwimmingOutfit() {
        Outfit outfit = Outfit.SUPER_WARM;
        assertThat(outfit.getValue()).isEqualTo(EXPECTED_OUTFIT_SUPER_WARM);
    }

    @Test
    void unknownYieldsUnspecific() {
        Outfit outfit = Outfit.UNKNOWN;
        assertThat(outfit.getValue()).isEqualTo(EXPECTED_OUTFIT_UNKNOWN);
    }

}