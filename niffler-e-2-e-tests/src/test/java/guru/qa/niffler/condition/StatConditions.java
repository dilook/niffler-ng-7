package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementCondition;
import com.codeborne.selenide.WebElementsCondition;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.CheckResult.accepted;
import static com.codeborne.selenide.CheckResult.rejected;

@ParametersAreNonnullByDefault
public class StatConditions {

  @Nonnull
  public static WebElementCondition bubble(Bubble expectedBubble) {
    final Color expectedColor = expectedBubble.color();

    return new WebElementCondition("color " + expectedColor.rgb + ", text " + expectedBubble.text()) {
      @NotNull
      @Override
      public CheckResult check(Driver driver, WebElement element) {
        final String rgba = element.getCssValue("background-color");
        return new CheckResult(
            expectedColor.rgb.equals(rgba),
            rgba
        );
      }
    };
  }

  @Nonnull
  public static WebElementsCondition bubble(@Nonnull Bubble... expectedBubbles) {
    return new WebElementsCondition() {

      private final Color[] expectedColors = Arrays.stream(expectedBubbles).map(Bubble::color).toArray(Color[]::new);
      private final String expectedRgba = Arrays.stream(expectedColors).map(c -> c.rgb).toList().toString();
      private final List<String> expectedText = Arrays.stream(expectedBubbles).map(Bubble::text).toList();

      @NotNull
      @Override
      public CheckResult check(Driver driver, List<WebElement> elements) {
        if (ArrayUtils.isEmpty(expectedColors)) {
          throw new IllegalArgumentException("No expected colors given");
        }
        if (expectedColors.length != elements.size()) {
          final String message = String.format("List size mismatch (expected: %s, actual: %s)", expectedColors.length, elements.size());
          return rejected(message, elements);
        }
        if (expectedText.isEmpty()) {
          throw new IllegalArgumentException("No expected texts given");
        }
        if (expectedText.size() != elements.size()) {
          final String message = String.format("List size mismatch (expected: %s, actual: %s)", expectedText.size(), elements.size());
          return rejected(message, elements);
        }

        boolean passed = true;
        final List<String> actualRgbaList = new ArrayList<>();
        for (int i = 0; i < elements.size(); i++) {
          final WebElement elementToCheck = elements.get(i);
          final Color colorToCheck = expectedColors[i];
          final String rgba = elementToCheck.getCssValue("background-color");
          actualRgbaList.add(rgba);
          if (passed) {
            passed = colorToCheck.rgb.equals(rgba);
          }
        }

        if (!passed) {
          final String actualRgba = actualRgbaList.toString();
          final String message = String.format(
              "List colors mismatch (expected: %s, actual: %s)", expectedRgba, actualRgba
          );
          return rejected(message, actualRgba);
        }
        return accepted();
      }

      @Override
      public String toString() {
        return expectedRgba;
      }
    };
  }
}
