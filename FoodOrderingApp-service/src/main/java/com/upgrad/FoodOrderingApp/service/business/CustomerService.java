package com.upgrad.FoodOrderingApp.service.business;

import com.upgrad.FoodOrderingApp.service.dao.CustomerAuthEntityDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class CustomerService {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private CustomerAuthEntityDao customerAuthEntityDao;

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    @Transactional
    public CustomerEntity signup(final CustomerEntity customerEntity) throws SignUpRestrictedException {

        System.out.println("\n\t ====> CustomerService.signup() called");

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

    @Transactional
    public CustomerAuthEntity authenticate(final String contactNumber, final String password) throws AuthenticationFailedException {

        System.out.println("\n\t ====> CustomerService.authenticate() called");

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

                customerAuthEntity.setUuid(UUID.randomUUID().toString());
                customerAuthEntity.setCustomerId(customerEntity.getId());

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

    @Transactional
    public CustomerEntity searchByContactNumber(final String contactNumber) {

        System.out.println("\n\t ====> CustomerService.searchByContactNumber() called");

        return customerDao.searchByContactNumber(contactNumber);
    }

    @Transactional
    public CustomerEntity searchByUuid(final String uuid) {

        System.out.println("\n\t ====> CustomerService.searchByUuid() called");

        return customerDao.searchByUuid(uuid);
    }

    @Transactional
    public CustomerEntity searchById(final long id) {

        System.out.println("\n\t ====> CustomerService.searchById() called");

        return customerDao.searchById(id);
    }

    @Transactional
    public CustomerAuthEntity logout(final String accessToken) throws AuthorizationFailedException {

        System.out.println("\n\t ====> CustomerService.logout() called");

        CustomerAuthEntity customerAuthEntity = customerAuthEntityDao.getAuthTokenByAccessToken(accessToken);

        if (customerAuthEntity == null) {
            throw new AuthorizationFailedException("AUTH-001", "Customer is not Logged in.");
        }

        if(!isCustomerLoggedIn(accessToken)) {
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
        }

        if(checkTokenExpiry(accessToken)) {
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
        }

        ZonedDateTime now = ZonedDateTime.now();
        long difference = customerAuthEntity.getExpiresAt().compareTo(now);

        if(customerAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("AUTH-002", "Customer is logged out. Log in again to access this endpoint.");
        } else if (difference < 0) {
            throw new AuthorizationFailedException("AUTH-003", "Your session is expired. Log in again to access this endpoint.");
        } else {
            customerAuthEntity.setLogoutAt(now);
            customerAuthEntityDao.updateCustomer(customerAuthEntity);
        }

        return customerAuthEntity;
    }

    @Transactional
    public CustomerEntity getCustomer (final String accessToken) throws AuthorizationFailedException {

        System.out.println("\n\t ====> CustomerService.getCustomer() called");

        CustomerAuthEntity customerAuthEntity = customerAuthEntityDao.getAuthTokenByAccessToken(accessToken);

        if(customerAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
        }

        ZonedDateTime now = ZonedDateTime.now();
        long difference = customerAuthEntity.getExpiresAt().compareTo(now);

        CustomerEntity customerEntity = customerDao.searchById(customerAuthEntity.getCustomerId());
        return customerEntity;
    }

    @Transactional
    public CustomerEntity updateCustomerPassword (final String oldPassword, final String newPassword,final CustomerEntity customerEntity) throws UpdateCustomerException {

        System.out.println("\n\t ====> CustomerService.updateCustomerPassword() called");

        String encryptedOldPassword = passwordCryptographyProvider.encrypt(oldPassword, customerEntity.getSalt());
        String encryptedNewPassword = passwordCryptographyProvider.encrypt(newPassword, customerEntity.getSalt());

        if( oldPassword.length()==0 || newPassword.length()==0 )
            throw new UpdateCustomerException("UCR-003", "No field should be empty");

        CustomerEntity newCustomerEntity = new CustomerEntity();
        newCustomerEntity.setPassword(newPassword);

        if(!isPasswordStrong(newCustomerEntity))
            throw new UpdateCustomerException("UCR-001", "Weak password!");

        if(!customerEntity.getPassword().equals(encryptedOldPassword))
            throw new UpdateCustomerException("UCR-004", "Incorrect old password!");

        customerEntity.setPassword(encryptedNewPassword);
        customerDao.updateUser(customerEntity);

        return customerEntity;
    }

    @Transactional
    public boolean checkTokenExpiry(final String accessToken) {

        System.out.println("\n\t ====> CustomerService.checkTokenExpiry() called");

        CustomerAuthEntity customerAuthEntity = customerAuthEntityDao.getAuthTokenByAccessToken(accessToken);

        if(customerAuthEntity == null) {
            return true;
        }

        ZonedDateTime now = ZonedDateTime.now();
        long diff = customerAuthEntity.getExpiresAt().compareTo(now);

        if(diff < 0)
            return true;
        else
            return false;
    }

    @Transactional
    public boolean isCustomerLoggedIn(final String accessToken) {

        System.out.println("\n\t ====> CustomerService.isCustomerLoggedIn() called");

        CustomerAuthEntity customerAuthEntity = customerAuthEntityDao.getAuthTokenByAccessToken(accessToken);

        if(customerAuthEntity == null)
            return false;

        ZonedDateTime logoutAt = customerAuthEntity.getLogoutAt();
        ZonedDateTime loginAt = customerAuthEntity.getLoginAt();
        if(logoutAt == null)
            return true;

        long diff = loginAt.compareTo(logoutAt);
        if(diff > 0)
            return true;
        else
            return false;
    }

}
