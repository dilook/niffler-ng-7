package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementCondition;
import com.codeborne.selenide.WebElementsCondition;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;


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
        return new BubblesCondition(expectedBubbles);
    }

    @Nonnull
    public static WebElementsCondition bubblesInAnyOrder(@Nonnull Bubble... expectedBubbles) {
        return new BubblesInAnyOrderCondition(expectedBubbles);
    }

}
