package guru.qa.niffler.model;

import java.util.ArrayList;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public record TestData(String password,
                       List<CategoryJson> categories,
                       List<SpendJson> spendings,
                       List<UserJson> friends,
                       List<UserJson> outcomeInvitations,
                       List<UserJson> incomeInvitations) {
    public TestData(String password) {
        this(password, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    public String[] getFriendsNames(){
        return friends().stream().map(UserJson::username).toArray(String[]::new);
    }

    public String[] getOutcomeInvitationsNames(){
        return outcomeInvitations().stream().map(UserJson::username).toArray(String[]::new);
    }

    public String[] getIncomeInvitationsNames(){
        return incomeInvitations().stream().map(UserJson::username).toArray(String[]::new);
    }

}
