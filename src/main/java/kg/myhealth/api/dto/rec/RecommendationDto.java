package kg.myhealth.api.dto.rec;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter @AllArgsConstructor
public class RecommendationDto {
    private List<String> recommendations;
}
