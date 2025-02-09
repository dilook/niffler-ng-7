package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement submitPasswordInput = $("#passwordSubmit");
    private final SelenideElement signUpButton = $("button[type='submit']");
    private final SelenideElement signInButton = $("a.form_sign-in");
    private final SelenideElement successText = $("p.form__paragraph");
    private final SelenideElement errorText = $(".form__error");

    public RegisterPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    public RegisterPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    public RegisterPage setPasswordSubmit(String password) {
        submitPasswordInput.setValue(password);
        return this;
    }

    public RegisterPage submitRegistration() {
        signUpButton.click();
        return this;
    }

    public LoginPage signInClick() {
        signInButton.click();
        return new LoginPage();
    }

    public RegisterPage checkSuccessRegistrationMessage(String message) {
        successText.shouldHave(text(message)).shouldBe(visible);
        return this;
    }

    public RegisterPage signUp(String username, String password) {
        setUsername(username);
        setPassword(password);
        setPasswordSubmit(password);
        submitRegistration();
        return this;
    }

    public RegisterPage checkErrorMessage(String errorMessage) {
        errorText.shouldHave(text(errorMessage)).shouldBe(visible);
        return this;
    }

}
