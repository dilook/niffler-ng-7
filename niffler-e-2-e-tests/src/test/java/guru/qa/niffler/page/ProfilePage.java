package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$x;

public class ProfilePage {

    private final SelenideElement showArchivedToggle = $("span.MuiSwitch-switchBase");
    private final ElementsCollection activeCategoryRows = $$x("//button[contains(@aria-label,'rchive category')]/../..");
    private final ElementsCollection archiveCategoryRows = $$x("//button[@aria-label='Unarchive category']/../..");


    public ProfilePage showArchivedCategories(){
        showArchivedToggle.click();
        showArchivedToggle.shouldHave(cssClass("Mui-checked"));
        return this;
    }


    public ProfilePage checkThatActiveCategoriesContains(String category){
        activeCategoryRows.find(text(category)).shouldBe(visible);
        return this;
    }

    public ProfilePage checkThatArchiveCategoriesContains(String category){
        archiveCategoryRows.find(text(category)).shouldBe(visible);
        return this;
    }


}
