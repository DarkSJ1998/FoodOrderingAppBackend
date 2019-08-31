package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RestaurantCategoryDao {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    public List<RestaurantCategoryEntity> getCategoriesUsingRestaurantId(final Long restaurantId) {

        System.out.println("\n\t ======> RestaurantCategoryDao.getCategoriesUsingRestaurantId() called");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List <RestaurantCategoryEntity> list = new ArrayList<>();

        try {
            TypedQuery <RestaurantCategoryEntity> query = entityManager
                    .createQuery("SELECT r FROM RestaurantCategoryEntity r WHERE r.restaurantId = :restaurantId", RestaurantCategoryEntity.class);
            query.setParameter("restaurantId", restaurantId);
            list = query.getResultList();

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }

        entityManager.close();
        return list;
    }

    public List<RestaurantCategoryEntity> getRestaurantsUsingCategoryId(final Long categoryId) {

        System.out.println("\n\t ======> RestaurantCategoryDao.getRestaurantsUsingCategoryId() called");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List <RestaurantCategoryEntity> list = new ArrayList<>();

        try {
            TypedQuery <RestaurantCategoryEntity> query = entityManager
                    .createQuery("SELECT r FROM RestaurantCategoryEntity r WHERE r.categoryId = :categoryId", RestaurantCategoryEntity.class);
            query.setParameter("categoryId", categoryId);
            list = query.getResultList();

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }

        entityManager.close();
        return list;
    }

}
