package com.example.eda.controllers;

import com.example.eda.domain.category.Category;
import com.example.eda.domain.category.CategoryDTO;
import com.example.eda.domain.product.Product;
import com.example.eda.domain.product.ProductDTO;
import com.example.eda.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody ProductDTO body){
        Product product = this.productService.save(body);
        return ResponseEntity.ok().body(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> create(@PathVariable String id, @RequestBody ProductDTO body){
        Product product = this.productService.edit(id, body);
        return ResponseEntity.ok().body(product);
    }

    @DeleteMapping("/{ownerId}/{id}")
    public ResponseEntity<Void> delete(@PathVariable String ownerId, @PathVariable String id){
        this.productService.delete(ownerId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAll(){
        List<Product> products = this.productService.getAll();
        return ResponseEntity.ok().body(products);
    }

}
