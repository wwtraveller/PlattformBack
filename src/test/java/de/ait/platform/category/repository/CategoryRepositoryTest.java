package de.ait.platform.category.repository;

import de.ait.platform.category.entity.Category;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void CategoryRepository_FindByName_ReturnCategory() {
        Category category = Category
                .builder()
                .name("Category").build();
        Category finance = Category.builder()
                .name("Finance")
                .build();

        categoryRepository.save(finance);
        categoryRepository.save(category);

        Category categories = categoryRepository.findByName(finance.getName()).getFirst();
        Assertions.assertThat(categories).isNotNull();
    }

    @Test
    public void CategoryRepository_Save_ReturnSavedCategory() {
        //Arrange
        Category category = Category
                .builder()
                .name("Category").build();
        //Act
        Category savedCategory = categoryRepository.save(category);
        //Assert
        Assertions.assertThat(savedCategory).isNotNull();
        Assertions.assertThat(savedCategory.getId()).isGreaterThan(0);
    }

    @Test
    public void CategoryRepository_FindAll_ReturnListCategories() {
        Category category = Category
                .builder()
                .name("Category").build();
        Category finance = Category.builder()
                .name("Finance")
                .build();

        categoryRepository.save(finance);
        categoryRepository.save(category);

        List<Category> categories = categoryRepository.findAll();
        Assertions.assertThat(categories).isNotNull();
        Assertions.assertThat(categories.size()).isEqualTo(2);   //example
    }

    @Test
    public void CategoryRepository_FindById_ReturnCategory() {
        Category category = Category
                .builder()
                .name("Category").build();
        Category finance = Category.builder()
                .name("Finance")
                .build();

        categoryRepository.save(finance);
        categoryRepository.save(category);

        Category categories = categoryRepository.findById(finance.getId()).get();
        Assertions.assertThat(categories).isNotNull();
    }

    @Test
    public void CategoryRepository_UpdateCategory_ReturnUpdatedCategory() {
        Category category = Category
                .builder()
                .name("Category").build();
        Category finance = Category.builder()
                .name("Finance")
                .build();

        categoryRepository.save(finance);
        categoryRepository.save(category);

        Category categories = categoryRepository.findById(finance.getId()).get();
        categories.setName("Updated Category");
        Category savedCategory = categoryRepository.save(categories);
        Assertions.assertThat(savedCategory.getName()).isNotNull();
    }
    @Test
    public void CategoryRepository_DeleteCategory_ReturnCategoryIsEmpty() {

        Category finance = Category.builder()
                .name("Finance")
                .build();
        categoryRepository.save(finance);
        categoryRepository.deleteById(finance.getId());
     Optional<Category> returnCategory = categoryRepository.findById(finance.getId());
        Assertions.assertThat(returnCategory).isEmpty();
    }
}
