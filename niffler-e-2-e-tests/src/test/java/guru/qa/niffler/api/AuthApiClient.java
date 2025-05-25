package guru.qa.niffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.config.Config;
import retrofit2.Response;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthApiClient {
    private static final Config CFG = Config.getInstance();

    final static String RESPONSE_TYPE = "code";
    final static String CLIENT_ID = "client";
    final static String SCOPE = "openid";
    final static String REDIRECT_URI = CFG.frontUrl() + "authorized";
    final static String CODE_CHALLENGE_METHOD = "S256";
    final static String GRANT_TYPE = "authorization_code";

    private final AuthApi authApi = new RestClient.EmtyRestClient(CFG.authUrl(), true).create(AuthApi.class);

    public void preRequest(String codeChallenge) {
        final Response<Void> response;
        try {
            response = authApi.authorize(
                    RESPONSE_TYPE,
                    CLIENT_ID,
                    SCOPE,
                    REDIRECT_URI,
                    codeChallenge,
                    CODE_CHALLENGE_METHOD
            ).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(200, response.code());
    }

    @Nonnull
    public String login(String login, String password) {
        final Response<Void> response;
        try {
            response = authApi.login(
                    login,
                    password,
                    ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN")
            ).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(200, response.code());
        String requestUrl = response.raw().request().url().toString();
        return URI.create(requestUrl).getQuery().split("code=")[1].split("&")[0];
    }

    public String token(String code, String codeVerifier) {
        final Response<JsonNode> response;
        try {
            response = authApi.token(
                    CLIENT_ID,
                    REDIRECT_URI,
                    GRANT_TYPE,
                    code,
                    codeVerifier
            ).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(200, response.code());
        return Objects.requireNonNull(response.body()).get("id_token").asText();
    }
}
