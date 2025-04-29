package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class RegisterPage extends BasePage<RegisterPage> {

  private final SelenideElement usernameInput = $("input[name='username']");
  private final SelenideElement passwordInput = $("input[name='password']");
  private final SelenideElement passwordSubmitInput = $("input[name='passwordSubmit']");
  private final SelenideElement submitButton = $("button[type='submit']");
  private final SelenideElement proceedLoginButton = $(".form_sign-in");
  private final SelenideElement errorContainer = $(".form__error");

  @Nonnull
  @Step("Fill register page fields")
  public RegisterPage fillRegisterPage(String login, String password, String passwordSubmit) {
    usernameInput.setValue(login);
    passwordInput.setValue(password);
    passwordSubmitInput.setValue(passwordSubmit);
    return this;
  }

  @Nonnull
  @Step("Success submit")
  public LoginPage successSubmit() {
    submit();
    proceedLoginButton.click();
    return new LoginPage();
  }

  @Step("Submit register page")
  public void submit() {
    submitButton.click();
  }

  @Nonnull
  @Step("Check error message {0}")
  public RegisterPage checkAlertMessage(String errorMessage) {
    errorContainer.shouldHave(text(errorMessage));
    return this;
  }
}
