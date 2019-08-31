package com.upgrad.FoodOrderingApp.service.business;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Transactional
    public CategoryEntity getCategoryUsingId(final Long categoryId) {

        System.out.println("\n\t ====> CategoryService.getCategoryUsingId() called");

        return categoryDao.getCategoryUsingId(categoryId);
    }

    @Transactional
    public CategoryEntity getCategoryUsingUuid(final String categoryUuid) throws CategoryNotFoundException {

        System.out.println("\n\t ====> CategoryService.getCategoryUsingUuid() called");

        if(categoryUuid.length() == 0) {
            throw new CategoryNotFoundException("CNF-001", "Category id field should not be empty");
        }

        CategoryEntity categoryEntity =  categoryDao.getCategoryUsingUuid(categoryUuid);
        if(categoryEntity == null) {
            throw new CategoryNotFoundException("CNF-002", "No category by this id");
        }

        return categoryEntity;
    }

    @Transactional
    public List <CategoryEntity> getAllCategories() {

        System.out.println("\n\t ====> CategoryService.getAllCategories() called");

        return categoryDao.getAllCategories();
    }

}
