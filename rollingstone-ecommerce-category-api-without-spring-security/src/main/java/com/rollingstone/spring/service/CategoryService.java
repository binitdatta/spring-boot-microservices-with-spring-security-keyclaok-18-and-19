package com.rollingstone.spring.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.rollingstone.spring.model.Category;

public interface CategoryService {

    Category save(Category category);
    Optional<Category> get(long id);
    Page<Category> getCategorysByPage(Integer pageNumber, Integer pageSize);
    List<Category> getCategories();
    void update(long id, Category category);
    void delete(long id);
}