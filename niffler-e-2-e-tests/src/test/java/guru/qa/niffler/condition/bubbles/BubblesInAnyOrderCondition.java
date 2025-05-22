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

public class BubblesInAnyOrderCondition extends AbstractBubblesCondition {

    public BubblesInAnyOrderCondition(Bubble[] expectedBubbles) {
        super(expectedBubbles);
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

    private boolean checkContainsBubblesMatch(List<SimpleBubble> actualBubbles, Bubble expectedBubble) {
        for (SimpleBubble bubble : actualBubbles) {
            if (bubble.rgba().equals(expectedBubble.color().rgb) && Html.text.equals(bubble.text(), expectedBubble.text())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String errorMessage() {
        return "BubblesInAnyOrder check failed";
    }
}
