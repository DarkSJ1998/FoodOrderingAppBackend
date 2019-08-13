package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.SignupCustomerRequest;
import com.upgrad.FoodOrderingApp.api.model.SignupCustomerResponse;
import com.upgrad.FoodOrderingApp.service.business.CustomerService;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @RequestMapping(value = "/customer/signup", method = RequestMethod.POST)
    public ResponseEntity<SignupCustomerResponse> signup(SignupCustomerRequest signupCustomerRequest) throws SignUpRestrictedException {
        CustomerEntity customerEntity = new CustomerEntity();

        customerEntity.setUuid( UUID.randomUUID().toString() );
        customerEntity.setFirstName( signupCustomerRequest.getFirstName() );
        customerEntity.setLastName( signupCustomerRequest.getLastName() );
        customerEntity.setEmail( signupCustomerRequest.getEmailAddress() );
        customerEntity.setContactNumber( signupCustomerRequest.getContactNumber() );
        customerEntity.setPassword( signupCustomerRequest.getPassword() );

        try {
            CustomerEntity result = customerService.signup(customerEntity);
            SignupCustomerResponse response = new SignupCustomerResponse().id(result.getUuid()).status("CUSTOMER SUCCESSFULLY REGISTERED");

            return new ResponseEntity<SignupCustomerResponse>(response, HttpStatus.CREATED);
        } catch (SignUpRestrictedException e) {
            SignupCustomerResponse response = new SignupCustomerResponse().id(e.getCode()).status(e.getErrorMessage());

            return new ResponseEntity<SignupCustomerResponse>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
