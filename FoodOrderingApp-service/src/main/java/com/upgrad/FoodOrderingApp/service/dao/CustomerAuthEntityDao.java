package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class CustomerAuthEntityDao {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public CustomerAuthEntity create(final CustomerAuthEntity customerAuthEntity) {

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        System.out.println("customerAuthEntity created with UUID : " + customerAuthEntity.getUuid());

        EntityTransaction tx = entityManager.getTransaction();

        try {

            tx.begin();
            entityManager.persist(customerAuthEntity);
            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            System.out.println(e);
            return null;
        }
        return customerAuthEntity;
    }

    @Transactional
    public CustomerAuthEntity updateCustomer(final CustomerAuthEntity customerAuthEntity) {

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();

        try {

            tx.begin();
            entityManager.merge(customerAuthEntity);
            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            System.out.println(e);
            return null;
        }
        return customerAuthEntity;
    }

    public CustomerAuthEntity getAuthTokenByAccessToken(final String access_token) {

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery <CustomerAuthEntity> query = entityManager.
                createQuery("select ca from CustomerAuthEntity ca where ca.access_token = :access_token", CustomerAuthEntity.class);
        List <CustomerAuthEntity> list = query.setParameter("access_token", access_token).getResultList();
        if(list.size() == 0)
            return null;
        else
            return list.get(0);
    }

}
