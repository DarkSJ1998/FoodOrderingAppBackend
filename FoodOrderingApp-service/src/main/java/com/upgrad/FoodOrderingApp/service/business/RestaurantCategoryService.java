package com.upgrad.FoodOrderingApp.service.business;

import com.upgrad.FoodOrderingApp.service.dao.RestaurantCategoryDao;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class RestaurantCategoryService {

    @Autowired
    private RestaurantCategoryDao restaurantCategoryDao;

    @Transactional
    public List<RestaurantCategoryEntity> getCategoriesUsingRestaurantId(final Long restaurantId) {

        System.out.println("\n\t ====> RestaurantCategoryService.getCategoriesUsingRestaurantId() called");

        return restaurantCategoryDao.getCategoriesUsingRestaurantId(restaurantId);
    }

    @Transactional
    public List<RestaurantCategoryEntity> getRestaurantsUsingCategoryId(final Long categoryId) {

        System.out.println("\n\t ====> RestaurantCategoryService.getRestaurantsUsingCategoryId() called");

        return restaurantCategoryDao.getRestaurantsUsingCategoryId(categoryId);
    }

}
