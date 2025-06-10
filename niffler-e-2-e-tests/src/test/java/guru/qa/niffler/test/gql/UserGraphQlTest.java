package guru.qa.niffler.test.gql;

import com.apollographql.apollo.api.ApolloResponse;
import com.apollographql.apollo.api.Error;
import com.apollographql.java.client.ApolloCall;
import com.apollographql.java.rx2.Rx2Apollo;
import guru.qa.UserWithFriendsCategoriesQuery;
import guru.qa.UserWithUsersDepthThreeQuery;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class UserGraphQlTest extends BaseGraphQlTest {

    @User(friends = 1)
    @Test
    @ApiLogin
    void shouldNotReturnCategoriesOtherUsers(@Token String bearerToken) {
        final ApolloCall<UserWithFriendsCategoriesQuery.Data> currenciesCall = apolloClient.query(new UserWithFriendsCategoriesQuery(0, 1, null, null))
                .addHttpHeader("authorization", bearerToken);

        final ApolloResponse<UserWithFriendsCategoriesQuery.Data> response = Rx2Apollo.single(currenciesCall).blockingGet();
        final List<Error> errors = response.errors;
        Assertions.assertNotNull(errors);
        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals("Can`t query categories for another user", errors.getFirst().getMessage());
        Assertions.assertEquals("BAD_REQUEST", errors.getFirst().getExtensions().get("classification"));
    }

    @User(friends = 1)
    @Test
    @ApiLogin
    void shouldNotFetchMoreThan2FriendsInSubQueries(@Token String bearerToken) {
        final ApolloCall<UserWithUsersDepthThreeQuery.Data> currenciesCall = apolloClient.query(new UserWithUsersDepthThreeQuery(0, 10, null, null))
                .addHttpHeader("authorization", bearerToken);

        final ApolloResponse<UserWithUsersDepthThreeQuery.Data> response = Rx2Apollo.single(currenciesCall).blockingGet();
        final List<Error> errors = response.errors;
        Assertions.assertNotNull(errors);
        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals("Can`t fetch over 2 friends sub-queries", errors.getFirst().getMessage());
        Assertions.assertEquals("BAD_REQUEST", errors.getFirst().getExtensions().get("classification"));
    }

}
