import com.revature.models.UserAddress;
import com.revature.repos.UserAddressDAO;
import com.revature.services.UserAddressesService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;

public class UserAddressServicesTest {

    private UserAddressesService userAddressService;
    private UserAddressDAO mockDAO;

    @Before
    public void setup() {
        mockDAO = Mockito.mock(UserAddressDAO.class);
        userAddressService = new UserAddressesService(mockDAO);
    }

    @Test
    public void registerNewAddress() {
        // Arrange
        int userId = 1;
        String country = "USA";
        String state = "CA";
        String city = "Any town";
        String street = "Main St";
        String houseNumber = "123";
        String postalCode = "12345";
        UserAddress expectedAddress = new UserAddress(1, userId, country, state, city, street, houseNumber, postalCode);
        when(mockDAO.create(Mockito.any(UserAddress.class))).thenReturn(expectedAddress);

        // Act
        UserAddress actualAddress = userAddressService.registerNewAddress(userId, country, state, city, street, houseNumber, postalCode);

        // Assert
        Assert.assertEquals(expectedAddress, actualAddress);
    }

    @Test
    public void getUserAddress() {
        // Arrange
        int userAddressId = 1;
        int userId = 1;
        String country = "USA";
        String state = "CA";
        String city = "Any town";
        String street = "Main St";
        String houseNumber = "123";
        String postalCode = "12345";
        UserAddress expectedAddress = new UserAddress(userAddressId, userId, country, state, city, street, houseNumber, postalCode);
        when(mockDAO.getById(userAddressId)).thenReturn(expectedAddress);

        // Act
        UserAddress actualAddress = userAddressService.getUserAddress(userAddressId);

        // Assert
        Assert.assertEquals(expectedAddress, actualAddress);
    }

    @Test
    public void updateAddress() {
        // Arrange
        int userAddressId = 1;
        int userId = 1;
        String country = "USA";
        String state = "CA";
        String city = "Any town";
        String street = "Main St";
        String houseNumber = "123";
        String postalCode = "12345";
        UserAddress expectedAddress = new UserAddress(userAddressId, userId, country, state, city, street, houseNumber, postalCode);
        when(mockDAO.update(Mockito.any(UserAddress.class))).thenReturn(expectedAddress);

        // Act
        UserAddress actualAddress = userAddressService.updateAddress(userAddressId, userId, country, state, city, street, houseNumber, postalCode);

        // Assert
        Assert.assertEquals(expectedAddress, actualAddress);
    }

    @Test
    public void updateStatus() {
        // Arrange
        int userId = 1;
        boolean status = true;
        UserAddress expectedAddress = new UserAddress(1, userId, "USA", "CA", "Any town", "Main St", "123", "12345");
        when(mockDAO.updateStatus(userId, status)).thenReturn(expectedAddress);

        // Act
        UserAddress actualAddress = userAddressService.updateStatus(userId, status);

        // Assert
        Assert.assertEquals(expectedAddress, actualAddress);
    }
}