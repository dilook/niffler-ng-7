package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.UserData;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

public class LoginWebTest {

    private static final Config CFG = Config.getInstance();

    @Test
    public void mainPageShouldBeDisplayedAfterSuccessLogin() {
        UserData.User randomUser = UserData.generateUser();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .createNewAccountBtnClick()
                .signUp(randomUser.name(), randomUser.password())
                .signInClick()
                .login(randomUser.name(), randomUser.password())
                .checkMainPageIsLoaded();
    }

    @Test
    public void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        UserData.User randomUser = UserData.generateUser();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(randomUser.name(), randomUser.password());
        new LoginPage().checkErrorMessage("Неверные учетные данные пользователя");
    }
}
