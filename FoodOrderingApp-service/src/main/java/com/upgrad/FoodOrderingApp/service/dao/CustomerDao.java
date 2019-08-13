package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class CustomerDao {

    @Autowired
    private EntityManagerFactory emf;

    @PersistenceContext
    private EntityManager entityManager;

    public CustomerEntity searchByContactNumber(final String contactNumber) {

        TypedQuery <CustomerEntity> query = entityManager.createQuery("SELECT c from CustomerEntity c where c.contactNumber = :contactNumber",
                CustomerEntity.class);

        List <CustomerEntity> list = query.setParameter("contactNumber", contactNumber).getResultList();
        if(list.size() == 0)
            return null;
        else
            return list.get(0);
    }

    @Transactional
    public CustomerEntity createUser(final CustomerEntity customerEntity) {
        EntityManager entityManagerNew = emf.createEntityManager();
        EntityTransaction tx = entityManagerNew.getTransaction();

        try {
            tx.begin();
            entityManagerNew.persist(customerEntity);
            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            System.out.println(e);
        }

        return customerEntity;
    }

}
