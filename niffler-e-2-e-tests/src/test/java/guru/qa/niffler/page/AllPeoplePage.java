package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$$x;

public class AllPeoplePage {
    private final ElementsCollection allPeopleRows = $$("#all");
    private final ElementsCollection outcomeRequestRows = $$x("//span[contains(text(),'Waiting...')]/../../../..");



    public AllPeoplePage checkThatExistOutcomeRequestTo(String friendName) {
        outcomeRequestRows.find(text(friendName)).shouldBe(visible);
        return this;
    }
}
