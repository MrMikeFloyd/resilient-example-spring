package de.maik.failingapp.location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
class PopularityService {

    private static final int MAX_POPULARITY = 100;
    private static final int MAX_NUMOFVOTES = 10000;

    private static Logger logger = LoggerFactory.getLogger(PopularityService.class);

    Popularity getPopularity(int locationId) {
        Popularity popularity = createRandomPopularity();
        logger.info("Popularity for location '{}' is '{}' (number of votes: '{}').", locationId, popularity.getPopularityScore(), popularity.getNumberOfVotes());

        return popularity;
    }

    private Popularity createRandomPopularity() {
        Random random = new Random();
        Popularity popularity = new Popularity();

        popularity.setPopularityScore(random.nextInt(MAX_POPULARITY + 1));
        popularity.setNumberOfVotes(random.nextInt(MAX_NUMOFVOTES + 1));

        return popularity;
    }


}
