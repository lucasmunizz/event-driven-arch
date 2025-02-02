package com.example.eda.controllers;

import com.example.eda.domain.category.Category;
import com.example.eda.domain.category.CategoryDTO;
import com.example.eda.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping("/{id}")
    public ResponseEntity<Category> create(@PathVariable String id, @RequestBody CategoryDTO body){
        Category category = this.categoryService.edit(id, body);
        return ResponseEntity.ok().body(category);
    }

    @DeleteMapping("/{ownerId}/{id}")
    public ResponseEntity<Void> delete(@PathVariable String ownerId, @PathVariable String id){
        this.categoryService.delete(ownerId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAll(){
        List<Category> categories = this.categoryService.getAll();
        return ResponseEntity.ok().body(categories);
    }

}
