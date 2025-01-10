package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;

public class AllPeoplePage {
    private final ElementsCollection allPeopleRows = $$("#all tr");

    public AllPeoplePage checkThatExistOutcomeRequestTo(String friendName) {
        allPeopleRows.find(text(friendName)).shouldHave(text("Waiting..."));
        return this;
    }
}
