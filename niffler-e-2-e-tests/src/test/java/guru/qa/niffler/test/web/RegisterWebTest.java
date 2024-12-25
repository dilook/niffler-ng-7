package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.UserData;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class RegisterWebTest {

    private static final Config CFG = Config.getInstance();

    @Test
    public void shouldRegisterNewUser() {
        UserData.User randomUser = UserData.generateUser();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .createNewAccountBtnClick()
                .setUsername(randomUser.name())
                .setPassword(randomUser.password())
                .setPasswordSubmit(randomUser.password())
                .submitRegistration()
                .checkSuccessRegistrationMessage("Congratulations! You've registered!");
    }


    @Test
    public void shouldNotRegisterUserWithExistingUsername() {
        UserData.User randomUser = UserData.generateUser();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .createNewAccountBtnClick()
                .signUp(randomUser.name(), randomUser.password());
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .createNewAccountBtnClick()
                .signUp(randomUser.name(), randomUser.password())
                .checkErrorMessage("Username `" + randomUser.name() + "` already exists");
    }

    @Test
    public void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .createNewAccountBtnClick()
                .setUsername("static")
                .setPassword("123")
                .setPasswordSubmit("321")
                .submitRegistration()
                .checkErrorMessage("Passwords should be equal");
    }

    @ParameterizedTest(name = "Password = {0}")
    @ValueSource(strings = {"1", "1234567891012"})
    public void shouldShowErrorIfPasswordLessThan3Characters(String password) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .createNewAccountBtnClick()
                .setUsername("static")
                .setPassword(password)
                .setPasswordSubmit(password)
                .submitRegistration()
                .checkErrorMessage("Allowed password length should be from 3 to 12 characters");
    }
}
