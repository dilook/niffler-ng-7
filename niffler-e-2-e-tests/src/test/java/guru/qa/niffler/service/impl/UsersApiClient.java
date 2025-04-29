package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.AuthApi;
import guru.qa.niffler.api.UserDataApi;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.jetbrains.annotations.NotNull;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public class UsersApiClient implements UsersClient {

    private static final Config CFG = Config.getInstance();

    private final UserDataApi userDataApi = new RestClient.EmtyRestClient(CFG.userdataUrl()).create(UserDataApi.class);
    private final AuthApi authApi = new RestClient.EmtyRestClient(CFG.authUrl()).create(AuthApi.class);

    @Override
    @Nonnull
    public UserJson createUser(String username, String password) {
        try {
            authApi.getRegisterPage().execute();
            Response<Void> regResponse = authApi.register(username,
                    password,
                    password,
                    ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN")
            ).execute();
            assertEquals(201, regResponse.code());

            return waitAndGetUser(username, password);
        } catch (IOException | InterruptedException e) {
            throw new AssertionError(e);
        }

    }

    // При создании юзеров через апи иногда ивент не успевает обработаться в кафке в и создаться в сервисе userdata,
    // поэтому необходимо подождать
    @NotNull
    private UserJson waitAndGetUser(String username, String password) throws IOException, InterruptedException {
        StopWatch sw = StopWatch.createStarted();
        while (sw.getTime(TimeUnit.SECONDS) < 30) {
            Response<UserJson> userJson = userDataApi.getCurrentUser(username).execute();
            assertEquals(200, userJson.code());
            if(userJson.body() != null && userJson.body().id() != null) {
                return userJson.body().addTestData(new TestData(password));
            } else {
                Thread.sleep(100);
            }
        }
        throw new AssertionError("User wasn't created in 30 seconds");
    }

    @Override
    public void addIncomeInvitation(UserJson targetUser, int count) {
        if (count < 1) {
            return;
        }
        for (int i = 0; i < count; i++) {
            UserJson tempUser = createUser(RandomDataUtils.randomUsername(), "12345");
            try {
                Response<UserJson> response = userDataApi.sendInvitation(tempUser.username(), targetUser.username())
                        .execute();
                assertEquals(200, response.code());
                targetUser.testData().incomeInvitations().add(tempUser);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }
    }

    @Override
    public void addOutcomeInvitation(UserJson targetUser, int count) {
        if (count < 1) {
            return;
        }
        for (int i = 0; i < count; i++) {
            UserJson tempUser = createUser(RandomDataUtils.randomUsername(), "12345");
            try {
                Response<UserJson> response = userDataApi.sendInvitation(targetUser.username(), tempUser.username())
                        .execute();
                assertEquals(200, response.code());
                targetUser.testData().outcomeInvitations().add(tempUser);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }
    }

    @Override
    public void addFriend(UserJson targetUser, int count) {
        if (count < 1) {
            return;
        }
        for (int i = 0; i < count; i++) {
            UserJson tempUser = createUser(RandomDataUtils.randomUsername(), "12345");
            try {
                Response<UserJson> response = userDataApi.sendInvitation(tempUser.username(), targetUser.username())
                        .execute();
                assertEquals(200, response.code());
                response = userDataApi.acceptInvitation(targetUser.username(), tempUser.username())
                        .execute();
                assertEquals(200, response.code());
                targetUser.testData().friends().add(response.body());
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }
    }
}
