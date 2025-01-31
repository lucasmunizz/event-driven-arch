package com.example.eda.controllers;

import com.example.eda.domain.category.Category;
import com.example.eda.domain.category.CategoryDTO;
import com.example.eda.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Category> create(@RequestBody CategoryDTO body){
        Category category = this.categoryService.save(body);
        return ResponseEntity.ok().body(category);
    }

}
