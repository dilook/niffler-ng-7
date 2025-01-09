package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class FriendsPage {

    private final ElementsCollection friendsRows = $$("#friends tr");
    private final ElementsCollection friendReqRows = $$("#requests tr");
    private final SelenideElement emptyUsersText = $(byText("There are no users yet"));

    public void checkThatFriendsTableContainsFriend(String friendName) {
        friendsRows.find(text(friendName)).should(visible);
    }

    public void checkThatRequestsTableContainsRequestFrom(String friendName) {
        friendReqRows.find(text(friendName)).should(visible);
    }

    public void checkThatFriendsTableIsEmpty() {
        emptyUsersText.shouldBe(visible);
    }
}
