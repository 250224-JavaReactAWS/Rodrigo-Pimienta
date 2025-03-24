
import com.revature.models.CartItem;
import com.revature.models.Product;
import com.revature.repos.CartItemDaoImpl;
import com.revature.repos.ProductDAOImpl;
import com.revature.services.CartItemService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

public class CartItemsServicesTest {

    private CartItemService cartItemService;
    private CartItemDaoImpl mockCartItemDAO;
    private ProductDAOImpl mockProductDAO;

    @Before
    public void setup() {
        mockCartItemDAO = Mockito.mock(CartItemDaoImpl.class);
        mockProductDAO = Mockito.mock(ProductDAOImpl.class);
        cartItemService = new CartItemService(mockCartItemDAO, mockProductDAO);
    }

    @Test
    public void registerCartItem_CreatesAndReturnsCartItem() {
        int userId = 1;
        int productId = 1;
        int quantity = 2;
        CartItem expectedCartItem = new CartItem(1, userId, productId, quantity);
        when(mockCartItemDAO.create(Mockito.any(CartItem.class))).thenReturn(expectedCartItem);
        CartItem actualCartItem = cartItemService.registerCartItem(userId, productId, quantity);
        Assert.assertEquals(expectedCartItem, actualCartItem);
    }

    @Test
    public void isProductExist_ProductExists_ReturnsTrue() {
        int productId = 1;
        when(mockProductDAO.getById(productId)).thenReturn(new Product());
        Assert.assertTrue(cartItemService.isProductExist(productId));
    }

    @Test
    public void isProductExist_ProductDoesNotExist_ReturnsFalse() {
        int productId = 1;
        when(mockProductDAO.getById(productId)).thenReturn(null);
        Assert.assertFalse(cartItemService.isProductExist(productId));
    }

    @Test
    public void isProductActive_ProductInactive_ReturnsTrue() {
        int productId = 1;
        Product inactiveProduct = new Product();
        inactiveProduct.setActive(false);
        when(mockProductDAO.getById(productId)).thenReturn(inactiveProduct);
        Assert.assertTrue(cartItemService.isProductActive(productId));
    }

    @Test
    public void isProductActive_ProductActive_ReturnsFalse() {
        int productId = 1;
        Product activeProduct = new Product();
        activeProduct.setActive(true);
        when(mockProductDAO.getById(productId)).thenReturn(activeProduct);
        Assert.assertFalse(cartItemService.isProductActive(productId));
    }

    @Test
    public void isCartItemRelatedWithUserAndProduct_Related_ReturnsTrue() {
        int cartItemId = 1;
        int userId = 1;
        int productId = 1;
        CartItem relatedCartItem = new CartItem(cartItemId, userId, productId, 1);
        when(mockCartItemDAO.getCartItemByProductAndUser(userId, productId)).thenReturn(relatedCartItem);
        Assert.assertTrue(cartItemService.isCartItemRelatedWithUserAndProduct(cartItemId, userId, productId));
    }

    @Test
    public void isCartItemRelatedWithUserAndProduct_NotRelated_ReturnsFalse() {
        int cartItemId = 1;
        int userId = 1;
        int productId = 1;
        CartItem unrelatedCartItem = new CartItem(2, userId, productId, 1);
        when(mockCartItemDAO.getCartItemByProductAndUser(userId, productId)).thenReturn(unrelatedCartItem);
        Assert.assertFalse(cartItemService.isCartItemRelatedWithUserAndProduct(cartItemId, userId, productId));
    }

    @Test
    public void isCartItemRelatedWithUser_Related_ReturnsTrue() {
        int cartItemId = 1;
        int userId = 1;
        CartItem relatedCartItem = new CartItem(cartItemId, userId, 1, 1);
        when(mockCartItemDAO.getById(cartItemId)).thenReturn(relatedCartItem);
        Assert.assertTrue(cartItemService.isCartItemRelatedWithUser(cartItemId, userId));
    }

    @Test
    public void isCartItemRelatedWithUser_NotRelated_ReturnsFalse() {
        int cartItemId = 1;
        int userId = 1;
        CartItem unrelatedCartItem = new CartItem(cartItemId, 2, 1, 1);
        when(mockCartItemDAO.getById(cartItemId)).thenReturn(unrelatedCartItem);
        Assert.assertFalse(cartItemService.isCartItemRelatedWithUser(cartItemId, userId));
    }

    @Test
    public void isProductRelatedWithUserCart_ProductNotRelated_ReturnsTrue() {
        int productId = 1;
        int userId = 1;
        when(mockCartItemDAO.getCartItemByProductAndUser(userId, productId)).thenReturn(null);
        Assert.assertTrue(cartItemService.isProductRelatedWithUserCart(productId, userId));
    }

    @Test
    public void isProductRelatedWithUserCart_ProductRelated_ReturnsFalse() {
        int productId = 1;
        int userId = 1;
        when(mockCartItemDAO.getCartItemByProductAndUser(userId, productId)).thenReturn(new CartItem());
        Assert.assertFalse(cartItemService.isProductRelatedWithUserCart(productId, userId));
    }

    @Test
    public void getUserCartItems_ReturnsListOfCartItems() {
        int userId = 1;
        List<CartItem> expectedCartItems = Arrays.asList(new CartItem(), new CartItem());
        when(mockCartItemDAO.getCartItemsByUser(userId)).thenReturn(expectedCartItems);
        List<CartItem> actualCartItems = cartItemService.getUserCartItems(userId);
        Assert.assertEquals(expectedCartItems, actualCartItems);
    }

    @Test
    public void deleteCartItem_DeletesAndReturnsTrue() {
        int cartItemId = 1;
        when(mockCartItemDAO.deleteById(cartItemId)).thenReturn(true);
        Assert.assertTrue(cartItemService.deleteCartItem(cartItemId));
    }

    @Test
    public void deleteUserCartItems_DeletesAndReturnsTrue() {
        int userId = 1;
        when(mockCartItemDAO.deleteCartItemByUser(userId)).thenReturn(true);
        Assert.assertTrue(cartItemService.deleteUserCartItems(userId));
    }
}