package guru.qa.niffler.page;

import guru.qa.niffler.page.component.PeopleTable;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PeoplePage extends BasePage<PeoplePage> {

    private final PeopleTable peopleTable = new PeopleTable();

    @Nonnull
    @Step("Check invitations from users {0}")
    public PeoplePage checkInvitationSentToUser(String[] usernames) {
        peopleTable.checkThatTableContainsUserWithRequest(usernames);
        return this;
    }
}
