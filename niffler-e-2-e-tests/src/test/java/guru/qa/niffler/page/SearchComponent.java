package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class SearchComponent {
    private final SelenideElement inputSearch = $("input[aria-label='search']");


    void find(String text) {
        inputSearch.setValue(text).pressEnter();
    }

}

