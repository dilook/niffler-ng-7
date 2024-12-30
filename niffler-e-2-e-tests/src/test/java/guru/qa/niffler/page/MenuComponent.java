package guru.qa.niffler.page;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

class MenuComponent {
    void selectMenuItem(String menuItem) {
        $(byText(menuItem)).click();
    }
}
