package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StateDao {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    public StateEntity getStateById(final Long stateId) {

        System.out.println("\n\t ======> StateDao.getStateById() called");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List <StateEntity> list = new ArrayList<>();

        try {
            TypedQuery<StateEntity> query = entityManager
                    .createQuery("SELECT s FROM StateEntity s WHERE s.id = :stateId", StateEntity.class);
            query.setParameter("stateId", stateId);
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
