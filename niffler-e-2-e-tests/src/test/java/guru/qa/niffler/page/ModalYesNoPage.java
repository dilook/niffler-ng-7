package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;

public class ModalYesNoPage {

    private final SelenideElement deleteBtn = $$("[aria-describedby='alert-dialog-slide-description'] button")
            .find(text("Delete"));
    private final SelenideElement cancelBtn = $$("[aria-describedby='alert-dialog-slide-description'] button")
            .find(text("Cancel"));


    void delete() {
        deleteBtn.click();
    }


}
