package com.upgrad.FoodOrderingApp.service.business;

import com.upgrad.FoodOrderingApp.service.dao.CustomerAuthEntityDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class CustomerService {

    @Autowired
    CustomerDao customerDao;

    @Autowired
    CustomerAuthEntityDao customerAuthEntityDao;

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    public CustomerEntity signup(final CustomerEntity customerEntity) throws SignUpRestrictedException {
        CustomerEntity obj = customerDao.searchByContactNumber(customerEntity.getContactNumber());

        if(obj != null) {
            throw new SignUpRestrictedException("SGR-001", "This contact number is already registered! Try other contact number.");

        } else if(isFieldEmpty(customerEntity)) {
            throw new SignUpRestrictedException("SGR-005", "Except last name all fields should be filled");

        } else if(!isEmailIdCorrect(customerEntity)) {
            throw new SignUpRestrictedException("SGR-002", "Invalid email-id format!");

        } else if(!isContactNumberCorrect(customerEntity)) {
            throw new SignUpRestrictedException("SGR-003", "Invalid contact number!");

        } else if(!isPasswordStrong(customerEntity)) {
            throw new SignUpRestrictedException("SGR-004", "Weak password!");
        } else {

            encryptPassword(customerEntity);
            CustomerEntity customerEntityNew = customerDao.createUser(customerEntity);
            System.out.println(customerEntityNew.getEmail() + " " + customerEntityNew.getPassword());
            return customerEntityNew;
        }
    }

    public boolean isFieldEmpty(final CustomerEntity customerEntity) {
        if(customerEntity.getFirstName().length() == 0 ||
            customerEntity.getContactNumber().length() == 0 ||
            customerEntity.getEmail().length() == 0 ||
            customerEntity.getPassword().length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isEmailIdCorrect(final CustomerEntity customerEntity) {
        final String email = customerEntity.getEmail();
        return email.contains("@") && email.contains(".") && !email.contains(" ");
    }

    public boolean isContactNumberCorrect(final CustomerEntity customerEntity) {
        final String contactNumber = customerEntity.getContactNumber();

        try {
            long number = Long.parseLong(contactNumber);
            if(contactNumber.length() != 10)            // if number contains less than/greater than 10 digits
                return false;
            else
                return true;

        } catch (NumberFormatException e) {             // if number contains any other character other than digits
            return false;
        }
    }

    public boolean isPasswordStrong(final CustomerEntity customerEntity) {
        String password = customerEntity.getPassword();

        boolean flag1, flag2, flag3, flag4;
        flag1 = flag2 = flag3 = flag4 = false;

        if(password.length() >= 8)
            flag1 = true;

        for(int i=0;i<password.length();i++) {
            char ch = password.charAt(i);

            if(ch >= '0' && ch <= '9')
                flag2 = true;
            else if(ch >= 'A' && ch <= 'Z')
                flag3 = true;
            else if(ch=='#' || ch=='@' || ch=='$' || ch=='%' || ch=='&'
                    || ch=='*' || ch=='!' || ch=='^')
                flag4 = true;
        }

        return flag1 && flag2 && flag3 && flag4;
    }

    private void encryptPassword(final CustomerEntity customerEntity) {

        String password = customerEntity.getPassword();

        final String[] encryptedData = passwordCryptographyProvider.encrypt(password);
        customerEntity.setSalt(encryptedData[0]);
        customerEntity.setPassword(encryptedData[1]);
    }

    public CustomerAuthEntity authenticate(final String contactNumber, final String password) throws AuthenticationFailedException {

        //1. Using customerDao to find the user based on contact number
        CustomerEntity customerEntity = customerDao.searchByContactNumber(contactNumber);

        if(customerEntity == null) {
            throw new AuthenticationFailedException("AUTH-001", "This contact number has not been registered!");

        } else {

            //2. Authenticate the user
                // Encrypt the password(received from the user with the salt)

            String encryptedPassword = passwordCryptographyProvider.encrypt(password, customerEntity.getSalt());

            if(customerEntity.getPassword().equals(encryptedPassword)) {

                // User has been authenticated
                // Create a JWT token for the user
                JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);

                // Store data and that token in the database using CustomerAuthEntity
                CustomerAuthEntity customerAuthEntity = new CustomerAuthEntity();
                customerAuthEntity.setUuid(customerEntity.getUuid());
                customerAuthEntity.setCustomerId( (int) customerEntity.getId());

                ZonedDateTime now = ZonedDateTime.now();
                ZonedDateTime expiry = now.plusHours(8);
                customerAuthEntity.setLoginAt(now);
                customerAuthEntity.setExpiresAt(expiry);

                String accessToken = jwtTokenProvider.generateToken(customerEntity.getUuid(), now, expiry);
                customerAuthEntity.setAccess_token(accessToken);

                // Persist the CustomerAuthEntity generated, in the database
                customerAuthEntityDao.create(customerAuthEntity);
                customerDao.updateUser(customerEntity);

                // Return the CustomerAuthEntity generated
                return customerAuthEntity;

            } else {
                throw new AuthenticationFailedException("AUTH-002", "Invalid Credentials");

            }
        }
    }

    public CustomerEntity searchByContactNumber(final String contactNumber) {

        return customerDao.searchByContactNumber(contactNumber);
    }

    public CustomerEntity searchByUuid(final String uuid) {

        return customerDao.searchByUuid(uuid);
    }
}
