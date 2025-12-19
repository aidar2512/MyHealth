package kg.myhealth.service;

import kg.myhealth.api.dto.health.AnalysisResult;
import kg.myhealth.api.dto.rec.RecommendationDto;
import kg.myhealth.domain.User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RecommendationService {

    private final HealthDataService healthDataService;
    private final AnalysisService analysisService;

    public RecommendationService(HealthDataService healthDataService, AnalysisService analysisService) {
        this.healthDataService = healthDataService;
        this.analysisService = analysisService;
    }

    public RecommendationDto latest(User u) {
        var entries = healthDataService.latest(u);

        List<String> recs = new ArrayList<>();
        Set<Object> seen = new HashSet<>();

        for (var e : entries) {
            if (seen.add(e.getType())) {
                AnalysisResult r = analysisService.analyze(e.getType(), e.getValue());
                recs.addAll(analysisService.recommendationsFrom(r));
            }
        }

        if (recs.isEmpty()) recs.add("No data yet. Add measurements to get recommendations.");
        return new RecommendationDto(recs);
    }
}
