package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.AuthApi;
import guru.qa.niffler.api.ThreadSafeCookieStore;
import guru.qa.niffler.api.UserDataApi;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersClient;
import guru.qa.niffler.utils.RandomDataUtils;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UsersApiClient implements UsersClient {

    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .cookieJar(new JavaNetCookieJar(
                    new CookieManager(
                            ThreadSafeCookieStore.INSTANCE,
                            CookiePolicy.ACCEPT_ALL
                    )
            ))
            .build();

    private final UserDataApi userDataApi = new Retrofit.Builder()
            .baseUrl(Config.getInstance().userdataUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
            .create(UserDataApi.class);
    private final AuthApi authApi = new Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(Config.getInstance().authUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build().create(AuthApi.class);

    @Override
    public UserJson createUser(String username, String password) {

        try {
            authApi.getRegisterPage().execute();
            Response<Void> regResponse = authApi.register(username,
                    password,
                    password,
                    ThreadSafeCookieStore.INSTANCE.getCookieValue("XSRF-TOKEN")
            ).execute();
            assertEquals(201, regResponse.code());

            Response<UserJson> response = userDataApi.getCurrentUser(username).execute();
            assertEquals(200, response.code());
            return Objects.requireNonNull(response.body()).addTestData(new TestData(password));
        } catch (IOException e) {
            throw new AssertionError(e);
        }
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