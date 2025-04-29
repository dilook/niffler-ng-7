package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.component.Calendar;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.attributeMatching;
import static com.codeborne.selenide.Condition.disabled;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class ProfilePage extends BasePage<ProfilePage> {

  public static String url = Config.getInstance().frontUrl() + "profile";

  private final SelenideElement avatar = $("#image__input").parent().$("img");
  private final SelenideElement userName = $("#username");
  private final SelenideElement nameInput = $("#name");
  private final SelenideElement photoInput = $("input[type='file']");
  private final SelenideElement submitButton = $("button[type='submit']");
  private final SelenideElement categoryInput = $("input[name='category']");
  private final SelenideElement archivedSwitcher = $(".MuiSwitch-input");
  private final ElementsCollection bubbles = $$(".MuiChip-filled.MuiChip-colorPrimary");
  private final ElementsCollection bubblesArchived = $$(".MuiChip-filled.MuiChip-colorDefault");

  private final Calendar calendar = new Calendar($(".ProfileCalendar"));

  private final SelenideElement alert = $(".MuiSnackbar-root");

  @Nonnull
  @Step("Set username {0}")
  public ProfilePage setName(String name) {
    nameInput.clear();
    nameInput.setValue(name);
    return this;
  }

  @Nonnull
  @Step("Upload photo")
  public ProfilePage uploadPhotoFromClasspath(String path) {
    photoInput.uploadFromClasspath(path);
    return this;
  }

  @Nonnull
  @Step("Add category {0}")
  public ProfilePage addCategory(String category) {
    categoryInput.setValue(category).pressEnter();
    return this;
  }

  @Nonnull
  @Step("Check that category {0} exists")
  public ProfilePage checkCategoryExists(String category) {
    bubbles.find(text(category)).shouldBe(visible);
    return this;
  }

  @Nonnull
  @Step("Check that category {0} exists in archived section")
  public ProfilePage checkArchivedCategoryExists(String category) {
    archivedSwitcher.click();
    bubblesArchived.find(text(category)).shouldBe(visible);
    return this;
  }

  @Nonnull
  @Step("Check username")
  public ProfilePage checkUsername(String username) {
    this.userName.should(value(username));
    return this;
  }

  @Nonnull
  @Step("Check name")
  public ProfilePage checkName(String name) {
    nameInput.shouldHave(value(name));
    return this;
  }

  @Nonnull
  @Step("Check that photo exists")
  public ProfilePage checkPhotoExist() {
    avatar.should(attributeMatching("src", "data:image.*"));
    return this;
  }

  @Nonnull
  @Step("Check that category input is disabled")
  public ProfilePage checkThatCategoryInputDisabled() {
    categoryInput.should(disabled);
    return this;
  }

  @Nonnull
  @Step("Save profile changes")
  public ProfilePage submitProfile() {
    submitButton.click();
    return this;
  }

  @Nonnull
  @Step("Check alert {0}")
  public ProfilePage checkAlert(String message) {
    alert.shouldHave(text(message));
    return this;
  }
}
