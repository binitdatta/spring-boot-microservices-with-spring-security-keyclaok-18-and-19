package com.rollingstone.spring.dao;

import com.rollingstone.spring.model.Product;

import org.springframework.data.repository.CrudRepository;

public interface ProductCrudDao  extends CrudRepository<Product, Long>{
}


