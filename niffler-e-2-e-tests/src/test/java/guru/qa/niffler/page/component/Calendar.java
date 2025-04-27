package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;

import javax.annotation.ParametersAreNonnullByDefault;
import java.text.SimpleDateFormat;
import java.util.Date;

@ParametersAreNonnullByDefault
public class Calendar {
  private final SelenideElement self;

  public Calendar(SelenideElement self) {
    this.self = self;
  }

  public Calendar selectDateInCalendar(Date date) {

    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
    self.setValue(formatter.format(date));
    return this;
  }
}
