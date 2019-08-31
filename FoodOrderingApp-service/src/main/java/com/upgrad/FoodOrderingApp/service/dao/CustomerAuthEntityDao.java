package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

@Repository
public class CustomerAuthEntityDao {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @PersistenceContext
    private EntityManager entityManager;

    public CustomerAuthEntity create(final CustomerAuthEntity customerAuthEntity) {

        System.out.println("\n\t ======> CustomerAuthEntityDao.create() called");

        EntityManager entityManager = entityManagerFactory.createEntityManager();

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
        System.out.println("// customerAuthEntity created : ");
        System.out.println("   uuid : " + customerAuthEntity.getUuid());
        System.out.println("   login_at : " + customerAuthEntity.getLoginAt());
        System.out.println("   logout_at : " + customerAuthEntity.getLogoutAt());
        return customerAuthEntity;
    }

    public CustomerAuthEntity updateCustomer(final CustomerAuthEntity customerAuthEntity) {

        System.out.println("\n\t ======> CustomerAuthEntityDao.updateCustomer() called");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();

        try {

            tx.begin();
            entityManager.merge(customerAuthEntity);
            tx.commit();
            System.out.println("customerAuthEntity updated with UUID : " + customerAuthEntity.getUuid());

        } catch (Exception e) {
            tx.rollback();
            System.out.println(e);
            return null;
        }

        System.out.println("// customerAuthEntity updated : ");
        System.out.println("   uuid : " + customerAuthEntity.getUuid());
        System.out.println("   login_at : " + customerAuthEntity.getLoginAt());
        System.out.println("   logout_at : " + customerAuthEntity.getLogoutAt());

        return customerAuthEntity;
    }

    public CustomerAuthEntity getAuthTokenByAccessToken(final String access_token) {

        System.out.println("\n\t ======> CustomerAuthEntityDao.getAuthTokenByAccessToken() called");

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
