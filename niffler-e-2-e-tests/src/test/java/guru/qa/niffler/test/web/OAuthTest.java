package guru.qa.niffler.test.web;

import guru.qa.niffler.api.AuthApiClient;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.utils.OAuthUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OAuthTest {

    private final AuthApiClient authApiClient = new AuthApiClient();

    @Test
    @User
    void oauthTest(UserJson user) {
        String codeVerifier = OAuthUtils.generateCodeVerifier();
        String codeChallenge = OAuthUtils.generateCodeChallenge(codeVerifier);
        authApiClient.preRequest(codeChallenge);
        String code = authApiClient.login(user.username(), user.testData().password());
        String token = authApiClient.token(code, codeVerifier);
        assertNotNull(token);
    }
}
