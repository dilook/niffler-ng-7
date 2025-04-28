package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.Getter;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class PeopleTable {

    @Getter
    private final SelenideElement self = $("#simple-tabpanel-all");
    private final ElementsCollection rows = self.$("tbody").$$("tr");
    private final SearchField searchField = new SearchField();

    public PeopleTable() {
        self.shouldBe(visible);
    }

    @Step("Search user {0}")
    @Nonnull
    public PeopleTable searchUser(String username) {
        searchField.search(username);
        return this;
    }


    @Step("Check that table contains user(s) {0}")
    @Nonnull
    public PeopleTable checkThatTableContainsUserWithRequest(String... usernames) {
        for (String user : usernames) {
            searchUser(user);
            SelenideElement requestRow = rows.find(text(user)).shouldBe(visible);
            requestRow.shouldHave(text("Waiting..."));
        }
        return this;
    }

}
