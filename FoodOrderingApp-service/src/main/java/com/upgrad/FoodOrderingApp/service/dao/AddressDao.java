package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AddressDao {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    public AddressEntity saveAddress(AddressEntity addressEntity) {

        System.out.println("\n\t ======> AddressDao.saveAddress() called");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        System.out.println("addressEntity created with UUID : " + addressEntity.getUuid());

        EntityTransaction tx = entityManager.getTransaction();

        try {
            tx.begin();
            entityManager.persist(addressEntity);
            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            System.out.println(e);
            return null;
        }

        AddressEntity addressEntitySearch = searchByUuid(addressEntity.getUuid());
        addressEntity.setId(addressEntitySearch.getId());

        System.out.println("// AddressEntity created :");
        System.out.println("   id : " + addressEntity.getId());
        System.out.println("   uuid : " + addressEntity.getUuid());
        System.out.println("   flatBuilNumber : " + addressEntity.getFlatBuilNumber());
        System.out.println("   stateid : " + addressEntity.getStateId());

        entityManager.close();

        return addressEntity;
    }

    public StateEntity getStateIdByUuid(final String stateUuid) {

        System.out.println("\n\t ======> AddressDao.getStateIdByUuid() called");

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        TypedQuery <StateEntity> query = entityManager.
                createQuery("select s from StateEntity s where s.uuid = :uuid", StateEntity.class);
        List<StateEntity> list = query.setParameter("uuid", stateUuid).getResultList();

        entityManager.close();
        if(list.size() == 0)
            return null;
        else
            return list.get(0);
    }

    public List <AddressEntity> getAllAddresses(final CustomerEntity customerEntity) {

        System.out.println("\n\t ======> AddressDao.getAllAddresses() called");

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        TypedQuery <CustomerAddressEntity> query = entityManager
                .createQuery("select c from CustomerAddressEntity c where c.customerId = :customerId", CustomerAddressEntity.class);

        query.setParameter("customerId", customerEntity.getId());

        List <CustomerAddressEntity> customerAddressEntityList = query.getResultList();

        List <AddressEntity> list = new ArrayList<>();

        for(CustomerAddressEntity customerAddressEntity : customerAddressEntityList) {

            TypedQuery<AddressEntity> query2 = entityManager
                    .createQuery("select a From AddressEntity a where a.id = :id", AddressEntity.class);

            query2.setParameter("id", customerAddressEntity.getAddressId());

            AddressEntity addressEntity = query2.getSingleResult();

            list.add(addressEntity);
        }
        entityManager.close();
        return list;
    }

    public String getStateNameByStateId(final long stateId) {

        System.out.println("\n\t ======> AddressDao.getStateNameByStateId() called");

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        TypedQuery <StateEntity> query = entityManager.
                createQuery("select s from StateEntity s where s.id = :id", StateEntity.class);
        List<StateEntity> list = query.setParameter("id", stateId).getResultList();

        entityManager.close();

        if(list.size() == 0)
            return null;
        else
            return list.get(0).getStateName();
    }

    public AddressEntity deleteAddress(AddressEntity addressEntity) {

        System.out.println("\n\t ======> AddressDao.deleteAddress() called");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();

        try {
            tx.begin();
            AddressEntity searched = entityManager.find(AddressEntity.class, addressEntity.getId());
            entityManager.remove(searched);
            tx.commit();

            System.out.println("// addressEntity deleted :");
            System.out.println("   id :"+ searched.getUuid());
            System.out.println("   uuid :"+ searched.getUuid());

        } catch (Exception e) {
            tx.rollback();
            System.out.println(e);
            return null;
        }
        entityManager.close();

        return addressEntity;
    }

    public AddressEntity searchByUuid(final String addressUuid) {

        System.out.println("\n\t ======> AddressDao.searchByUuid() called");

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        TypedQuery <AddressEntity> query = entityManager.
                createQuery("select a from AddressEntity a where a.uuid = :uuid", AddressEntity.class);
        List<AddressEntity> list = query.setParameter("uuid", addressUuid).getResultList();

        entityManager.close();
        if(list.size() == 0)
            return null;
        else
            return list.get(0);
    }

    public AddressEntity getAddressById(final Long addressId) {

        System.out.println("\n\t ======> AddressDao.getAddressById() called");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List <AddressEntity> list = new ArrayList<>();

        try {
            TypedQuery<AddressEntity> query = entityManager
                    .createQuery("SELECT a FROM AddressEntity a WHERE a.id = :addressId", AddressEntity.class);
            query.setParameter("addressId", addressId);
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
