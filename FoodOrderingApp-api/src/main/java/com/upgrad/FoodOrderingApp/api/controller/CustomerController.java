package com.upgrad.FoodOrderingApp.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

    @RequestMapping(value = "/customer/signup",method = RequestMethod.POST)
    public void signup() {

    }
}
