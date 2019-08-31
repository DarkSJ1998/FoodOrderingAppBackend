package com.upgrad.FoodOrderingApp.service.business;

import com.upgrad.FoodOrderingApp.service.dao.StateDao;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class StateService {

    @Autowired
    private StateDao stateDao;

    @Transactional
    public StateEntity getStateById(final Long stateId) {

        System.out.println("\n\t ====> StateService.getStateById() called");

        return stateDao.getStateById(stateId);
    }

}
