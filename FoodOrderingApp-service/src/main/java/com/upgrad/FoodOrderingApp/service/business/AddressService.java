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
    AddressDao addressDao;

    @Autowired
    CustomerAddressDao customerAddressDao;

    @Transactional
    public AddressEntity saveAddress(AddressEntity addressEntity, final String stateUuid, final CustomerEntity customerEntity) throws SaveAddressException, AddressNotFoundException {

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
        return addressDao.getStateIdByUuid(stateUuid);
    }

    @Transactional
    public List <AddressEntity> getAllAddresses(final CustomerEntity customerEntity) {
        return addressDao.getAllAddresses(customerEntity);
    }

    @Transactional
    public String getStateNameByStateId(final long stateId) {
        return addressDao.getStateNameByStateId(stateId);
    }

    @Transactional
    @Modifying
    public AddressEntity deleteAddress(AddressEntity addressEntity, CustomerAddressEntity customerAddressEntity) {
        customerAddressDao.deleteCustomerAddress(customerAddressEntity);
        return addressDao.deleteAddress(addressEntity);
    }

    @Transactional
    public AddressEntity searchByUuid(final String addressUuid) {
        return addressDao.searchByUuid(addressUuid);
    }

    @Transactional
    public CustomerAddressEntity searchByAddressId(final long addressId) {
        return customerAddressDao.searchByAddressId(addressId);
    }
}
