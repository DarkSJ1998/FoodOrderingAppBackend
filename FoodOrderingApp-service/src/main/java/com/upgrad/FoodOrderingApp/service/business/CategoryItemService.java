package com.upgrad.FoodOrderingApp.service.business;

import com.upgrad.FoodOrderingApp.service.dao.CategoryItemDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryItemEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CategoryItemService {

    @Autowired
    private CategoryItemDao categoryItemDao;

    @Transactional
    public List<CategoryItemEntity> getItemsUsingCategoryId(final long categoryId) {

        System.out.println("\n\t ====> CategoryItemService.getItemsUsingCategoryId() called");

        return categoryItemDao.getItemsUsingCategoryId(categoryId);
    }
}
