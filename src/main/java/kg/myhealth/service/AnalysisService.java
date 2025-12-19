package kg.myhealth.service;

import kg.myhealth.api.dto.health.AnalysisResult;
import kg.myhealth.domain.HealthMetricType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnalysisService {

    public AnalysisResult analyze(HealthMetricType type, double value) {
        Range r = switch (type) {
            case BLOOD_PRESSURE_SYSTOLIC -> new Range(90, 120);
            case BLOOD_PRESSURE_DIASTOLIC -> new Range(60, 80);
            case GLUCOSE -> new Range(3.9, 5.5);
            case PULSE -> new Range(60, 100);
        };

        String status;
        String msg;
        if (value < r.min) {
            status = "LOW";
            msg = "Below the normal range (" + r.min + " - " + r.max + ")";
        } else if (value > r.max) {
            status = "HIGH";
            msg = "Above the normal range (" + r.min + " - " + r.max + ")";
        } else {
            status = "NORMAL";
            msg = "Within the normal range (" + r.min + " - " + r.max + ")";
        }
        return new AnalysisResult(type, value, status, msg);
    }

    public List<String> recommendationsFrom(AnalysisResult r) {
        List<String> recs = new ArrayList<>();
        if ("NORMAL".equals(r.getStatus())) {
            recs.add("Looks stable. Keep tracking regularly.");
            return recs;
        }
        switch (r.getType()) {
            case GLUCOSE -> {
                recs.add("Repeat measurement and track meals.");
                recs.add("If this persists, consult a doctor.");
            }
            case PULSE -> {
                recs.add("Rest and re-check after 5 minutes.");
                recs.add("If symptoms occur, contact a specialist.");
            }
            case BLOOD_PRESSURE_SYSTOLIC, BLOOD_PRESSURE_DIASTOLIC -> {
                recs.add("Re-check after resting.");
                recs.add("If values remain abnormal, consult a doctor.");
            }
        }
        return recs;
    }

    private record Range(double min, double max) {}
}
