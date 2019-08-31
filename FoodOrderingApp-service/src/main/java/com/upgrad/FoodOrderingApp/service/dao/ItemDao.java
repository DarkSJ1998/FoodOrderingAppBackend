package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ItemDao {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    public ItemEntity getItemUsingId(final long itemId) {

        System.out.println("\n\t ======> ItemDao.getItemUsingId() called");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<ItemEntity> list = new ArrayList<>();

        try {
            TypedQuery<ItemEntity> query = entityManager
                    .createQuery("SELECT i FROM ItemEntity i WHERE i.id = :itemId", ItemEntity.class);
            query.setParameter("itemId", itemId);
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
