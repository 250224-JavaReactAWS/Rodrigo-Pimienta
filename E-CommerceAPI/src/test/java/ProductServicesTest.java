
import com.revature.models.Category;
import com.revature.models.Product;
import com.revature.repos.CategoryDAOImpl;
import com.revature.repos.ProductDAOImpl;
import com.revature.services.ProductService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

public class ProductServicesTest {

    private ProductService productService;
    private ProductDAOImpl mockProductDAO;
    private CategoryDAOImpl mockCategoryDAO;

    @Before
    public void setup() {
        mockProductDAO = Mockito.mock(ProductDAOImpl.class);
        mockCategoryDAO = Mockito.mock(CategoryDAOImpl.class);
        productService = new ProductService(mockProductDAO, mockCategoryDAO);
    }

    @Test
    public void isCategoryExist_CategoryDoesNotExist_ReturnsTrue() {
        int categoryId = 1;
        when(mockCategoryDAO.getById(categoryId)).thenReturn(null);
        Assert.assertTrue(productService.isCategoryExist(categoryId));
    }

    @Test
    public void isCategoryExist_CategoryExists_ReturnsFalse() {
        int categoryId = 1;
        when(mockCategoryDAO.getById(categoryId)).thenReturn(new Category());
        Assert.assertFalse(productService.isCategoryExist(categoryId));
    }

    @Test
    public void isCategoryActive_CategoryInactive_ReturnsTrue() {
        int categoryId = 1;
        Category inactiveCategory = new Category();
        inactiveCategory.setActive(false);
        when(mockCategoryDAO.getById(categoryId)).thenReturn(inactiveCategory);
        Assert.assertTrue(productService.isCategoryActive(categoryId));
    }

    @Test
    public void isCategoryActive_CategoryActive_ReturnsFalse() {
        int categoryId = 1;
        Category activeCategory = new Category();
        activeCategory.setActive(true);
        when(mockCategoryDAO.getById(categoryId)).thenReturn(activeCategory);
        Assert.assertFalse(productService.isCategoryActive(categoryId));
    }

    @Test
    public void registerNewProduct_CreatesAndReturnsProduct() {
        int categoryId = 1;
        String name = "Product Name";
        String description = "Product Description";
        Double price = 10.0;
        int stock = 5;
        Product expectedProduct = new Product(1, categoryId, name, description, price, stock);
        when(mockProductDAO.create(Mockito.any(Product.class))).thenReturn(expectedProduct);
        Product actualProduct = productService.registerNewProduct(categoryId, name, description, price, stock);
        Assert.assertEquals(expectedProduct, actualProduct);
    }

    @Test
    public void getAllProducts_ReturnsListOfProducts() {
        List<Product> expectedProducts = Arrays.asList(new Product(), new Product());
        when(mockProductDAO.getAll()).thenReturn(expectedProducts);
        List<Product> actualProducts = productService.getAllProducts();
        Assert.assertEquals(expectedProducts, actualProducts);
    }

    @Test
    public void getProduct_ReturnsProduct() {
        int productId = 1;
        Product expectedProduct = new Product();
        when(mockProductDAO.getById(productId)).thenReturn(expectedProduct);
        Product actualProduct = productService.getProduct(productId);
        Assert.assertEquals(expectedProduct, actualProduct);
    }

    @Test
    public void updateStatus_UpdatesAndReturnsProduct() {
        int productId = 1;
        boolean status = true;
        Product updatedProduct = new Product();
        when(mockProductDAO.updateStatusById(productId, status)).thenReturn(updatedProduct);
        Product actualProduct = productService.updateStatus(productId, status);
        Assert.assertEquals(updatedProduct, actualProduct);
    }
}