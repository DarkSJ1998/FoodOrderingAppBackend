package com.upgrad.FoodOrderingApp.service.business;

import com.upgrad.FoodOrderingApp.service.dao.ItemDao;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ItemService {

    @Autowired
    private ItemDao itemDao;

    @Transactional
    public ItemEntity getItemUsingId(final long itemId) {

        System.out.println("\n\t ====> ItemService.getItemUsingId() called");

        return itemDao.getItemUsingId(itemId);
    }

}
