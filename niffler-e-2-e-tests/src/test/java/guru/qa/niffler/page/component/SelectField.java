package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class SelectField {

    public SelectField(SelenideElement self) {
        this.self = self;
    }
    private final SelenideElement self;
    private final ElementsCollection selectItems = $$("ul li[role='option']");


    public void selectByValue(String value) {
        self.click();
        selectItems.find(text(value)).click();
        self.shouldHave(text(value));
    }
}
