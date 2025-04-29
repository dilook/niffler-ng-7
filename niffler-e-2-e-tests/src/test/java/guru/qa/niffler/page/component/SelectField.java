package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class SelectField extends BaseComponent<SelectField>{

    public SelectField(SelenideElement self) {
        super(self);
    }

    private final ElementsCollection selectItems = $$("ul li[role='option']");


    @Step("Select value {0}")
    public void selectByValue(String value) {
        self.click();
        selectItems.find(text(value)).click();
        self.shouldHave(text(value));
    }
}
