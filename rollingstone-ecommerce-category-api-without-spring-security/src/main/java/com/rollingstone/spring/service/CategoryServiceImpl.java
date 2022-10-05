package com.rollingstone.spring.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.rollingstone.exceptions.HTTP400Exception;
import com.rollingstone.exceptions.HTTP404Exception;
import com.rollingstone.spring.dao.CategoryDaoCrudRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.rollingstone.spring.dao.CategoryDaoRepository;
import com.rollingstone.spring.model.Category;

@Service
public class CategoryServiceImpl implements CategoryService {

    final static Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryDaoRepository categoryDao;

    @Autowired
    private CategoryDaoCrudRepository categoryDaoCrudRepository;

    @Override
    public Category save(Category category) {
        try{
            return categoryDao.save(category);
        }
        catch (Exception e)
        {
            throw new HTTP400Exception(e.getMessage());
        }
    }

    @Override
    public Optional<Category> get(long id) {
        try{
            logger.info("inside category get!");
            Optional<Category> categoryOptinoal = categoryDao.findById(id);

            if (categoryOptinoal.isPresent()){
                return categoryOptinoal;
            }else {
                throw new HTTP404Exception("Category Not Found");
            }
        }  catch (Exception e)
        {
            logger.info("inside category get ex!");

            throw new HTTP404Exception(e.getMessage());
        }
    }

    @Override
    public Page<Category> getCategorysByPage(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("categoryName").descending());
        return categoryDao.findAll(pageable);
    }

    @Override
    public List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        Iterable<Category> categoryIterable = categoryDaoCrudRepository.findAll();
        categoryIterable.forEach(categories::add);
        return categories;
    }

    @Override
    public void update(long id, Category category) {
        categoryDao.save(category);
    }


    @Override
    public void delete(long id) {
        categoryDao.deleteById(id);
    }

}