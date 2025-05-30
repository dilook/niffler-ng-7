package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.spend.CategoryEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryDao {
  CategoryEntity create(CategoryEntity category);

  Optional<CategoryEntity> findById(UUID id);

  List<CategoryEntity> findAll();

  Optional<CategoryEntity> findCategoryByUsernameAndSpendName(String username, String name);

  void remove(CategoryEntity category);

  void update(CategoryEntity category);
}
