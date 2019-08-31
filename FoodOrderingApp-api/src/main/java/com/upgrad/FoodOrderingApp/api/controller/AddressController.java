package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.business.AddressService;
import com.upgrad.FoodOrderingApp.service.business.CustomerService;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class AddressController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AddressService addressService;

    @RequestMapping(value = "/address", method = RequestMethod.POST)
    public ResponseEntity<SaveAddressResponse> saveAddress(SaveAddressRequest saveAddressRequest, @RequestHeader("access-token") final String accessToken) {

        System.out.println("\n\t ==> AddressController.saveAddress() called");

        try {

            // Getting the CustomerEntity object using the access_token.
            CustomerEntity customerEntity = customerService.getCustomer(accessToken);

            if(customerEntity == null) {
                throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
            }

            if(!customerService.isCustomerLoggedIn(accessToken)) {
                throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
            }

            if(customerService.checkTokenExpiry(accessToken)) {
                throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
            }

            // Creating an empty AddressEntity object.
            AddressEntity addressEntity = new AddressEntity();

            // Setting values for the AddressEntity object.
            addressEntity.setUuid(UUID.randomUUID().toString());
            addressEntity.setFlatBuilNumber(saveAddressRequest.getFlatBuildingName());
            addressEntity.setLocality(saveAddressRequest.getLocality());
            addressEntity.setCity(saveAddressRequest.getCity());
            addressEntity.setPincode(saveAddressRequest.getPincode());

            // Extracting stateUuid value from the saveAddressRequest object.
            String stateUuid = saveAddressRequest.getStateUuid();

            // Saving the AddressEntity object.
            addressService.saveAddress(addressEntity, stateUuid, customerEntity);

            SaveAddressResponse saveAddressResponse = new SaveAddressResponse()
                    .id(addressEntity.getUuid())
                    .status("ADDRESS SUCCESSFULLY REGISTERED");

            return new ResponseEntity<SaveAddressResponse>(saveAddressResponse, HttpStatus.CREATED);

        } catch (AuthorizationFailedException e) {
            System.out.println(e);

            SaveAddressResponse saveAddressResponse = new SaveAddressResponse()
                    .id(e.getCode())
                    .status(e.getErrorMessage());
            return new ResponseEntity<SaveAddressResponse>(saveAddressResponse, HttpStatus.BAD_REQUEST);

        } catch (SaveAddressException e) {
            System.out.println(e);

            SaveAddressResponse saveAddressResponse = new SaveAddressResponse().id(e.getCode()).status(e.getErrorMessage());
            return new ResponseEntity<SaveAddressResponse>(saveAddressResponse, HttpStatus.BAD_REQUEST);

        } catch (AddressNotFoundException e) {
            System.out.println(e);

            SaveAddressResponse saveAddressResponse = new SaveAddressResponse().id(e.getCode()).status(e.getErrorMessage());
            return new ResponseEntity<SaveAddressResponse>(saveAddressResponse, HttpStatus.BAD_REQUEST);

        }
    }

    @RequestMapping(value = "/address/customer", method = RequestMethod.GET)
    public ResponseEntity getAllAddresses(@RequestHeader("access-token") final String accessToken) {

        System.out.println("\n\t ==> AddressController.getAllAddresses() called");

        try {

            // Getting the CustomerEntity object using the access_token.
            CustomerEntity customerEntity = customerService.getCustomer(accessToken);

            if(customerEntity == null) {
                throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
            }

            if(!customerService.isCustomerLoggedIn(accessToken)) {
                throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
            }

            if(customerService.checkTokenExpiry(accessToken)) {
                throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
            }

            // Getting the AddressEntity objects list.
            List <AddressEntity> addressEntityList = addressService.getAllAddresses(customerEntity);

            // Creating an empty AddressList list.
            List <AddressList> addressListList = new ArrayList<>();

            for(AddressEntity addressEntity : addressEntityList) {

                // Creating an AddressListState object.
                AddressListState addressListState = new AddressListState()
                        .id(UUID.fromString( addressEntity.getUuid() ))
                        .stateName(addressService.getStateNameByStateId(addressEntity.getId()));

                // Creating an AddressList object.
                AddressList addressList = new AddressList()
                        .id( UUID.fromString(addressEntity.getUuid()) )
                        .flatBuildingName(addressEntity.getFlatBuilNumber())
                        .locality(addressEntity.getLocality())
                        .city(addressEntity.getCity())
                        .state(addressListState)
                        .pincode(addressEntity.getPincode());

                // Adding AddressList object to the list.
                addressListList.add(addressList);
            }

            AddressListResponse addressListResponse = new AddressListResponse().addresses(addressListList);
            return new ResponseEntity(addressListResponse, HttpStatus.OK);

        } catch (AuthorizationFailedException e) {
            System.out.println(e);

            ErrorResponse errorResponse = new ErrorResponse().code(e.getCode()).message(e.getErrorMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/address/{address_id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteAddress(@PathVariable("address_id") final String addressUuid, @RequestHeader("access-token") final String accessToken) {

        System.out.println("\n\t ==> AddressController.deleteAddress() called");

        try {

            // Getting the CustomerEntity object using the access_token.
            CustomerEntity customerEntity = customerService.getCustomer(accessToken);

            if(customerEntity == null) {
                throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
            }

            if(!customerService.isCustomerLoggedIn(accessToken)) {
                throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
            }

            if(customerService.checkTokenExpiry(accessToken)) {
                throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
            }

            if(addressUuid.length() == 0) {
                throw new AddressNotFoundException("ANF-005", "Address id can not be empty");
            }

            // Getting the AddressEntity object using the addressUuid.
            AddressEntity addressEntity = addressService.searchByUuid(addressUuid);
            CustomerAddressEntity customerAddressEntity = addressService.searchByAddressId(addressEntity.getId());

            if(customerAddressEntity.getCustomerId() != customerEntity.getId()) {
                throw new AuthorizationFailedException("ATHR-004", "You are not authorized to view/update/delete any one else's address.");
            }

            if(addressEntity == null) {
                throw new AddressNotFoundException("ANF-003", "No address by this id");
            }

            // Deleting the AddressEntity object from Address table and CustomerAddressEntity object from Customer_Address table.
            addressService.deleteAddress(addressEntity, customerAddressEntity);

            DeleteAddressResponse deleteAddressResponse = new DeleteAddressResponse()
                    .id( UUID.fromString(addressEntity.getUuid()) )
                    .status("ADDRESS DELETED SUCCESSFULLY");

            return new ResponseEntity<DeleteAddressResponse>(deleteAddressResponse, HttpStatus.OK);

        } catch (AuthorizationFailedException e) {
            System.out.println(e);

            ErrorResponse errorResponse = new ErrorResponse().code(e.getCode()).message(e.getErrorMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

        } catch (AddressNotFoundException e) {
            System.out.println(e);

            ErrorResponse errorResponse = new ErrorResponse().code(e.getCode()).message(e.getErrorMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

}
