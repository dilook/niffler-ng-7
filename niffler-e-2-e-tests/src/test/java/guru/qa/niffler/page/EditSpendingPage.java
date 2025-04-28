package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Calendar;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import java.util.Date;

import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class EditSpendingPage {

  private final SelenideElement descriptionInput = $("#description");
  private final SelenideElement amountInput = $("#amount");
  private final SelenideElement categoryInput = $("#category");
  private final SelenideElement saveBtn = $("#save");

  private final Calendar calendar = new Calendar($("[name='date']"));

  @Nonnull
  @Step("Enter description {0}")
  public EditSpendingPage setNewSpendingDescription(String description) {
    descriptionInput.setValue(description);
    return this;
  }

  @Nonnull
  @Step("Fill spending required fields")
  public MainPage fillAndSaveSpending(double amount, String category, String description) {
    amountInput.setValue(String.valueOf(amount));
    categoryInput.setValue(category);
    descriptionInput.setValue(description);
    calendar.selectDateInCalendar(new Date());
    saveBtn.click();
    return new MainPage();
  }

  @Step("Save spending")
  public void save() {
    saveBtn.click();
  }
}
