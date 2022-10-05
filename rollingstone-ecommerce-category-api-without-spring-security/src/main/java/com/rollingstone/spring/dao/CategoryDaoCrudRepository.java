package com.rollingstone.spring.dao;

import com.rollingstone.spring.model.Category;

import org.springframework.data.repository.CrudRepository;

public interface CategoryDaoCrudRepository  extends CrudRepository<Category, Long> {

}
