package de.maik.resilientapp.recommender.boundary;

import de.maik.resilientapp.recommender.control.OutfitRecommendation;
import de.maik.resilientapp.recommender.control.OutfitService;
import de.maik.resilientapp.recommender.control.VisitationRecommendation;
import de.maik.resilientapp.recommender.control.VisitationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recommender")
public class RecommenderController {

    private Logger logger;
    private VisitationService visitationService;
    private OutfitService outfitService;

    @Autowired
    public RecommenderController(VisitationService visitationService, OutfitService outfitService) {
        logger = LoggerFactory.getLogger(RecommenderController.class);
        this.visitationService = visitationService;
        this.outfitService = outfitService;
    }

    @GetMapping("/{locationId}/visit")
    public VisitationRecommendation getVisitRecommendationForLocation(@PathVariable int locationId) {
        logger.info("Retrieving visitation recommendation for location '{}'.", locationId);
        return visitationService.recommendVisitForLocation(locationId);
    }

    @GetMapping("/{locationId}/outfit")
    public OutfitRecommendation getOutfitRecommendationForLocation(@PathVariable int locationId) {
        logger.info("Retrieving outfit recommendation for location '{}'.", locationId);
        return outfitService.recommendOutfitForLocation(locationId);
    }
}
