package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.text.SimpleDateFormat;
import java.util.Date;

@ParametersAreNonnullByDefault
public class Calendar {
  private final SelenideElement self;

  public Calendar(SelenideElement self) {
    this.self = self;
  }

  @Nonnull
  @Step("Select date")
  public Calendar selectDateInCalendar(Date date) {
    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
    self.setValue(formatter.format(date));
    return this;
  }
}
