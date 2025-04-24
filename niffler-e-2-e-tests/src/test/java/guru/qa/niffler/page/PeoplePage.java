package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class PeoplePage {

    private final SelenideElement peopleTab = $("a[href='/people/friends']");
    private final SelenideElement allTab = $("a[href='/people/all']");
    private final SelenideElement peopleTable = $("#all");
    private final SearchComponent search = new SearchComponent();

    public PeoplePage checkInvitationSentToUser(String[] usernames) {
        for (String username : usernames) {
            search.find(username);
            SelenideElement friendRow = peopleTable.$$("tr").find(text(username));
            friendRow.shouldHave(text("Waiting..."));
        }
        return this;
    }
}
