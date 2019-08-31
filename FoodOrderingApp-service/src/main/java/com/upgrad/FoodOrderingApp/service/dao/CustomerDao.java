package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.stereotype.Repository;
import javax.persistence.*;
import java.util.List;

@Repository
public class CustomerDao {

    @PersistenceContext
    private EntityManager entityManager;

    public CustomerEntity searchByContactNumber(final String contactNumber) {

        System.out.println("\n\t ======> CustomerDao.searchByContactNumber() called");

        TypedQuery <CustomerEntity> query = entityManager.createQuery("SELECT c from CustomerEntity c where c.contactNumber = :contactNumber",
                CustomerEntity.class);

        List <CustomerEntity> list = query.setParameter("contactNumber", contactNumber).getResultList();
        if(list.size() == 0)
            return null;
        else
            return list.get(0);
    }

    public CustomerEntity searchByUuid(final String uuid) {

        System.out.println("\n\t ======> CustomerDao.searchByUuid() called");

        TypedQuery <CustomerEntity> query = entityManager.createQuery("SELECT c from CustomerEntity c where c.uuid = :uuid",
                CustomerEntity.class);

        List <CustomerEntity> list = query.setParameter("uuid", uuid).getResultList();
        if(list.size() == 0)
            return null;
        else
            return list.get(0);
    }

    public CustomerEntity searchById(final long id) {

        System.out.println("\n\t ======> CustomerDao.searchById() called");

        TypedQuery <CustomerEntity> query = entityManager.createQuery("SELECT c from CustomerEntity c where c.id = :id",
                CustomerEntity.class);

        List <CustomerEntity> list = query.setParameter("id", id).getResultList();
        if(list.size() == 0)
            return null;
        else
            return list.get(0);
    }

    public CustomerEntity createUser(final CustomerEntity customerEntity) {

        System.out.println("\n\t ======> CustomerDao.createUser() called");

        try {

            entityManager.persist(customerEntity);

        } catch (Exception e) {
            System.out.println(e);
        }

        return customerEntity;
    }

    public void updateUser(final CustomerEntity customerEntityNew) {

        System.out.println("\n\t ======> CustomerDao.updateUser() called");

        try {
            entityManager.merge(customerEntityNew);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
