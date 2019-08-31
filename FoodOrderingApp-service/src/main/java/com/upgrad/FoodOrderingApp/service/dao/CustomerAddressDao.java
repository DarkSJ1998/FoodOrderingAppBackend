package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

@Repository
public class CustomerAddressDao {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @PersistenceContext
    private EntityManager entityManager;

    public CustomerAddressEntity searchByAddressId(final long addressId) {

        System.out.println("\n\t ======> CustomerAddressDao.searchByAddressId() called");

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        TypedQuery<CustomerAddressEntity> query = entityManager.
                createQuery("select c from CustomerAddressEntity c where c.addressId = :addressId", CustomerAddressEntity.class);
        List<CustomerAddressEntity> list = query.setParameter("addressId", addressId).getResultList();

        if(list.size() == 0)
            return null;
        else
            return list.get(0);
    }

    public CustomerAddressEntity saveCustomerAddress(final CustomerAddressEntity customerAddressEntity) {

        System.out.println("\n\t ======> CustomerAddressDao.saveCustomerAddress() called");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();

        try {

            tx.begin();
            entityManager.persist(customerAddressEntity);
            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            System.out.println(e);
            return null;
        }

        System.out.println("// CustomerAddressEntity created :");
        System.out.println("   customerId : " + customerAddressEntity.getCustomerId());
        System.out.println("   addressId : " + customerAddressEntity.getAddressId());

        return customerAddressEntity;
    }

    public CustomerAddressEntity deleteCustomerAddress(final CustomerAddressEntity customerAddressEntity) {

        System.out.println("\n\t ======> CustomerAddressDao.deleteCustomerAddress() called");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();

        try {

            tx.begin();
            entityManager.remove(customerAddressEntity);
            tx.commit();

            System.out.println("// CustomerAddressEntity deleted :");
            System.out.println("   customerId : " + customerAddressEntity.getCustomerId());
            System.out.println("   addressId : " + customerAddressEntity.getAddressId());

        } catch (Exception e) {
            tx.rollback();
            System.out.println(e);
            return null;
        }

        return customerAddressEntity;
    }

}
