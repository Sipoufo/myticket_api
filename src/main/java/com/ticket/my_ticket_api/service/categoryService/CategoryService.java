package com.ticket.my_ticket_api.service.categoryService;

import com.ticket.my_ticket_api.entity.Category;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    public HttpStatus createCategory(Category category);
    public ResponseEntity<?> getAllCategories();
    public Optional<Category> getOneCategory(long categoryId);
    public HttpStatus updateSCategory(Category category, long categoryId);
    public HttpStatus deleteCategory(long categoryId);
    public HttpStatus categoryExist(String categoryName);
}
