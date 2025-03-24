package com.revature.services;


import com.revature.config.TimeZoneConfig;
import com.revature.models.User;
import com.revature.models.UserAddress;
import com.revature.models.UserResetCode;
import com.revature.repos.UserAddressDAO;
import com.revature.repos.UserDAO;
import com.revature.util.SendGridUtil;
import org.mindrot.jbcrypt.BCrypt;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;

public class UserService {

    private final UserDAO userDAO;
    private final UserAddressDAO userAddressDAO;

    public UserService(UserDAO userDAO, UserAddressDAO userAddressDAO) {
        this.userDAO = userDAO;
        this.userAddressDAO = userAddressDAO;
    }

    // Security
    public boolean validateEmail(String email) {

        //Validations:Regex type email
        if (email == null || email.isEmpty()) {
            return false;
        }

        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        return Pattern.compile(regexPattern)
                .matcher(email)
                .matches();
    }

    public boolean validatePassword(String password) {
            /*
                Validations
                // Length >= 8
                // At least 1 lower case character
                // At least 1 Upper case character
                // At least 1 digit
                // At least 1 Special char of the pull (!@#$%^&*)
             */

        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasLower = false;
        boolean hasUpper = false;
        boolean hasSpecial = false;
        boolean hasDigit = false;

        String specialChars = "!@#$%^&*";

        for (char c : password.toCharArray()) {
            if (Character.isLowerCase(c)) {
                hasLower = true;
            } else if (Character.isUpperCase(c)) {
                hasUpper = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (specialChars.indexOf(c) != -1) {
                hasSpecial = true;
            }
        }

        return hasLower && hasUpper && hasSpecial && hasDigit;
    }

    // Availability

    public boolean isEmailAvailable(String email) {
        return userDAO.getUserByEmail(email) == null;
    }

    public boolean isEmailAvailableForUpdate(int userId, String email) {
        User u = userDAO.getUserByEmail(email);

        if (u == null) {
            return true;
        }

        return u.getUserId() == userId;
    }

    // GET
    public List<User> getAllUsers() {
        return userDAO.getAll();
    }


    public List<UserAddress> getUserAddresses(int userId) {
        return userAddressDAO.getUserAddress(userId);
    }

    public User getUserByEmail(String email){
        return userDAO.getUserByEmail(email);
    }

    // POST
    public User registerNewUser(String firstName, String lastName, String email, String password) {
        // NOTE: We expect our validation methods to be called BEFORE this method is called in the controller layer
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User userToBeSaved = new User(0, firstName, lastName, email, hashedPassword);

        return userDAO.create(userToBeSaved);
    }

    public User loginUser(String email, String password) {
        User returnedUser = userDAO.getUserByEmail(email);
        if (returnedUser == null) {
            return null;
        }


        if (BCrypt.checkpw(password, returnedUser.getPassword())) {
            return returnedUser;
        }

        return null;
    }

    // UPDATE

    public User updateUser(int userId, String firstName, String lastName, String email, String password) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User userToBeUpdate = new User(userId, firstName, lastName, email, hashedPassword);
        return userDAO.update(userToBeUpdate);
    }

    public User updateStatus(int userId, boolean status) {

        return userDAO.updateStatus(userId, status);
    }

    public User updatePassword(int userId, String password) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        return userDAO.updatePassword(userId, hashedPassword);
    }

    // send email
    public boolean sendResetCodeEmail(String email, UserResetCode resetCode){

        String subject = "Reset code for E-Comers";
        SendGridUtil sendGrid = new SendGridUtil();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy h:mm a Z")
                .withZone(java.time.ZoneId.of(TimeZoneConfig.ZONE_ID));

        String formattedDateTime = resetCode.getExpiredAt().format(formatter);

        String content = "This is your reset code: " + resetCode.getResetCode() + " " +
                "expired at: " + formattedDateTime;

        return sendGrid.SendEmail(subject, email, content);
    }
}
