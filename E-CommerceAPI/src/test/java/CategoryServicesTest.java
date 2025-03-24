import com.revature.models.Category;
import com.revature.models.Product;
import com.revature.repos.CategoryDAOImpl;
import com.revature.repos.ProductDAOImpl;
import com.revature.services.CategoryService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

public class CategoryServicesTest {

    private CategoryService categoryService;
    private CategoryDAOImpl mockCategoryDAO;
    private ProductDAOImpl mockProductDAO;

    @Before
    public void setup() {
        mockCategoryDAO = Mockito.mock(CategoryDAOImpl.class);
        mockProductDAO = Mockito.mock(ProductDAOImpl.class);
        categoryService = new CategoryService(mockCategoryDAO, mockProductDAO);
    }

    @Test
    public void isNameAvailable_NameDoesNotExist_ReturnsTrue() {
        String categoryName = "New Category";
        when(mockCategoryDAO.getCategoryByName(categoryName)).thenReturn(null);
        Assert.assertTrue(categoryService.isNameAvailable(categoryName));
    }

    @Test
    public void isNameAvailable_NameExists_ReturnsFalse() {
        String categoryName = "Existing Category";
        when(mockCategoryDAO.getCategoryByName(categoryName)).thenReturn(new Category());
        Assert.assertFalse(categoryService.isNameAvailable(categoryName));
    }

    @Test
    public void isNameAvailableForUpdate_NameDoesNotExist_ReturnsTrue() {
        int categoryId = 1;
        String categoryName = "New Category";
        when(mockCategoryDAO.getCategoryByName(categoryName)).thenReturn(null);
        Assert.assertTrue(categoryService.isNameAvailableForUpdate(categoryId, categoryName));
    }

    @Test
    public void isNameAvailableForUpdate_NameExistsSameId_ReturnsTrue() {
        int categoryId = 1;
        String categoryName = "Existing Category";
        Category existingCategory = new Category(categoryId, categoryName);
        when(mockCategoryDAO.getCategoryByName(categoryName)).thenReturn(existingCategory);
        Assert.assertTrue(categoryService.isNameAvailableForUpdate(categoryId, categoryName));
    }

    @Test
    public void isNameAvailableForUpdate_NameExistsDifferentId_ReturnsFalse() {
        int categoryId = 1;
        String categoryName = "Existing Category";
        Category existingCategory = new Category(2, categoryName);
        when(mockCategoryDAO.getCategoryByName(categoryName)).thenReturn(existingCategory);
        Assert.assertFalse(categoryService.isNameAvailableForUpdate(categoryId, categoryName));
    }

    @Test
    public void registerCategory_CreatesAndReturnsCategory() {
        String categoryName = "New Category";
        Category expectedCategory = new Category(1, categoryName);
        when(mockCategoryDAO.create(Mockito.any(Category.class))).thenReturn(expectedCategory);
        Category actualCategory = categoryService.registerCategory(categoryName);
        Assert.assertEquals(expectedCategory, actualCategory);
    }

    @Test
    public void getAllCategories_ReturnsListOfCategories() {
        List<Category> expectedCategories = Arrays.asList(new Category(1, "Category 1"), new Category(2, "Category 2"));
        when(mockCategoryDAO.getAll()).thenReturn(expectedCategories);
        List<Category> actualCategories = categoryService.getAllCategories();
        Assert.assertEquals(expectedCategories, actualCategories);
    }

    @Test
    public void getCategory_ReturnsCategory() {
        int categoryId = 1;
        Category expectedCategory = new Category(categoryId, "Category 1");
        when(mockCategoryDAO.getById(categoryId)).thenReturn(expectedCategory);
        Category actualCategory = categoryService.getCategory(categoryId);
        Assert.assertEquals(expectedCategory, actualCategory);
    }

    @Test
    public void getCategoryProducts_ReturnsListOfProducts() {
        int categoryId = 1;
        List<Product> expectedProducts = Arrays.asList(new Product(), new Product());
        when(mockProductDAO.getProductsByCategory(categoryId)).thenReturn(expectedProducts);
        List<Product> actualProducts = categoryService.getCategoryProducts(categoryId);
        Assert.assertEquals(expectedProducts, actualProducts);
    }

    @Test
    public void updateStatus_UpdatesAndReturnsCategory() {
        int categoryId = 1;
        boolean status = true;
        Category updatedCategory = new Category(categoryId, "Category 1");
        when(mockCategoryDAO.updateStatus(categoryId, status)).thenReturn(updatedCategory);
        Category actualCategory = categoryService.updateStatus(categoryId, status);
        Assert.assertEquals(updatedCategory, actualCategory);
    }
}