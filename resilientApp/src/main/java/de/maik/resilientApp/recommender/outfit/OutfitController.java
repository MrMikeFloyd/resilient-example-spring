package de.maik.resilientApp.recommender.outfit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recommender/outfit")
public class OutfitController {

    private Logger logger;
    private OutfitService outfitService;

    @Autowired
    public OutfitController(OutfitService outfitService) {
        logger = LoggerFactory.getLogger(OutfitController.class);
        this.outfitService = outfitService;
    }

    @GetMapping("/{locationId}")
    public String getRecommendedOutfitForLocation(@PathVariable int locationId) {
        logger.info("Retrieving outfit recommendation for location '{}'.", locationId);
        return outfitService.recommendForLocation(locationId);
    }
}
