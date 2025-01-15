package guru.qa.niffler.test.web;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.userdata.UserdataUserEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UserDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Date;

@Disabled
public class JdbcTest {

    @Test
    void txTest() {
        SpendDbClient spendDbClient = new SpendDbClient();

        SpendJson spend = spendDbClient.createSpend(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                "cat-name-tx-2",
                                "duck",
                                false
                        ),
                        CurrencyValues.RUB,
                        1000.0,
                        "spend-name-tx",
                        null
                )
        );

        System.out.println(spend);
    }

    @Test
    void xaTest() {
        UserDbClient userDbClient = new UserDbClient();

        UserdataUserEntity user = userDbClient.createUser(
                new AuthUserEntity(
                        null,
                        "xattr1",
                        "12345",
                        true,
                        true,
                        true,
                        true
                )
                ,
                new UserdataUserEntity(
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null,
                        RandomDataUtils.randomName(),
                        null,
                        null
                )
        );
        System.out.println(user);
    }
}
