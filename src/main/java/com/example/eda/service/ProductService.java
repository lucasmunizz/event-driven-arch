package com.example.eda.service;

import com.example.eda.domain.category.Category;
import com.example.eda.domain.category.CategoryDTO;
import com.example.eda.domain.product.Product;
import com.example.eda.domain.product.ProductDTO;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private DynamoDbTemplate dynamoDbTemplate;

    public Product create(ProductDTO dto){
        Product product = new Product();
        product.setTitle(dto.title());
        product.setDescription(dto.description());
        product.setOwnerId(dto.ownerId());
        product.setId(UUID.randomUUID().toString());
        product.setCreatedAt(Instant.now());
        product.setCategoryId(dto.categoryId());
        product.setPrice(dto.price());

        dynamoDbTemplate.save(product);

        return product;
    }

    public Product save(ProductDTO dto){

        var categoryKey = Key.builder()
                .partitionValue(dto.ownerId())
                .sortValue(dto.categoryId())
                .build();

        var category = dynamoDbTemplate.load(categoryKey, Category.class);

        if (category == null) {
            throw new RuntimeException("Category not found");
        }

        Product product = new Product();

        product.setId(UUID.randomUUID().toString());
        product.setDescription(dto.description());
        product.setTitle(dto.title());
        product.setOwnerId(dto.ownerId());
        product.setCreatedAt(Instant.now());
        product.setCategoryId(dto.categoryId());
        product.setPrice(dto.price());

        dynamoDbTemplate.save(product);

        return product;

    }


    public Product edit(String id, ProductDTO dto){
        var key = Key.builder()
                .partitionValue(dto.ownerId())
                .sortValue(id)
                .build();

        var product = dynamoDbTemplate.load(key, Product.class);

        if (product == null){
            throw new RuntimeException("Product Not Found");
        }

        var categoryKey = Key.builder()
                        .partitionValue(dto.ownerId())
                                .sortValue(dto.categoryId())
                                        .build();

        var category = dynamoDbTemplate.load(categoryKey, Category.class);

        if (category == null) {
            throw new RuntimeException("Category not found");
        }

        product.setDescription(dto.description());
        product.setTitle(dto.title());
        product.setOwnerId(dto.ownerId());
        product.setCategoryId(dto.categoryId());
        product.setPrice(dto.price());

        dynamoDbTemplate.save(product);

        return product;
    }

    public List<Product> getAll(){
        return dynamoDbTemplate.scanAll(Product.class).items().stream().toList();
    }

    public void delete(String ownerId, String id){
        var key = Key.builder()
                .partitionValue(ownerId)
                .sortValue(id)
                .build();

        var product = dynamoDbTemplate.load(key, Product.class);

        if (product == null){
            throw new RuntimeException("Product not found");
        }

        dynamoDbTemplate.delete(product);
    }

}
