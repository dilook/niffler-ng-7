package guru.qa.niffler.test.grpc;

import com.google.protobuf.Empty;
import guru.qa.niffler.grpc.CalculateRequest;
import guru.qa.niffler.grpc.CalculateResponse;
import guru.qa.niffler.grpc.Currency;
import guru.qa.niffler.grpc.CurrencyResponse;
import guru.qa.niffler.grpc.CurrencyValues;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

public class CurrencyGrpcTest extends BaseGrpcTest {

    @Test
    void allCurrenciesShouldReturned() {
        final List<Currency> expectedCurrenciesList = List.of(
                Currency.newBuilder().setCurrency(CurrencyValues.RUB).setCurrencyRate(0.015).build(),
                Currency.newBuilder().setCurrency(CurrencyValues.KZT).setCurrencyRate(0.0021).build(),
                Currency.newBuilder().setCurrency(CurrencyValues.EUR).setCurrencyRate(1.08).build(),
                Currency.newBuilder().setCurrency(CurrencyValues.USD).setCurrencyRate(1.0).build()
        );
        final CurrencyResponse response = blockingStub.getAllCurrencies(Empty.getDefaultInstance());
        final List<Currency> allCurrenciesList = response.getAllCurrenciesList();
        Assertions.assertEquals(4, allCurrenciesList.size());
        Assertions.assertEquals(expectedCurrenciesList, allCurrenciesList);
    }

    static Stream<Arguments> spendCurrencyShouldBeConverted() {
        return Stream.of(
                Arguments.of(150.50, CurrencyValues.RUB, CurrencyValues.KZT, 1_075.0),
                Arguments.of(34.00, CurrencyValues.USD, CurrencyValues.EUR, 31.48),
                Arguments.of(100.60, CurrencyValues.USD, CurrencyValues.USD, 100.60),
                Arguments.of(2_500.00, CurrencyValues.RUB, CurrencyValues.RUB, 2_500.00),
                Arguments.of(0.00, CurrencyValues.KZT, CurrencyValues.RUB, 0.00)
        );
    }

    @ParameterizedTest(name = "spend {0} {1} should be converted to {3} {2}")
    @MethodSource
    void spendCurrencyShouldBeConverted(double spend,
                                        CurrencyValues spendCurrency,
                                        CurrencyValues desiredCurrency,
                                        double expectedResult) {
        final CalculateResponse response = blockingStub.calculateRate(
                CalculateRequest.newBuilder()
                        .setSpendCurrency(spendCurrency)
                        .setAmount(spend)
                        .setDesiredCurrency(desiredCurrency)
                        .build()
        );
        final double calculateAmount = response.getCalculatedAmount();
        Assertions.assertEquals(expectedResult, calculateAmount, 0.01);
    }

}
