package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementsCondition;
import com.codeborne.selenide.impl.ElementCommunicator;
import guru.qa.niffler.model.rest.SpendJson;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.CheckResult.accepted;
import static com.codeborne.selenide.CheckResult.rejected;
import static com.codeborne.selenide.impl.Plugins.inject;

public class SpendConditions {

    @Nonnull
    public static WebElementsCondition spends(SpendJson... expectedSpends) {

        return new WebElementsCondition() {
            static final ElementCommunicator communicator = inject(ElementCommunicator.class);

            @NotNull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (expectedSpends.length != elements.size()) {
                    final String message = String.format("List size mismatch (expected: %s, actual: %s)",
                            expectedSpends.length, elements.size());
                    return rejected(message, elements);
                }
                for (WebElement element : elements) {
                    List<WebElement> cells = element.findElements(By.tagName("td"));
                    for (SpendJson expectedSpending : expectedSpends) {
                        StringBuilder errMessageBuilder = new StringBuilder();
                        if (!cells.get(1).getText().equals(expectedSpending.category().name())) {
                            errMessageBuilder.append(String.format(
                                    "Spend category mismatch (expected: %s, actual: %s)",
                                    expectedSpending.category().name(), cells.get(1).getText()
                            ));
                        }
                        if (!cells.get(2).getText().equals(expectedSpending.amount().toString() + " ₽")) {
                            errMessageBuilder.append(String.format(
                                    "Spend amount mismatch (expected: %s, actual: %s)",
                                    expectedSpending.amount(), cells.get(2).getText()
                            ));
                        }
                        if (!cells.get(3).getText().equals(expectedSpending.description())) {
                             errMessageBuilder.append(String.format(
                                    "Spend description mismatch (expected: %s, actual: %s)",
                                    expectedSpending.description(), cells.get(3).getText()
                            ));
                        }
                        SimpleDateFormat formatter = new SimpleDateFormat("MMMM d, yyyy");
                        if (!cells.get(4).getText().equals(formatter.format(expectedSpending.spendDate()))) {
                             errMessageBuilder.append(String.format(
                                    "Spend date mismatch (expected: %s, actual: %s)",
                                    formatter.format(expectedSpending.spendDate()), cells.get(4).getText()
                            ));
                        }
                        String message = errMessageBuilder.toString();
                        if(!message.isEmpty()) {
                            return rejected(message, elements);
                        }
                    }
                }

                return accepted();
            }

            @Override
            public String toString() {
                return Arrays.toString(expectedSpends);
            }

        };
    }
}
