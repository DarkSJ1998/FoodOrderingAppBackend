package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RestaurantDao {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    public List <RestaurantEntity> getRestaurantsByName(final String restaurantName) {

        System.out.println("\n\t ======> RestaurantDao.getRestaurantsByName() called");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<RestaurantEntity> list = new ArrayList<>();

        String str = "%"+restaurantName.toLowerCase()+"%";

        try {
            TypedQuery <RestaurantEntity> query = entityManager
                    .createQuery("SELECT r FROM RestaurantEntity r WHERE LOWER(r.restaurantName) LIKE :str ORDER BY r.restaurantName", RestaurantEntity.class);
            query.setParameter("str", str);
            list = query.getResultList();

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }

        entityManager.close();
        return list;
    }

    public RestaurantEntity getRestaurantById(final Long restaurantId) {

        System.out.println("\n\t ======> RestaurantDao.getRestaurantById() called");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<RestaurantEntity> list = new ArrayList<>();

        try {
            TypedQuery <RestaurantEntity> query = entityManager
                    .createQuery("SELECT r FROM RestaurantEntity r WHERE r.id = :restaurantId ORDER BY r.restaurantName", RestaurantEntity.class);
            query.setParameter("restaurantId", restaurantId);
            list = query.getResultList();

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }

        entityManager.close();

        if(list.size() == 0)
            return null;
        else
            return list.get(0);
    }

    public RestaurantEntity getRestaurantByUuid(final String restaurantUuid) {

        System.out.println("\n\t ======> RestaurantDao.getRestaurantByUuid() called");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<RestaurantEntity> list = new ArrayList<>();

        try {
            TypedQuery <RestaurantEntity> query = entityManager
                    .createQuery("SELECT r FROM RestaurantEntity r WHERE r.uuid = :restaurantUuid", RestaurantEntity.class);
            query.setParameter("restaurantUuid", restaurantUuid);
            list = query.getResultList();

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }

        entityManager.close();

        if(list.size() == 0)
            return null;
        else
            return list.get(0);
    }

}
