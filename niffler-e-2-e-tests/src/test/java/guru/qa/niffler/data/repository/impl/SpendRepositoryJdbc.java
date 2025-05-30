package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;

import java.util.Optional;
import java.util.UUID;

public class SpendRepositoryJdbc implements SpendRepository {
    SpendDao spendDao = new SpendDaoJdbc();
    CategoryDao categoryDao = new CategoryDaoJdbc();

    @Override
    public SpendEntity create(SpendEntity spend) {
        final UUID categoryId = spend.getCategory().getId();
        if (categoryId == null || categoryDao.findById(categoryId).isEmpty()) {
            spend.setCategory(
                    categoryDao.create(spend.getCategory())
            );
        }
        return spendDao.create(spend);
    }

    @Override
    public SpendEntity update(SpendEntity spend) {
        spendDao.update(spend);
        categoryDao.update(spend.getCategory());
        return spend;
    }

    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        return categoryDao.create(category);
    }

    @Override
    public Optional<CategoryEntity> findCategoryById(UUID id) {
        return categoryDao.findById(id);
    }

    @Override
    public Optional<CategoryEntity> findCategoryByUsernameAndSpendName(String username, String name) {
        return categoryDao.findCategoryByUsernameAndSpendName(username, name);
    }

    @Override
    public Optional<SpendEntity> findSpendById(UUID id) {
        return spendDao.findById(id);
    }

    @Override
    public Optional<SpendEntity> findSpendByUsernameAndSpendDescription(String username, String description) {
        return spendDao.findSpendByUsernameAndSpendDescription(username, description);
    }

    @Override
    public void remove(SpendEntity spend) {
        spendDao.remove(spend);
    }

    @Override
    public void removeCategory(CategoryEntity category) {
        categoryDao.remove(category);
    }

    @Override
    public CategoryEntity updateCategory(CategoryEntity category) {
        categoryDao.update(category);
        return category;
    }
}
