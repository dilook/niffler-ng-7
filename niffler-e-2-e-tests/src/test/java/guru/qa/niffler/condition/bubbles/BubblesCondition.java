package guru.qa.niffler.condition.bubbles;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.impl.Html;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.codeborne.selenide.CheckResult.accepted;
import static com.codeborne.selenide.CheckResult.rejected;

public class BubblesCondition extends AbstractBubblesCondition {

    public BubblesCondition(Bubble[] expectedBubbles) {
        super(expectedBubbles);
    }

    @NotNull
    @Override
    public CheckResult check(Driver driver, List<WebElement> elements) {
        List<String> actualTexts = communicator.texts(driver, elements);
        final List<SimpleBubble> actualBubbles = collectActualBubbles(elements, actualTexts);
        boolean allMatch = checkAllBubblesMatch(actualBubbles);

        if (expectedBubbles.length != elements.size()) {
            final String message = String.format("List size mismatch (expected: %s, actual: %s)",
                    expectedBubbles.length, elements.size());
            return rejected(message, ArrayUtils.toString(actualBubbles));
        }

        if (!allMatch) {
            return createMismatchResult(actualBubbles);
        }

        return accepted();
    }

    private boolean checkAllBubblesMatch(List<SimpleBubble> actualBubbles) {
        for (int i = 0; i < actualBubbles.size(); i++) {
            SimpleBubble actual = actualBubbles.get(i);
            String expectedRgba = expectedColors[i].rgb;
            String expectedText = expectedTexts.get(i);

            if (!expectedRgba.equals(actual.rgba()) || !Html.text.equals(actual.text(), expectedText)) {
                return false;
            }
        }
        return true;
    }
}
