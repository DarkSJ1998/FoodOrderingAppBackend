package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.business.CustomerService;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
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

    @RequestMapping(value = "/customer/login", method = RequestMethod.POST)
    public ResponseEntity<LoginResponse> login(@RequestHeader("authorization") final String authorization) throws AuthenticationFailedException {
        byte[] decodedBytes = Base64.getDecoder().decode(authorization);
        String decodedString = new String(decodedBytes);

        if(!decodedString.contains(":")) {
            throw new AuthenticationFailedException("ATH-003", "Incorrect format of decoded customer name and password");

        }

        try {
            String[] decodedArray = decodedString.split(":");
            CustomerAuthEntity customerAuthEntity = customerService.authenticate(
                    decodedArray[0], decodedArray[1]
            );

            CustomerEntity customerEntity = customerService.searchByUuid(customerAuthEntity.getUuid());

            if(customerEntity == null) {
                throw new AuthenticationFailedException("AUTH-002", "Invalid Credentials");
            }

            LoginResponse response = new LoginResponse().id(customerEntity.getUuid())
                    .emailAddress(customerEntity.getEmail())
                    .firstName(customerEntity.getFirstName())
                    .lastName(customerEntity.getLastName())
                    .contactNumber(customerEntity.getContactNumber())
                    .message("LOGGED IN SUCCESSFULLY");

            // Sending the received access token in the Header
            HttpHeaders httpHeader = new HttpHeaders();
            httpHeader.add("access-token", customerAuthEntity.getAccess_token());
            return new ResponseEntity<LoginResponse>(response, httpHeader, HttpStatus.OK);

        } catch (AuthenticationFailedException e) {
            LoginResponse response = new LoginResponse().id(e.getCode()).message(e.getErrorMessage());
            return new ResponseEntity<LoginResponse>(response, HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/customer/logout", method=RequestMethod.POST)
    public ResponseEntity<LogoutResponse> logout(@RequestHeader("access-token")final String accessToken) throws AuthorizationFailedException {

        try {
            CustomerAuthEntity c = customerService.logout(accessToken);
            LogoutResponse response = new LogoutResponse().id(c.getUuid()).message("LOGGED OUT SUCCESSFULLY");
            return new ResponseEntity<LogoutResponse>(response, HttpStatus.OK);

        } catch (AuthorizationFailedException e) {
            LogoutResponse response = new LogoutResponse().id(e.getCode()).message(e.getErrorMessage());
            return new ResponseEntity<LogoutResponse>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/customer/password", method = RequestMethod.PUT)
    public ResponseEntity<UpdatePasswordResponse> updateCustomerPassword (UpdatePasswordRequest request, @RequestHeader("access-token")final String accessToken) {
        CustomerEntity c = customerService.getCustomer(accessToken);
        CustomerEntity p = customerService.updateCustomerPassword(
                request.getOldPassword(),
                request.getNewPassword(),c
        );

        UpdatePasswordResponse response = new UpdatePasswordResponse().
                id(p.getUuid()).
                status("CUSTOMER PASSWORD UPDATED SUCCESSFULLY");

        return new ResponseEntity<UpdatePasswordResponse> (response, HttpStatus.OK);
    }
}
