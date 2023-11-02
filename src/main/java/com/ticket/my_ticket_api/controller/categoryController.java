package com.ticket.my_ticket_api.controller;

import com.ticket.my_ticket_api.entity.Category;
import com.ticket.my_ticket_api.payload.request.CategoryRequest;
import com.ticket.my_ticket_api.payload.response.MessageResponse;
import com.ticket.my_ticket_api.service.categoryService.CategoryService;
import com.ticket.my_ticket_api.service.categoryService.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/category")
public class categoryController {
    @Autowired
    private final CategoryService categoryService = new CategoryServiceImpl();

    @PostMapping("")
    public ResponseEntity<?> createCategory(@Validated  @RequestBody CategoryRequest categoryRequest, @RequestHeader (name="Authorization") String token) {
        if (categoryService.categoryExist(categoryRequest.getName()) == HttpStatus.OK) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Category already exist!"));
        }

        Category category = Category
                .builder()
                .name(categoryRequest.getName())
                .create_at(new Date())
                .update_at(new Date())
                .build();

        HttpStatus response = categoryService.createCategory(category);

        if (response != HttpStatus.CREATED) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Error please retry!"));
        }

        return ResponseEntity.ok(category);
    }

    @GetMapping("")
    public ResponseEntity<?> getAllEvents() {
        return categoryService.getAllCategories();
    }
}
