package guru.qa.niffler.condition.bubbles;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementsCondition;
import com.codeborne.selenide.impl.ElementCommunicator;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.CheckResult.rejected;
import static com.codeborne.selenide.impl.Plugins.inject;

public abstract class AbstractBubblesCondition extends WebElementsCondition {

    protected record SimpleBubble(String rgba, String text) {
        @NotNull
        @Override
        public String toString() {
            return String.format("Bubble{%s, text=%s}", rgba, text);
        }
    }

    protected final Bubble[] expectedBubbles;
    protected final Color[] expectedColors;
    protected final List<String> expectedTexts;
    protected static final ElementCommunicator communicator = inject(ElementCommunicator.class);

    public AbstractBubblesCondition(Bubble[] expectedBubbles) {
        this.expectedBubbles = expectedBubbles;
        this.expectedColors = Arrays.stream(expectedBubbles).map(Bubble::color).toArray(Color[]::new);
        this.expectedTexts = Arrays.stream(expectedBubbles).map(Bubble::text).toList();

        if (ArrayUtils.isEmpty(expectedBubbles)) {
            throw new IllegalArgumentException("No expected bubbles given");
        }
    }

    @NotNull
    @Override
    public abstract CheckResult check(Driver driver, List<WebElement> elements);

    protected List<SimpleBubble> collectActualBubbles(List<WebElement> elements, List<String> actualTexts) {
        List<SimpleBubble> actualBubbles = new ArrayList<>();

        for (int i = 0; i < elements.size(); i++) {
            final WebElement element = elements.get(i);
            final String actualRgba = element.getCssValue("background-color");
            final String actualText = actualTexts.get(i);

            actualBubbles.add(new SimpleBubble(actualRgba, actualText));
        }

        return actualBubbles;
    }

    protected CheckResult createMismatchResult(List<SimpleBubble> actualBubbles) {
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
        return "Bubbles check failed";
    }
}
