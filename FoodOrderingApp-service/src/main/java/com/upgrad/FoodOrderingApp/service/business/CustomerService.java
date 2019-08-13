package com.upgrad.FoodOrderingApp.service.business;

import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    CustomerDao customerDao;

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
        if(password == null) {
            password = "foodApp@123";
        }

        final String[] encryptedData = passwordCryptographyProvider.encrypt(password);
        customerEntity.setSalt(encryptedData[0]);
        customerEntity.setPassword(encryptedData[1]);
    }
}
