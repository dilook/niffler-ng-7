package guru.qa.niffler.service;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface SpendClient {
  @Nonnull
  @Step("Create spend {0}")
  SpendJson createSpend(SpendJson spend);

  @Nonnull
  @Step("Create category {0}")
  CategoryJson createCategory(CategoryJson category);

  @Nullable
  @Step("Find category by username {0} and name {1}")
  CategoryJson findCategoryByUsernameAndName(String username, String name);

  @Step("Remove category {0}")
  void removeCategory(CategoryJson category);
}
