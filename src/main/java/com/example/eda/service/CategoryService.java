package com.example.eda.service;

import com.example.eda.domain.category.Category;
import com.example.eda.domain.category.CategoryDTO;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
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


}
