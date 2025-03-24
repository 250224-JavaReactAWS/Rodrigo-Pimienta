import com.revature.models.User;
import com.revature.models.UserAddress;
import com.revature.models.UserRole;
import com.revature.repos.UserAddressDAOImpl;
import com.revature.repos.UserDAOImpl;
import com.revature.services.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

public class UserServicesTest {

    //private final UserService userService = new UserService(new UserDAOImpl(), new UserAddressDAOImpl());


    private UserService userService;

    private UserDAOImpl mockDAO;

    @Before
    public void setup(){
        mockDAO = Mockito.mock(UserDAOImpl.class);
        userService = new UserService(mockDAO, new UserAddressDAOImpl());
    }

    // I'm going to use AAA

    // Arrange, Act and Assert

    // TEST VALIDATION (SECURITY)
    @Test
    public void invalidEmail(){ // SHOULD RETURN FALSE
        String email = "test_gmail.com";

        boolean result = userService.validateEmail(email);

        Assert.assertFalse(result);
    }

    @Test
    public void validEmail(){ // SHOULD RETURN TRUE
        String email = "test@gmail.com";

        boolean result = userService.validateEmail(email);

        Assert.assertTrue(result);
    }

    @Test
    public void invalidLengthPassword(){ // SHOULD RETURN False
        /*
            Validations
            // Length >= 8
            // At least 1 lower case character
            // At least 1 Upper case character
            // At least 1 digit
            // At least 1 Special char of the pull (!@#$%^&*)
         */
        String password = "ABCDEFG";

        boolean result = userService.validatePassword(password);

        Assert.assertFalse(result);
    }

    @Test
    public void invalidLowerCasePassword(){ // SHOULD RETURN False
        /*
            Validations
            // Length >= 8
            // At least 1 lower case character
            // At least 1 Upper case character
            // At least 1 digit
            // At least 1 Special char of the pull (!@#$%^&*)
         */
        String password = "ABCDEF1*";

        boolean result = userService.validatePassword(password);

        Assert.assertFalse(result);
    }

    @Test
    public void invalidUpperCasePassword(){ // SHOULD RETURN False
        /*
            Validations
            // Length >= 8
            // At least 1 lower case character
            // At least 1 Upper case character
            // At least 1 digit
            // At least 1 Special char of the pull (!@#$%^&*)
         */
        String password = "abcdef1*";

        boolean result = userService.validatePassword(password);

        Assert.assertFalse(result);
    }

    @Test
    public void invalidDigitPassword(){ // SHOULD RETURN False
        /*
            Validations
            // Length >= 8
            // At least 1 lower case character
            // At least 1 Upper case character
            // At least 1 digit
            // At least 1 Special char of the pull (!@#$%^&*)
         */
        String password = "abcdefG*";

        boolean result = userService.validatePassword(password);

        Assert.assertFalse(result);
    }

    @Test
    public void invalidSpecialCHarPassword(){ // SHOULD RETURN False
        /*
            Validations
            // Length >= 8
            // At least 1 lower case character
            // At least 1 Upper case character
            // At least 1 digit
            // At least 1 Special char of the pull (!@#$%^&*)
         */
        String password = "abcdefG1";

        boolean result = userService.validatePassword(password);

        Assert.assertFalse(result);
    }



    @Test
    public void validPassword(){ // SHOULD RETURN False
      /*
            Validations
            // Length >= 8
            // At least 1 lower case character
            // At least 1 Upper case character
            // At least 1 digit
            // At least 1 Special char of the pull (!@#$%^&*)
         */
        String password = "Abcdef1*";

        boolean result = userService.validatePassword(password);

        Assert.assertTrue(result);
    }

    // TEST VALIDATION AVAILABILITY
    @Test
    public void takenEmail(){ // SHOULD RETURN FALSE
        String testEmail = "email@gmail.com";
        User mockedUser = new User(1,"test","test",testEmail,"test");
        when(mockDAO.getUserByEmail(testEmail)).thenReturn(mockedUser);

        boolean isEmailAvailability = userService.isEmailAvailable(testEmail);

        Assert.assertFalse(isEmailAvailability);
    }

    @Test
    public void availableEmail(){ // SHOULD RETURN TRUE
        String testEmail = "email@gmail.com";
        when(mockDAO.getUserByEmail(testEmail)).thenReturn(null);

        boolean isEmailAvailability = userService.isEmailAvailable(testEmail);

        Assert.assertTrue(isEmailAvailability);
    }

    @Test
    public void takenEmailToUpdate(){ // SHOULD RETURN FALSE
        String testEmail = "email@gmail.com";
        int userId = 2;
        User mockedUser = new User(1,"test","test",testEmail,"test");
        when(mockDAO.getUserByEmail(testEmail)).thenReturn(mockedUser);

        boolean isEmailAvailability = userService.isEmailAvailableForUpdate(userId,testEmail);

        Assert.assertFalse(isEmailAvailability);
    }

