package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.stereotype.Repository;
import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class CustomerDao {

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

    public CustomerEntity searchByUuid(final String uuid) {

        TypedQuery <CustomerEntity> query = entityManager.createQuery("SELECT c from CustomerEntity c where c.uuid = :uuid",
                CustomerEntity.class);

        List <CustomerEntity> list = query.setParameter("uuid", uuid).getResultList();
        if(list.size() == 0)
            return null;
        else
            return list.get(0);
    }

    @Transactional
    public CustomerEntity createUser(final CustomerEntity customerEntity) {
        try {

            entityManager.persist(customerEntity);

        } catch (Exception e) {
            System.out.println(e);
        }

        return customerEntity;
    }

    @Transactional
    public void updateUser(final CustomerEntity customerEntityNew) {
        try {
            entityManager.merge(customerEntityNew);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
