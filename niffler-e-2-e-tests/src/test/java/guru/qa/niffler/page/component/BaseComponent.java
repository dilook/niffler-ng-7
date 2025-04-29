package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import javax.annotation.ParametersAreNonnullByDefault;


@ParametersAreNonnullByDefault
public abstract class BaseComponent<T extends BaseComponent<?>> {

  @Getter
  protected final SelenideElement self;

  protected BaseComponent(SelenideElement self) {
    this.self = self;
  }

}
