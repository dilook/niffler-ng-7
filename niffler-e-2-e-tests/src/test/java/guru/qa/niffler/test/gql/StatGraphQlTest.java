package guru.qa.niffler.test.gql;

import com.apollographql.apollo.api.ApolloResponse;
import com.apollographql.java.client.ApolloCall;
import com.apollographql.java.rx2.Rx2Apollo;
import guru.qa.StatQuery;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CurrencyValues;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static guru.qa.type.CurrencyValues.RUB;

public class StatGraphQlTest extends BaseGraphQlTest {

    @User
    @Test
    @ApiLogin
    void emptyStatTest(@Token String bearerToken) {
        final ApolloCall<StatQuery.Data> currenciesCall = apolloClient.query(StatQuery.builder()
                        .filterCurrency(null)
                        .statCurrency(null)
                        .filterPeriod(null)
                        .build())
                .addHttpHeader("authorization", bearerToken);

        final ApolloResponse<StatQuery.Data> response = Rx2Apollo.single(currenciesCall).blockingGet();
        final StatQuery.Data data = response.dataOrThrow();
        StatQuery.Stat result = data.stat;
        Assertions.assertEquals(
                0.0,
                result.total
        );
    }


    @User(
            categories = {
                    @Category(name = "Disney", archived = true),
                    @Category(name = "Cats")
            },
            spendings = {
                    @Spending(category = "Cats", description = "Cat food", amount = 3_500.50),
                    @Spending(category = "Disney", description = "Trip to France", amount = 8000.0)
            }
    )
    @Test
    @ApiLogin
    void statWithArchivedCategoryTest(@Token String bearerToken) {
        final ApolloCall<StatQuery.Data> currenciesCall = apolloClient.query(StatQuery.builder()
                        .filterCurrency(null)
                        .statCurrency(null)
                        .filterPeriod(null)
                        .build())
                .addHttpHeader("authorization", bearerToken);

        final ApolloResponse<StatQuery.Data> response = Rx2Apollo.single(currenciesCall).blockingGet();
        final StatQuery.Data data = response.dataOrThrow();
        StatQuery.Stat result = data.stat;
        Assertions.assertEquals(
                11_500.5,
                result.total
        );
        Assertions.assertEquals(RUB, result.currency);
        Assertions.assertEquals(2, result.statByCategories.size());

        StatQuery.StatByCategory cats = result.statByCategories.getFirst();
        Assertions.assertEquals("RUB", cats.currency.rawValue);
        Assertions.assertEquals("Cats", cats.categoryName);
        Assertions.assertEquals(3_500.50, cats.sum);
        Assertions.assertEquals(getCurrentDateWithoutTime(), cats.firstSpendDate);
        Assertions.assertEquals(getCurrentDateWithoutTime(), cats.lastSpendDate);

        StatQuery.StatByCategory archived = result.statByCategories.getLast();
        Assertions.assertEquals("RUB", archived.currency.rawValue);
        Assertions.assertEquals("Archived", archived.categoryName);
        Assertions.assertEquals(8_000.0, archived.sum);
        Assertions.assertEquals(getCurrentDateWithoutTime(), archived.firstSpendDate);
        Assertions.assertEquals(getCurrentDateWithoutTime(), archived.lastSpendDate);
    }

    @User(
            categories = {
                    @Category(name = "Машина"),
                    @Category(name = "Еда")
            },
            spendings = {
                    @Spending(category = "Машина", description = "ТО-0", amount = 5_000.0),
                    @Spending(category = "Еда", description = "Кумыс", amount = 1_000.0, currency = CurrencyValues.KZT)
            }
    )
    @Test
    @ApiLogin
    void statWithDifferentCurrenciesTest(@Token String bearerToken) {
        final ApolloCall<StatQuery.Data> currenciesCall = apolloClient.query(StatQuery.builder()
                        .filterCurrency(null)
                        .statCurrency(null)
                        .filterPeriod(null)
                        .build())
                .addHttpHeader("authorization", bearerToken);

        final ApolloResponse<StatQuery.Data> response = Rx2Apollo.single(currenciesCall).blockingGet();
        final StatQuery.Data data = response.dataOrThrow();
        StatQuery.Stat result = data.stat;
        Assertions.assertEquals(
                5_140.0,
                result.total
        );
        Assertions.assertEquals(RUB, result.currency);
        Assertions.assertEquals(2, result.statByCategories.size());

        StatQuery.StatByCategory cats = result.statByCategories.getFirst();
        Assertions.assertEquals("RUB", cats.currency.rawValue);
        Assertions.assertEquals("Машина", cats.categoryName);
        Assertions.assertEquals(5_000, cats.sum);
        Assertions.assertEquals(getCurrentDateWithoutTime(), cats.firstSpendDate);
        Assertions.assertEquals(getCurrentDateWithoutTime(), cats.lastSpendDate);

        StatQuery.StatByCategory archived = result.statByCategories.getLast();
        Assertions.assertEquals("RUB", archived.currency.rawValue);
        Assertions.assertEquals("Еда", archived.categoryName);
        Assertions.assertEquals(140.0, archived.sum);
        Assertions.assertEquals(getCurrentDateWithoutTime(), archived.firstSpendDate);
        Assertions.assertEquals(getCurrentDateWithoutTime(), archived.lastSpendDate);
    }

    @User(
            categories = {
                    @Category(name = "Машина"),
                    @Category(name = "Еда")
            },
            spendings = {
                    @Spending(category = "Машина", description = "ТО-0", amount = 5_000.0),
                    @Spending(category = "Еда", description = "Кумыс", amount = 1_000.0, currency = CurrencyValues.KZT)
            }
    )
    @Test
    @ApiLogin
    void filteredStatByCurrencyTest(@Token String bearerToken) {
        final ApolloCall<StatQuery.Data> onlyTengeStat = apolloClient.query(StatQuery.builder()
                        .filterCurrency(guru.qa.type.CurrencyValues.KZT)
                        .statCurrency(guru.qa.type.CurrencyValues.KZT)
                        .filterPeriod(null)
                        .build())
                .addHttpHeader("authorization", bearerToken);

        final ApolloResponse<StatQuery.Data> response = Rx2Apollo.single(onlyTengeStat).blockingGet();
        final StatQuery.Data data = response.dataOrThrow();
        StatQuery.Stat result = data.stat;

        Assertions.assertEquals(1_000.0, result.total);
        Assertions.assertEquals("KZT", result.currency.rawValue);
        Assertions.assertEquals(1, result.statByCategories.size());
        Assertions.assertEquals("Еда", result.statByCategories.getFirst().categoryName);
    }

    private Date getCurrentDateWithoutTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

}
