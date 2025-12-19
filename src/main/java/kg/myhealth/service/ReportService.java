package kg.myhealth.service;

import kg.myhealth.api.dto.health.AnalysisResult;
import kg.myhealth.api.dto.report.ReportDto;
import kg.myhealth.domain.HealthMetricType;
import kg.myhealth.domain.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class ReportService {

    private final HealthDataService healthDataService;
    private final AnalysisService analysisService;

    public ReportService(HealthDataService healthDataService, AnalysisService analysisService) {
        this.healthDataService = healthDataService;
        this.analysisService = analysisService;
    }

    public ReportDto summary(User u) {
        var entries = healthDataService.latest(u);

        Map<HealthMetricType, Double> latest = new EnumMap<>(HealthMetricType.class);
        for (var e : entries) {
            latest.putIfAbsent(e.getType(), e.getValue());
        }

        Map<String, Double> latestStr = new LinkedHashMap<>();
        List<AnalysisResult> analysis = new ArrayList<>();

        for (var t : HealthMetricType.values()) {
            if (latest.containsKey(t)) {
                double v = latest.get(t);
                latestStr.put(t.name(), v);
                analysis.add(analysisService.analyze(t, v));
            }
        }

        return new ReportDto(Instant.now(), latestStr, analysis);
    }
}
