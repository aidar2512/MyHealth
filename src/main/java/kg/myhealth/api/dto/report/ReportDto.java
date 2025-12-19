package kg.myhealth.api.dto.report;

import kg.myhealth.api.dto.health.AnalysisResult;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Getter @AllArgsConstructor
public class ReportDto {
    private Instant generatedAt;
    private Map<String, Double> latestValues;
    private List<AnalysisResult> analysis;
}
