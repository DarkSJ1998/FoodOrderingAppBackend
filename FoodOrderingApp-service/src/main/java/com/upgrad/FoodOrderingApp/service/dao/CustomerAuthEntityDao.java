package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

@Repository
public class CustomerAuthEntityDao {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private EntityManagerFactory emf;

    public CustomerAuthEntity create(final CustomerAuthEntity customerAuthEntity) {
        try {
//            entityManager.persist(customerAuthEntity);
            EntityManager em = emf.createEntityManager();
            em.persist(customerAuthEntity);

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
        return customerAuthEntity;
    }
}
