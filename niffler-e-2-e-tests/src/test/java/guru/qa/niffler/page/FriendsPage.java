package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.SearchField;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class FriendsPage {

    private final SelenideElement peopleTab = $("a[href='/people/friends']");
    private final SelenideElement allTab = $("a[href='/people/all']");
    private final SelenideElement requestsTable = $("#requests");
    private final SelenideElement friendsTable = $("#friends");

    private final SearchField searchField = new SearchField();

    @Nonnull
    public FriendsPage checkExistingFriends(String... expectedUsernames) {
        List<String> usernameOnFirstPage = friendsTable.$$("tr td").shouldHave(sizeGreaterThan(0)).texts();
        for (String username : expectedUsernames) {
            if (!usernameOnFirstPage.contains(username)) {
                searchField.search(username);
                friendsTable.$$("tr").should(texts(username));
            }
        }
        return this;
    }

    @Nonnull
    public FriendsPage checkNoExistingFriends() {
        friendsTable.$$("tr").shouldHave(size(0));
        return this;
    }

    @Nonnull
    public FriendsPage checkExistingInvitations(String... expectedUsernames) {
        List<String> usernameOnFirstPage = requestsTable.$$("tr td").shouldHave(sizeGreaterThan(0)).texts();
        for (String username : expectedUsernames) {
            if (!usernameOnFirstPage.contains(username)) {
                searchField.search(username);
                requestsTable.$$("tr").should(texts(username));
            }
        }
        return this;
    }

    @Nonnull
    public FriendsPage acceptFriendRequest(String username) {
        SelenideElement row = requestsTable.$$("tr").find(text(username));
        row.find(byText("Accept")).click();
        return this;
    }

    @Nonnull
    public FriendsPage declineFriendRequest(String username) {
        SelenideElement row = requestsTable.$$("tr").find(text(username));
        row.find(byText("Decline")).click();
        return this;
    }
}
