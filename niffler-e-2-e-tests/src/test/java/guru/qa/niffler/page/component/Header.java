package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.*;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class Header {

    private final SelenideElement self = $("#root header");

    private final SelenideElement header = self.$("h1");

    private final SelenideElement buttonMenu = self.$("button");

    private final ElementsCollection menuItems = $$("#account-menu li");

    private final SelenideElement addNewSending = self.$("a[href='/spending']");

    @Nonnull
    public FriendsPage toFriendsPage() {
        buttonMenu.click();
        menuItems.find(text("Friends")).click();
        return new FriendsPage();
    }

    @Nonnull
    public ProfilePage toProfilePage() {
        buttonMenu.click();
        menuItems.find(text("Profile")).click();
        return new ProfilePage();
    }

    @Nonnull
    public PeoplePage toPeoplePage() {
        buttonMenu.click();
        menuItems.find(text("All People")).click();
        return new PeoplePage();
    }

    @Nonnull
    public LoginPage signOut() {
        buttonMenu.click();
        menuItems.find(text("Sign out")).click();
        return new LoginPage();
    }

    @Nonnull
    public EditSpendingPage addSpending() {
        addNewSending.click();
        return new EditSpendingPage();
    }

    @Nonnull
    public MainPage toMainPage() {
        header.click();
        return new MainPage();
    }

}
