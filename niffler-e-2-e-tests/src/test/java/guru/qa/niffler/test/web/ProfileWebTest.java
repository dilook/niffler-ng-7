package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

public class ProfileWebTest {
    private static final Config CFG = Config.getInstance();

    @Category(
            username = "den",
            archived = true
    )
    @Test
    public void archivedCategoryShouldPresentInCategoryList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("den", "123")
                .goToProfile()
                .showArchivedCategories()
                .checkThatArchiveCategoriesContains(category.name());
    }

    @Category(
            username = "den",
            archived = false
    )
    @Test
    public void activeCategoryShouldPresentInCategoryList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("den", "123")
                .goToProfile()
                .checkThatActiveCategoriesContains(category.name());
    }
}
