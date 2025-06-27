package guru.qa.niffler.model.allure;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record AllureResults(
    @JsonProperty("results")
    List<AllureResult> results
) {
}