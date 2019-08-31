package com.upgrad.FoodOrderingApp.service.business;

import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantDao restaurantDao;

    @Transactional
    public List<RestaurantEntity> getRestaurantsByName(final String restaurantName) throws RestaurantNotFoundException {

        System.out.println("\n\t ====> RestaurantService.getRestaurantsByName() called");

        if(restaurantName.length() == 0) {
            throw new RestaurantNotFoundException("RNF-003", "Restaurant name field should not be empty");
        } else {
            return restaurantDao.getRestaurantsByName(restaurantName);
        }
    }

    @Transactional
    public RestaurantEntity getRestaurantById(final Long restaurantId) {

        System.out.println("\n\t ====> RestaurantService.getRestaurantById() called");

        return restaurantDao.getRestaurantById(restaurantId);
    }

    @Transactional
    public RestaurantEntity getRestaurantByUuid(final String restaurantUuid) throws RestaurantNotFoundException {

        System.out.println("\n\t ====> RestaurantService.getRestaurantByUuid() called");

        if(restaurantUuid.length() == 0) {
            throw new RestaurantNotFoundException("RNF-002", "Restaurant id field should not be empty");
        }

        RestaurantEntity restaurantEntity = restaurantDao.getRestaurantByUuid(restaurantUuid);
        if(restaurantEntity == null) {
            throw new RestaurantNotFoundException("RNF-001", "No restaurant by this id");
        }

        return restaurantEntity;
    }

}
