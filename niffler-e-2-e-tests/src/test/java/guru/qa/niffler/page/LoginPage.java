package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import kotlin.jvm.Strictfp;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class LoginPage {

  private final SelenideElement usernameInput = $("input[name='username']");
  private final SelenideElement passwordInput = $("input[name='password']");
  private final SelenideElement submitButton = $("button[type='submit']");
  private final SelenideElement registerButton = $("a[href='/register']");
  private final SelenideElement errorContainer = $(".form__error");

  @Nonnull
  @Step("Register")
  public RegisterPage doRegister() {
    registerButton.click();
    return new RegisterPage();
  }

  @Nonnull
  @Step("Success login")
  public MainPage successLogin(String username, String password) {
    login(username, password);
    return new MainPage();
  }

  @Step("Login with username")
  public void login(String username, String password) {
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    submitButton.click();
  }

  @Nonnull
  @Step("Check error message '{error}'")
  public LoginPage checkError(String error) {
    errorContainer.shouldHave(text(error));
    return this;
  }
}
