
import com.revature.models.ProductReview;
import com.revature.repos.OrderItemDAOImpl;
import com.revature.repos.ProductReviewDAOImpl;
import com.revature.services.ProductReviewService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

public class ProductReviewServicesTest {

    private ProductReviewService productReviewService;
    private ProductReviewDAOImpl mockProductReviewDAO;
    private OrderItemDAOImpl mockOrderItemDAO;

    @Before
    public void setup() {
        mockProductReviewDAO = Mockito.mock(ProductReviewDAOImpl.class);
        mockOrderItemDAO = Mockito.mock(OrderItemDAOImpl.class);
        productReviewService = new ProductReviewService(mockProductReviewDAO, mockOrderItemDAO);
    }

    @Test
    public void hasUserBoughtThisProduct_UserHasBought_ReturnsTrue() {
        int userId = 1;
        int productId = 1;
        when(mockOrderItemDAO.getItemByProductAndUser(userId, productId)).thenReturn(true);
        Assert.assertTrue(productReviewService.hasUserBoughtThisProduct(userId, productId));
    }

    @Test
    public void hasUserBoughtThisProduct_UserHasNotBought_ReturnsFalse() {
        int userId = 1;
        int productId = 1;
        when(mockOrderItemDAO.getItemByProductAndUser(userId, productId)).thenReturn(false);
        Assert.assertFalse(productReviewService.hasUserBoughtThisProduct(userId, productId));
    }

    @Test
    public void registerReview_CreatesAndReturnsReview() {
        int userId = 1;
        int productId = 1;
        String comment = "Good product";
        short rating = 5;
        ProductReview expectedReview = new ProductReview(1, userId, productId, comment, rating);
        when(mockProductReviewDAO.create(Mockito.any(ProductReview.class))).thenReturn(expectedReview);
        ProductReview actualReview = productReviewService.registerReview(userId, productId, comment, rating);
        Assert.assertEquals(expectedReview, actualReview);
    }

    @Test
    public void getAllProductsReviews_ReturnsListOfReviews() {
        List<ProductReview> expectedReviews = Arrays.asList(new ProductReview(), new ProductReview());
        when(mockProductReviewDAO.getAll()).thenReturn(expectedReviews);
        List<ProductReview> actualReviews = productReviewService.getAllProductsReviews();
        Assert.assertEquals(expectedReviews, actualReviews);
    }

    @Test
    public void getUserReviews_ReturnsListOfReviews() {
        int userId = 1;
        List<ProductReview> expectedReviews = Arrays.asList(new ProductReview(), new ProductReview());
        when(mockProductReviewDAO.getUserReviews(userId)).thenReturn(expectedReviews);
        List<ProductReview> actualReviews = productReviewService.getUserReviews(userId);
        Assert.assertEquals(expectedReviews, actualReviews);
    }

    @Test
    public void getAllProductReviews_ReturnsListOfReviews() {
        int productId = 1;
        List<ProductReview> expectedReviews = Arrays.asList(new ProductReview(), new ProductReview());
        when(mockProductReviewDAO.getProductReviewsByProduct(productId)).thenReturn(expectedReviews);
        List<ProductReview> actualReviews = productReviewService.getAllProductReviews(productId);
        Assert.assertEquals(expectedReviews, actualReviews);
    }

    @Test
    public void getReview_ReturnsReview() {
        int reviewId = 1;
        ProductReview expectedReview = new ProductReview();
        when(mockProductReviewDAO.getById(reviewId)).thenReturn(expectedReview);
        ProductReview actualReview = productReviewService.getReview(reviewId);
        Assert.assertEquals(expectedReview, actualReview);
    }
}