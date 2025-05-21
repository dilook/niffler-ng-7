package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementsCondition;
import com.codeborne.selenide.impl.ElementCommunicator;
import com.codeborne.selenide.impl.Html;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.CheckResult.accepted;
import static com.codeborne.selenide.CheckResult.rejected;
import static com.codeborne.selenide.impl.Plugins.inject;

public class BubblesInAnyOrderCondition extends WebElementsCondition {
    private record SimpleBubble(String rgba, String text) {
        @NotNull
        @Override
        public String toString() {
            return String.format("Bubble{%s, text=%s}", rgba, text);
        }
    }

    private final Bubble[] expectedBubbles;
    private final Color[] expectedColors;
    private final List<String> expectedTexts;
    private static final ElementCommunicator communicator = inject(ElementCommunicator.class);

    public BubblesInAnyOrderCondition(Bubble[] expectedBubbles) {
        this.expectedBubbles = expectedBubbles;
        this.expectedColors = Arrays.stream(expectedBubbles).map(Bubble::color).toArray(Color[]::new);
        this.expectedTexts = Arrays.stream(expectedBubbles).map(Bubble::text).toList();

        if (ArrayUtils.isEmpty(expectedBubbles)) {
            throw new IllegalArgumentException("No expected bubbles given");
        }
    }

    @NotNull
    @Override
    public CheckResult check(Driver driver, List<WebElement> elements) {
        List<String> actualTexts = communicator.texts(driver, elements);
        final List<SimpleBubble> actualBubbles = collectActualBubbles(elements, actualTexts);

        if (expectedBubbles.length != elements.size()) {
            final String message = String.format("List size mismatch (expected: %s, actual: %s)",
                    expectedBubbles.length, elements.size());
            return rejected(message, ArrayUtils.toString(actualBubbles));
        }

        for (Bubble expectedBubble : expectedBubbles) {
            boolean found = checkContainsBubblesMatch(actualBubbles, expectedBubble);
            if (!found) {
                return createMismatchResult(actualBubbles);
            }
        }

        return accepted();
    }

    private List<SimpleBubble> collectActualBubbles(List<WebElement> elements, List<String> actualTexts) {
        List<SimpleBubble> actualBubbles = new ArrayList<>();

        for (int i = 0; i < elements.size(); i++) {
            final WebElement element = elements.get(i);
            final String actualRgba = element.getCssValue("background-color");
            final String actualText = actualTexts.get(i);

            actualBubbles.add(new SimpleBubble(actualRgba, actualText));
        }

        return actualBubbles;
    }

    private boolean checkContainsBubblesMatch(List<SimpleBubble> actualBubbles, Bubble expectedBubble) {
        for (SimpleBubble bubble : actualBubbles) {
            if (bubble.rgba().equals(expectedBubble.color().rgb) && Html.text.equals(bubble.text(), expectedBubble.text())) {
                return true;
            }
        }
        return false;
    }

    private CheckResult createMismatchResult(List<SimpleBubble> actualBubbles) {
        StringBuilder stringBuilder = new StringBuilder("List bubbles mismatch [\n");

        for (int i = 0; i < actualBubbles.size(); i++) {
            SimpleBubble actual = actualBubbles.get(i);
            String expectedRgba = expectedColors[i].rgb;
            String expectedText = expectedTexts.get(i);

            stringBuilder.append(
                    String.format("expected: Bubble{%s, text=%s} actual: Bubble{%s, text=%s})\n",
                            expectedRgba, expectedText, actual.rgba(), actual.text()
                    )
            );
        }

        return rejected(stringBuilder.append("\n]").toString(), actualBubbles.toString());
    }

    @Override
    public String toString() {
        return Arrays.toString(expectedBubbles);
    }

    @Override
    public String errorMessage() {
        return "BubblesInAnyOrder check failed";
    }
}
