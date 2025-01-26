package guru.qa.niffler.test.web;

import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.UdUserDaoJdbcWithError;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UsersDbClient;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled
public class JdbcTest {

    UsersDbClient usersDbClient = new UsersDbClient();

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
    void chainedJdbcTest() {
        AuthUserDao authUserDaoJdbc = new AuthUserDaoJdbc();
        UdUserDaoJdbcWithError udUserDaoJdbc = new UdUserDaoJdbcWithError();
        List<AuthUserEntity> authUsers;

        try {
            UserJson user = usersDbClient.createUserWithChained(
                    new UserJson(
                            null,
                            "valentin-5",
                            null,
                            null,
                            null,
                            CurrencyValues.RUB,
                            null,
                            null,
                            null
                    )
            );
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }

        authUsers = authUserDaoJdbc.findByUsername("valentin-5");
        assertEquals(1, authUsers.size(), "Первая транзакция должна выполниться и не откатиться");

        List<UserEntity> users = udUserDaoJdbc.findByUsername("valentin-5");
        assertEquals(0, users.size(), "Вторая транзакция не должна выполниться");
    }

    @Test
    void createUserJdbcWithoutTxTest() {
        UserJson user = usersDbClient.createUserJdbcWithoutTx(
                new UserJson(
                        null,
                        "jdbc-without-tx",
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null,
                        null
                )
        );
        System.out.println(user);
    }

    @Test
    void createUserJdbcWithXaTest() {
        UserJson user = usersDbClient.createUserJdbcWithXa(
                new UserJson(
                        null,
                        "jdbc-with-xa",
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null,
                        null
                )
        );
        System.out.println(user);
    }

    @Test
    void createUserSpringJdbcWithoutTxTest() {
        UserJson user = usersDbClient.createUserSpringJdbcWithoutTx(
                new UserJson(
                        null,
                        "jdbc-spring-without-tx",
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null,
                        null
                )
        );
        System.out.println(user);
    }

    @Test
    void createUserSpringJdbcWithXaTest() {
        UserJson user = usersDbClient.createUserSpringJdbcWithXa(
                new UserJson(
                        null,
                        "jdbc-spring-with-xa",
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null,
                        null
                )
        );
        System.out.println(user);
    }
}
