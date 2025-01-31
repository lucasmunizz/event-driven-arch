package com.example.eda.service;

import com.example.eda.domain.category.Category;
import com.example.eda.domain.category.CategoryDTO;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {

    @Autowired
    private DynamoDbTemplate dynamoDbTemplate;

    public Category save(CategoryDTO dto){
        Category category = new Category();

        category.setId(UUID.randomUUID().toString());
        category.setDescription(dto.description());
        category.setTitle(dto.title());
        category.setOwnerId(dto.owner_id());
        category.setCreatedAt(Instant.now());

        dynamoDbTemplate.save(category);

        return category;

    }

    public Category edit(String id, CategoryDTO dto){
        var key = Key.builder()
                .partitionValue(id)
                .build();

        var category = dynamoDbTemplate.load(key, Category.class);

        if (category == null){
            throw new RuntimeException("Category Not Found");
        }

        category.setDescription(dto.description());
        category.setTitle(dto.title());
        category.setOwnerId(dto.owner_id());

        dynamoDbTemplate.save(category);

        return category;
    }

    public List<Category> getAll(){
        return dynamoDbTemplate.scanAll(Category.class).items().stream().toList();
    }

    public void delete(String id){
        var key = Key.builder()
                .partitionValue(id)
                .build();

        var category = dynamoDbTemplate.load(key, Category.class);

        if (category == null){
            throw new RuntimeException("Category not found");
        }

        dynamoDbTemplate.delete(category);
    }


}
