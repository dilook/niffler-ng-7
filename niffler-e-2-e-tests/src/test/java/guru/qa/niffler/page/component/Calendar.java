package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class Calendar extends BaseComponent<Calendar> {

  public Calendar(SelenideElement self) {
    super(self);
  }

  public Calendar() {
    super($(".MuiPickersLayout-root"));
  }

  @Nonnull
  @Step("Select date")
  public Calendar selectDateInCalendar(Date date) {
    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
    self.setValue(formatter.format(date));
    return this;
  }
}
