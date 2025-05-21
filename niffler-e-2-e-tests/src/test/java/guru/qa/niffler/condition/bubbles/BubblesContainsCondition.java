package guru.qa.niffler.condition.bubbles;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.impl.Html;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.codeborne.selenide.CheckResult.accepted;
import static com.codeborne.selenide.CheckResult.rejected;

public class BubblesContainsCondition extends AbstractBubblesCondition {

    public BubblesContainsCondition(Bubble[] expectedBubbles) {
        super(expectedBubbles);
    }

    @NotNull
    @Override
    public CheckResult check(Driver driver, List<WebElement> elements) {
        List<String> actualTexts = communicator.texts(driver, elements);
        final List<SimpleBubble> actualBubbles = collectActualBubbles(elements, actualTexts);

        if (elements.isEmpty()) {
            return rejected("No bubbles found", "[]");
        }

        for (Bubble expectedBubble : expectedBubbles) {
            boolean found = checkBubbleExists(actualBubbles, expectedBubble);
            if (!found) {
                return rejected(
                    String.format("Expected bubble %s not found among actual bubbles", expectedBubble),
                    actualBubbles.toString()
                );
            }
        }

        return accepted();
    }

    private boolean checkBubbleExists(List<SimpleBubble> actualBubbles, Bubble expectedBubble) {
        for (SimpleBubble bubble : actualBubbles) {
            if (bubble.rgba().equals(expectedBubble.color().rgb) && 
                Html.text.equals(bubble.text(), expectedBubble.text())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String errorMessage() {
        return "BubblesContains check failed";
    }
}
