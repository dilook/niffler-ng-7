package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class FriendsPage {

    private final SelenideElement peopleTab = $("a[href='/people/friends']");
    private final SelenideElement allTab = $("a[href='/people/all']");
    private final SelenideElement requestsTable = $("#requests");
    private final SelenideElement friendsTable = $("#friends");

    private final SearchComponent search = new SearchComponent();

    @Nonnull
    public FriendsPage checkExistingFriends(String... expectedUsernames) {
        List<String> usernameOnFirstPage = friendsTable.$$("tr td").shouldHave(sizeGreaterThan(0)).texts();
        for (String username : expectedUsernames) {
            if (!usernameOnFirstPage.contains(username)) {
                search.find(username);
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
                search.find(username);
                requestsTable.$$("tr").should(texts(username));
            }
        }
        return this;
    }
}
