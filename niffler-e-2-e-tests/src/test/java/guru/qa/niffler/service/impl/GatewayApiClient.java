package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.GatewayApi;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.model.rest.FriendJson;
import guru.qa.niffler.model.rest.UserJson;
import io.qameta.allure.Step;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.Query;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GatewayApiClient extends RestClient {

  private final GatewayApi gatewayApi;

  public GatewayApiClient() {
    super(CFG.gatewayUrl());
    this.gatewayApi = create(GatewayApi.class);
  }

  @Step("Send GET request /api/friends/all to niffler-gateway")
  @Nonnull
  public List<UserJson> allFriends(String bearerToken,
                                   @Nullable String searchQuery) {
    final Response<List<UserJson>> response;
    try {
      response = gatewayApi.allFriends(bearerToken, searchQuery)
          .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return requireNonNull(response.body());
  }

  @Step("Send GET request /api/users/all to niffler-gateway")
  @Nonnull
  public List<UserJson> allUsers(String bearerToken,
                                   @Nullable String searchQuery) {
    final Response<List<UserJson>> response;
    try {
      response = gatewayApi.allUsers(bearerToken, searchQuery)
          .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return requireNonNull(response.body());
  }

  @Step("Send DELETE request /api/friends to niffler-gateway")
  public void removeFriend(String bearerToken, String targetUsername) {
    final Response<Void> response;
    try {
      response = gatewayApi.removeFriend(bearerToken, targetUsername)
              .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
  }

  @Step("Send POST request /api/invitations/accept to niffler-gateway")
  @Nonnull
  public UserJson acceptInvitation(String bearerToken, String username) {
    final Response<UserJson> response;
    try {
      response = gatewayApi.acceptInvitation(bearerToken, new FriendJson(username))
              .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return requireNonNull(response.body());
  }

  @Step("Send POST request /api/invitations/decline to niffler-gateway")
  @Nonnull
  public UserJson declineInvitation(String bearerToken, String username) {
    final Response<UserJson> response;
    try {
      response = gatewayApi.declineInvitation(bearerToken, new FriendJson(username))
              .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return requireNonNull(response.body());
  }

  @Step("Send POST request /api/invitations/send to niffler-gateway")
  @Nonnull
  public UserJson sendInvitation(String bearerToken, String username) {
    final Response<UserJson> response;
    try {
      response = gatewayApi.sendInvitation(bearerToken, new FriendJson(username))
              .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return requireNonNull(response.body());
  }



}
