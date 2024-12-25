package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class LoginPage {
  private final SelenideElement usernameInput = $("input[name='username']");
  private final SelenideElement passwordInput = $("input[name='password']");
  private final SelenideElement submitButton = $("button[type='submit']");
  private final SelenideElement createNewAccountButton = $("a.form__register");
  private final SelenideElement errorMessage = $(".form__error-container");

  public MainPage login(String username, String password) {
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    submitButton.click();
    return new MainPage();
  }

  public RegisterPage createNewAccountBtnClick() {
    createNewAccountButton.click();
    webdriver().shouldHave(urlContaining("/register"));
    return new RegisterPage();
  }

  public LoginPage checkErrorMessage(String message) {
    errorMessage.shouldHave(text(message));
    return this;
  }
}
