package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementsCondition;
import com.codeborne.selenide.impl.ElementCommunicator;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.rest.SpendJson;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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
                    return rejected(message, message);
                }
                for (int i = 0; i < elements.size(); i++) {
                    List<WebElement> cells = elements.get(i).findElements(By.tagName("td"));
                    StringBuilder errMessageBuilder = new StringBuilder();
                    SpendJson expectedSpend = expectedSpends[i];
                    if (!cells.get(1).getText().equals(expectedSpend.category().name())) {
                        errMessageBuilder.append(String.format(
                                "\nSpend category mismatch (expected: %s, actual: %s)",
                                expectedSpend.category().name(), cells.get(1).getText()
                        ));
                    }

                    String expectedAmount = normalizeAmount(expectedSpend.amount(), expectedSpend.currency());
                    if (!cells.get(2).getText().equals(expectedAmount)) {
                        errMessageBuilder.append(String.format(
                                "\nSpend amount mismatch (expected: %s, actual: %s)",
                                expectedSpend.amount(), cells.get(2).getText()
                        ));
                    }
                    if (!cells.get(3).getText().equals(expectedSpend.description())) {
                        errMessageBuilder.append(String.format(
                                "\nSpend description mismatch (expected: %s, actual: %s)",
                                expectedSpend.description(), cells.get(3).getText()
                        ));
                    }
                    SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
                    if (!cells.get(4).getText().equals(formatter.format(expectedSpend.spendDate()))) {
                        errMessageBuilder.append(String.format(
                                "\nSpend date mismatch (expected: %s, actual: %s)",
                                formatter.format(expectedSpend.spendDate()), cells.get(4).getText()
                        ));
                    }
                    String message = errMessageBuilder.toString();
                    if (!message.isEmpty()) {
                        return rejected(message,
                                "\nRow %s with mismatch %s".formatted(i + 1, message));
                    }
                }

                return accepted();
            }

            private static String normalizeAmount(Double amount, CurrencyValues currency) {
                String formatString = amount > amount.intValue() ? "%.2f %s" : "%.0f %s";
                return formatString.formatted(amount, currency.getSymbol());
            }

            @Override
            public String toString() {
                return Arrays.toString(expectedSpends);
            }

        };
    }
}
