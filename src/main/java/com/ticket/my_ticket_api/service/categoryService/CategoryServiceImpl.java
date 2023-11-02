package com.ticket.my_ticket_api.service.categoryService;

import com.ticket.my_ticket_api.entity.Category;
import com.ticket.my_ticket_api.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public HttpStatus createCategory(Category category) {
        if (category.getName().length() < 3) {
            return HttpStatus.NOT_IMPLEMENTED;
        }
        categoryRepository.save(category);
        return HttpStatus.CREATED;
    }

    @Override
    public ResponseEntity<?> getAllCategories() {
        return ResponseEntity.ok(categoryRepository.findAll());
    }

    @Override
    public Optional<Category> getOneCategory(long categoryId) {
        return categoryRepository.findById(categoryId);
    }

    @Override
    public HttpStatus updateSCategory(Category category, long categoryId) {
        Optional<Category> category1 = categoryRepository.findById(categoryId);

        if (category1.isEmpty()) {
            return HttpStatus.NOT_FOUND;
        }

        category1.get().setName(category.getName());
        categoryRepository.save(category1.get());
        return HttpStatus.OK;
    }

    @Override
    public HttpStatus deleteCategory(long categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);

        if (category.isEmpty()) {
            return HttpStatus.NOT_FOUND;
        }

        categoryRepository.deleteById(categoryId);
        return HttpStatus.OK;
    }

    @Override
    public HttpStatus categoryExist(String categoryName) {
        Optional<Category> category = categoryRepository.findByName(categoryName);

        if (category.isPresent()) {
            return HttpStatus.OK;
        } else {
            return HttpStatus.NOT_FOUND;
        }
    }
}
