package com.upgrad.FoodOrderingApp.service.business;

import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerAddressDao;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class AddressService {

    @Autowired
    private AddressDao addressDao;

    @Autowired
    private CustomerAddressDao customerAddressDao;

    @Transactional
    public AddressEntity saveAddress(AddressEntity addressEntity, final String stateUuid, final CustomerEntity customerEntity) throws SaveAddressException, AddressNotFoundException {

        System.out.println("\n\t ====> AddressService.saveAddress() called");

        if(isFieldEmpty(addressEntity, stateUuid)) {
            throw new SaveAddressException("SAR-001", "No field can be empty");
        } else if(!isPincodeCorrect(addressEntity)) {
            throw new SaveAddressException("SAR-002", "Invalid pincode");
        } else {

            StateEntity stateEntity = getStateIdByUuid(stateUuid);
            if(stateEntity == null) {
                throw new AddressNotFoundException("ANF-002", "No state by this id");
            }

            addressEntity.setStateId(stateEntity.getId());
            addressEntity = addressDao.saveAddress(addressEntity);

            CustomerAddressEntity customerAddressEntity = new CustomerAddressEntity();
            customerAddressEntity.setCustomerId(customerEntity.getId());
            customerAddressEntity.setAddressId(addressEntity.getId());

            customerAddressDao.saveCustomerAddress(customerAddressEntity);

            return addressEntity;
        }
    }

    public boolean isFieldEmpty(final AddressEntity addressEntity, String stateUuid) {

        if(addressEntity.getFlatBuilNumber().length() == 0 ||
                addressEntity.getLocality().length() == 0 ||
                addressEntity.getCity().length() == 0 ||
                addressEntity.getPincode().length() == 0 ||
                stateUuid.length() == 0) {

            return true;
        } else {
            return false;
        }
    }

    public boolean isPincodeCorrect(final AddressEntity addressEntity) throws SaveAddressException {

        final String pincode = addressEntity.getPincode();

        try {
            long number = Long.parseLong(pincode);
            if(pincode.length() != 6)            // if number contains less than/greater than 6 digits
                return false;
            else
                return true;

        } catch (NumberFormatException e) {             // if number contains any other character other than digits
            return false;
        }
    }

    @Transactional
    public StateEntity getStateIdByUuid(final String stateUuid) throws AddressNotFoundException {

        System.out.println("\n\t ====> AddressService.getStateIdByUuid() called");

        return addressDao.getStateIdByUuid(stateUuid);
    }

    @Transactional
    public List <AddressEntity> getAllAddresses(final CustomerEntity customerEntity) {

        System.out.println("\n\t ====> AddressService.getAllAddresses() called");

        return addressDao.getAllAddresses(customerEntity);
    }

    @Transactional
    public String getStateNameByStateId(final long stateId) {

        System.out.println("\n\t ====> AddressService.getStateNameByStateId() called");

        return addressDao.getStateNameByStateId(stateId);
    }

    @Transactional
    @Modifying
    public AddressEntity deleteAddress(AddressEntity addressEntity, CustomerAddressEntity customerAddressEntity) {

        System.out.println("\n\t ====> AddressService.deleteAddress() called");

        customerAddressDao.deleteCustomerAddress(customerAddressEntity);
        return addressDao.deleteAddress(addressEntity);
    }

    @Transactional
    public AddressEntity searchByUuid(final String addressUuid) {

        System.out.println("\n\t ====> AddressService.searchByUuid() called");

        return addressDao.searchByUuid(addressUuid);
    }

    @Transactional
    public CustomerAddressEntity searchByAddressId(final long addressId) {

        System.out.println("\n\t ====> AddressService.searchByAddressId() called");

        return customerAddressDao.searchByAddressId(addressId);
    }

    @Transactional
    public AddressEntity getAddressById(final Long addressId) {

        System.out.println("\n\t ====> AddressService.getAddressById() called");

        return addressDao.getAddressById(addressId);
    }

}
