package guru.qa.niffler.test.web;

import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.UdUserRepository;
import guru.qa.niffler.data.repository.impl.UdUserRepositoryJdbc;
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
import java.util.Optional;
import java.util.UUID;

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
                "cat-name-tx-3",
                "duck",
                false
            ),
            CurrencyValues.RUB,
            1000.0,
            "spend-name-tx-3",
            "duck"
        )
    );

    System.out.println(spend);
  }

  @Test
  void springJdbcTest() {
    UsersDbClient usersDbClient = new UsersDbClient();
    UserJson user = usersDbClient.createUser(
        new UserJson(
            null,
            "valentin-1",
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
  void findAllTest() {
    UdUserRepository udUserRepositoryJdbc = new UdUserRepositoryJdbc();

    List<UserEntity> all = udUserRepositoryJdbc.findAll();
    all.forEach(System.out::println);
  }

  @Disabled
  @Test
  void testCreateUdUser() {
     UdUserRepository udUserRepository = new UdUserRepositoryJdbc();

    Optional<UserEntity> addressee = udUserRepository.findById(UUID.fromString("80d7baba-57ca-4f80-bc6a-0047d6f07713"));


    UserEntity user = new UserEntity();
    user.setUsername("valentin-3");
    user.setCurrency(CurrencyValues.RUB);
    user.addFriends(FriendshipStatus.PENDING, addressee.get());
    udUserRepository.create(user);

  }
}
