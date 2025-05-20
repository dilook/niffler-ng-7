package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementCondition;
import com.codeborne.selenide.WebElementsCondition;
import com.codeborne.selenide.impl.ElementCommunicator;
import com.codeborne.selenide.impl.Html;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.CheckResult.*;
import static com.codeborne.selenide.CheckResult.Verdict.ACCEPT;
import static com.codeborne.selenide.CheckResult.Verdict.REJECT;
import static com.codeborne.selenide.impl.Plugins.inject;

@ParametersAreNonnullByDefault
public class StatConditions {

    @Nonnull
    public static WebElementCondition bubbles(Bubble expectedBubble) {
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
    public static WebElementsCondition bubbles(@Nonnull Bubble... expectedBubbles) {
        return new WebElementsCondition() {

            private final Color[] expectedColors = Arrays.stream(expectedBubbles).map(Bubble::color).toArray(Color[]::new);
            private final String expectedRgba = Arrays.stream(expectedColors).map(c -> c.rgb).toList().toString();
            private final List<String> expectedTexts = Arrays.stream(expectedBubbles).map(Bubble::text).toList();
            private static final ElementCommunicator communicator = inject(ElementCommunicator.class);

            @NotNull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                return mergeCheckResults(
                        getCheckResultForColor(elements),
                        getCheckResultForText(driver, elements)
                );
            }

            @Override
            public String toString() {
                return Arrays.toString(expectedBubbles);
            }


            private CheckResult mergeCheckResults(CheckResult checkResult1, CheckResult checkResult2) {
                Verdict verdict = checkResult1.verdict() == REJECT || checkResult2.verdict() == REJECT ?
                        REJECT :
                        ACCEPT;
                String mergedActualValue = checkResult1.actualValue() + ", " + checkResult2.actualValue();
                return new CheckResult(verdict, mergedActualValue);
            }

            private CheckResult getCheckResultForColor(List<WebElement> elements) {
                if (ArrayUtils.isEmpty(expectedColors)) {
                    throw new IllegalArgumentException("No expected colors given");
                }
                if (expectedColors.length != elements.size()) {
                    final String message = String.format("List size mismatch (expected: %s, actual: %s)", expectedColors.length, elements.size());
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

            private CheckResult getCheckResultForText(Driver driver, List<WebElement> elements) {
                if (expectedTexts.isEmpty()) {
                    throw new IllegalArgumentException("No expected texts given");
                }
                if (expectedTexts.size() != elements.size()) {
                    final String message = String.format("List size mismatch (expected: %s, actual: %s)", expectedTexts.size(), elements.size());
                    return rejected(message, elements);
                }
                List<String> actualTexts = communicator.texts(driver, elements);
                if (actualTexts.size() != expectedTexts.size()) {
                    String message = String.format("List size mismatch (expected: %s, actual: %s)", expectedTexts.size(), actualTexts.size());
                    return rejected(message, actualTexts);
                }
                for (int i = 0; i < expectedTexts.size(); i++) {
                    String expectedText = expectedTexts.get(i);
                    String actualText = actualTexts.get(i);
                    if (!Html.text.equals(actualText, expectedText)) {
                        String message = String.format("Text #%s mismatch (expected: \"%s\", actual: \"%s\")", i, expectedText, actualText);
                        return rejected(message, actualTexts);
                    }
                }
                return accepted();
            }
        };
    }
}
