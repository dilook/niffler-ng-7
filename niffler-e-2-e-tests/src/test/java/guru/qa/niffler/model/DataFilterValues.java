package guru.qa.niffler.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum DataFilterValues {
  TODAY("Today"), WEEK("Last week"), MONTH("Last month"), ALL("All time");

  private final String period;

    DataFilterValues(String period) {
        this.period = period;
    }

}
