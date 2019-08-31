package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryItemEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CategoryItemDao {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    public List<CategoryItemEntity> getItemsUsingCategoryId(final long categoryId) {

        System.out.println("\n\t ======> CategoryItemDao.getItemsUsingCategoryId() called");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List <CategoryItemEntity> list = new ArrayList<>();

        try {
            TypedQuery<CategoryItemEntity> query = entityManager
                    .createQuery("SELECT c FROM CategoryItemEntity c WHERE c.categoryId = :categoryId", CategoryItemEntity.class);
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
