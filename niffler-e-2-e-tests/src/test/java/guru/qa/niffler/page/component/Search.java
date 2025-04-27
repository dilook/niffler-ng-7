package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class Search {

    private final SelenideElement inputSearch = $("input[aria-label='search']");

    public void find(String text) {
        inputSearch.setValue(text).pressEnter();
    }
}

