package com.ticket.my_ticket_api.repository;

import com.ticket.my_ticket_api.entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    public void should_find_no_category () {
        List<Category> categories = categoryRepository.findAll();
        assertEquals(0, categories.size());
    }

    @Test
    public void should_find_all_categories () {
        Category category = Category
                .builder()
                .name("Programming")
                .build();
        categoryRepository.save(category);
        Category category_2 = Category
                .builder()
                .name("Swimming")
                .build();
        categoryRepository.save(category_2);

        List<Category> categories = categoryRepository.findAll();
        assertEquals(2, categories.size());
    }

    @Test
    public void should_create_category () {
        Category category = Category
                .builder()
                .name("Programming")
                .build();
        categoryRepository.save(category);
        assertNotNull(categoryRepository.save(category));
    }

    @Test
    public void should_find_category_by_id () {
        Category category = Category
                .builder()
                .name("Programming")
                .build();
        categoryRepository.save(category);
        assertEquals(categoryRepository.findById(category.getCategoryId()).get(), category);
    }

    @Test
    public void should_update_category_by_id() {
        Category category = Category
                .builder()
                .name("Programming")
                .build();
        categoryRepository.save(category);

        Category categoryFind = categoryRepository.findById(category.getCategoryId()).get();
        assertEquals(categoryFind, category);

         categoryFind.setName("Art");
         categoryRepository.save(categoryFind);

        assertEquals(1, categoryRepository.findAll().size());
         Category checkCategory = categoryRepository.findById(category.getCategoryId()).get();
        assertEquals(checkCategory.getName(), "Art");
    }

    @Test
    public void should_delete_category() {
        Category category = Category
                .builder()
                .name("Programming")
                .build();
        categoryRepository.save(category);

        assertEquals(1, categoryRepository.findAll().size());

        categoryRepository.deleteById(category.getCategoryId());

        assertEquals(0, categoryRepository.findAll().size());
    }

    @Test
    public void should_find_by_name() {
        Category category = Category
                .builder()
                .name("Programming")
                .build();
        categoryRepository.save(category);

        Category categoryFind = categoryRepository.findByName(category.getName()).get();
        assertNotNull(categoryFind);
    }
}