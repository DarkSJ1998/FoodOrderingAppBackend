package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CategoryDao {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    public CategoryEntity getCategoryUsingId(final Long categoryId) {

        System.out.println("\n\t ======> CategoryDao.getCategoryUsingId() called");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List <CategoryEntity> list = new ArrayList<>();

        try {
            TypedQuery<CategoryEntity> query = entityManager
                    .createQuery("SELECT c FROM CategoryEntity c WHERE c.id = :categoryId", CategoryEntity.class);
            query.setParameter("categoryId", categoryId);
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

    public CategoryEntity getCategoryUsingUuid(final String categoryUuid) {

        System.out.println("\n\t ======> CategoryDao.getCategoryUsingUuid() called");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List <CategoryEntity> list = new ArrayList<>();

        try {
            TypedQuery<CategoryEntity> query = entityManager
                    .createQuery("SELECT c FROM CategoryEntity c WHERE c.uuid = :categoryUuid", CategoryEntity.class);
            query.setParameter("categoryUuid", categoryUuid);
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

    public List<CategoryEntity> getAllCategories() {

        System.out.println("\n\t ======> CategoryDao.getAllCategories() called");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List <CategoryEntity> list = new ArrayList<>();

        try {
            TypedQuery<CategoryEntity> query = entityManager
                    .createQuery("SELECT c FROM CategoryEntity c ORDER BY c.categoryName", CategoryEntity.class);
            list = query.getResultList();

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }

        entityManager.close();
        return list;
    }

}