    @Test
    public void availableEmailToUpdate(){ // SHOULD RETURN TRUE
        String testEmail = "email@gmail.com";
        int userId = 1;
        User mockedUser = new User(1,"test","test",testEmail,"test");
        when(mockDAO.getUserByEmail(testEmail)).thenReturn(mockedUser);

        boolean isEmailAvailability = userService.isEmailAvailableForUpdate(userId,testEmail);

        Assert.assertTrue(isEmailAvailability);
    }

    // Login test

    @Test
    public void loginWithNotExistingEmail(){ // SHOULD RETURN NULL
        String email = "test@gmail.com";
        String password = "Password1*";
        when(mockDAO.getUserByEmail(email)).thenReturn(null);

        User returnedUser = userService.loginUser(email, password);

        Assert.assertNull(returnedUser);
    }

    @Test
    public void loginWithIncorrectPassword(){ // SHOULD RETURN NULL
        String email = "test@gmail.com";
        String password = "Password1*";
        String incorrectPassword = "Password2*";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User mockedUser = new User(1,"test","test",email,hashedPassword);
        when(mockDAO.getUserByEmail(email)).thenReturn(mockedUser);

        User returnedUser = userService.loginUser(email, incorrectPassword);

        Assert.assertNull(returnedUser);

    }

    @Test
    public void loginWithCorrectCredentials(){ // SHOULD RETURN AN USER
        String email = "test@gmail.com";
        String password = "Password1*";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User mockedUser = new User(1,"test","test",email,hashedPassword);
        when(mockDAO.getUserByEmail(email)).thenReturn(mockedUser);

        User returnedUser = userService.loginUser(email, password);

        Assert.assertEquals(mockedUser, returnedUser);
    }

    // GETS

    @Test
    public void getAllUsersSuccess() { // SHOULD RETURN A LIST OF USERS
        // Arrange
        List<User> expectedUsers = Arrays.asList(
                new User(1, "John", "Doe", "john.doe@example.com", "hashedPassword1"),
                new User(2, "Jane", "Smith", "jane.smith@example.com", "hashedPassword2")
        );
        when(mockDAO.getAll()).thenReturn(expectedUsers);

        // Act
        List<User> actualUsers = userService.getAllUsers();

        // Assert
        Assert.assertEquals(expectedUsers, actualUsers);
    }


    @Test
    public void getUserByEmail() { // SHOULD RETURN A LIST OF USERS
        // Arrange
        String email = "testEmail";
        User u = new User(1, "John", "Doe", "testEmail", "hashedPassword1");

        when(mockDAO.getUserByEmail(email)).thenReturn(u);

        // Act
        User user = userService.getUserByEmail(email);

        // Assert
        Assert.assertEquals(u, user);
    }


    @Test
    public void getUserAddresses() { // SHOULD RETURN A LIST OF USERS
        // Arrange
        int userId = 1;
        List<UserAddress> expectedAddresses = Arrays.asList(
                new UserAddress(1, userId, "EUA","Houston","Texas","Main street","123","49000"),
                new UserAddress(2, userId, "EUA","Houston","Texas","Secondary street","321","49001")
        );
        UserAddressDAOImpl mockAddressDAO = Mockito.mock(UserAddressDAOImpl.class);
        UserService userServiceWithAddressDAO = new UserService(mockDAO, mockAddressDAO);

        when(mockAddressDAO.getUserAddress(userId)).thenReturn(expectedAddresses);

        // Act
        List<UserAddress> actualAddresses = userServiceWithAddressDAO.getUserAddresses(userId);

        // Assert
        Assert.assertEquals(expectedAddresses, actualAddresses);
    }

    @Test
    public void updateStatus() {
        // Arrange
        int userId = 1;
        boolean status = true;
        User expectedUser = new User(userId, "test", "test", "test", "test");
        when(mockDAO.updateStatus(userId, status)).thenReturn(expectedUser);

        // Act
        User actualUser = userService.updateStatus(userId, status);

        // Assert
        Assert.assertEquals(expectedUser, actualUser);
    }

    @Test
    public void registerNewUser() { // SHOULD RETURN USER
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String password = "Password1*";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User expectedUser = new User(1, firstName, lastName, email, hashedPassword, UserRole.USER, true); // Simulate the user with an ID after saving
        when(mockDAO.create(Mockito.any(User.class))).thenReturn(expectedUser);

        // Act
        User actualUser = userService.registerNewUser(firstName, lastName, email, password);

        // Assert
        Assert.assertEquals(expectedUser, actualUser);
    }

    @Test
    public void updateUser() { // SHOULD RETURN USER
        // Arrange
        int userId = 1;
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String password = "Password1*";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User expectedUser = new User(userId, firstName, lastName, email, hashedPassword, UserRole.USER, true); // Simulate the user with an ID after saving
        when(mockDAO.update(Mockito.any(User.class))).thenReturn(expectedUser);

        // Act
        User actualUser = userService.updateUser(userId, firstName, lastName, email, password);

        // Assert
        Assert.assertEquals(expectedUser, actualUser);
    }



}
