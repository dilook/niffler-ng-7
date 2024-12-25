package guru.qa.niffler.data;

import com.github.javafaker.Faker;

public class UserData {
    public record User(String name, String password) {
    }

    private static final Faker FAKER = new Faker();

    public static User generateUser() {
        return new User(FAKER.name().username(), FAKER.internet().password(3, 12));
    }
}
