
import com.revature.config.TimeZoneConfig;
import com.revature.models.Discount;
import com.revature.repos.DiscountDAOImpl;
import com.revature.services.DiscountService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

public class DiscountServicesTest {

    private DiscountService discountService;
    private DiscountDAOImpl mockDAO;

    @Before
    public void setup() {
        mockDAO = Mockito.mock(DiscountDAOImpl.class);
        discountService = new DiscountService(mockDAO);
    }

    @Test
    public void registerDiscount_CreatesAndReturnsDiscount() {
        String code = "DISCOUNT10";
        Double percentage = 10.0;
        short maxUsesPerUser = 1;
        OffsetDateTime expiredAt = OffsetDateTime.now(ZoneId.of(TimeZoneConfig.ZONE_ID)).plusMinutes(5);
        Discount expectedDiscount = new Discount(1, code, percentage, maxUsesPerUser, expiredAt, true, maxUsesPerUser);
        when(mockDAO.create(Mockito.any(Discount.class))).thenReturn(expectedDiscount);
        Discount actualDiscount = discountService.registerDiscount(code, percentage, maxUsesPerUser, expiredAt);
        Assert.assertEquals(expectedDiscount, actualDiscount);
    }

    @Test
    public void validateDiscount_InvalidLength_ReturnsFalse() {
        String code = "SHORT";
        Assert.assertFalse(discountService.validateDiscount(code));
    }

    @Test
    public void validateDiscount_ValidCode_ReturnsTrue() {
        String code = "aA1bCdEfGh";
        Assert.assertTrue(discountService.validateDiscount(code));
    }

    @Test
    public void isDiscountExist_CodeDoesNotExist_ReturnsTrue() {
        String code = "NEWCODE";
        when(mockDAO.getDiscountByCode(code)).thenReturn(null);
        Assert.assertTrue(discountService.isDiscountExist(code));
    }

    @Test
    public void isDiscountExist_CodeExists_ReturnsFalse() {
        String code = "EXISTCODE";
        when(mockDAO.getDiscountByCode(code)).thenReturn(new Discount());
        Assert.assertFalse(discountService.isDiscountExist(code));
    }

    @Test
    public void isDiscountAvailable_CodeDoesNotExist_ReturnsFalse() {
        String code = "NONEXISTCODE";
        int userId = 1;
        when(mockDAO.getCodeByUserAndCode(code, userId)).thenReturn(null);
        Assert.assertFalse(discountService.isDiscountAvailable(code, userId));
    }

    @Test
    public void isDiscountAvailable_CodeInactive_ReturnsFalse() {
        String code = "INACTIVECODE";
        int userId = 1;
        Discount inactiveDiscount = new Discount();
        inactiveDiscount.setActive(false);
        when(mockDAO.getCodeByUserAndCode(code, userId)).thenReturn(inactiveDiscount);
        Assert.assertFalse(discountService.isDiscountAvailable(code, userId));
    }

    @Test
    public void isDiscountAvailable_CodeExpired_ReturnsFalse() {
        String code = "EXPIREDCODE";
        int userId = 1;
        Discount expiredDiscount = new Discount();
        expiredDiscount.setActive(true);
        expiredDiscount.setExpiredAt(OffsetDateTime.now(ZoneId.of(TimeZoneConfig.ZONE_ID)).minusMinutes(1));
        when(mockDAO.getCodeByUserAndCode(code, userId)).thenReturn(expiredDiscount);
        Assert.assertFalse(discountService.isDiscountAvailable(code, userId));
    }

    @Test
    public void isDiscountAvailable_NoRemainUses_ReturnsFalse() {
        String code = "NOREMAINCODE";
        int userId = 1;
        Discount noRemainDiscount = new Discount();
        noRemainDiscount.setActive(true);
        noRemainDiscount.setExpiredAt(OffsetDateTime.now(ZoneId.of(TimeZoneConfig.ZONE_ID)).plusMinutes(1));
        noRemainDiscount.setRemainUses((short) 0);
        when(mockDAO.getCodeByUserAndCode(code, userId)).thenReturn(noRemainDiscount);
        Assert.assertFalse(discountService.isDiscountAvailable(code, userId));
    }

    @Test
    public void isDiscountAvailable_CodeValid_ReturnsTrue() {
        String code = "VALIDCODE";
        int userId = 1;
        Discount validDiscount = new Discount();
        validDiscount.setActive(true);
        validDiscount.setExpiredAt(OffsetDateTime.now(ZoneId.of(TimeZoneConfig.ZONE_ID)).plusMinutes(1));
        validDiscount.setRemainUses((short) 1);
        when(mockDAO.getCodeByUserAndCode(code, userId)).thenReturn(validDiscount);
        Assert.assertTrue(discountService.isDiscountAvailable(code, userId));
    }

    @Test
    public void getAllDiscounts_ReturnsListOfDiscounts() {
        List<Discount> expectedDiscounts = Arrays.asList(new Discount(), new Discount());
        when(mockDAO.getAll()).thenReturn(expectedDiscounts);
        List<Discount> actualDiscounts = discountService.getAllDiscounts();
        Assert.assertEquals(expectedDiscounts, actualDiscounts);
    }

    @Test
    public void getAvailableUserDiscounts_ReturnsListOfDiscounts() {
        int userId = 1;
        List<Discount> expectedDiscounts = Arrays.asList(new Discount(), new Discount());
        when(mockDAO.getAvailableDiscountsByUser(userId)).thenReturn(expectedDiscounts);
        List<Discount> actualDiscounts = discountService.getAvailableUserDiscounts(userId);
        Assert.assertEquals(expectedDiscounts, actualDiscounts);
    }

    @Test
    public void getDiscount_ReturnsDiscount() {
        int discountId = 1;
        Discount expectedDiscount = new Discount();
        when(mockDAO.getById(discountId)).thenReturn(expectedDiscount);
        Discount actualDiscount = discountService.getDiscount(discountId);
        Assert.assertEquals(expectedDiscount, actualDiscount);
    }

    @Test
    public void getDiscountByCode_ReturnsDiscount() {
        String code = "GETCODE";
        Discount expectedDiscount = new Discount();
        when(mockDAO.getDiscountByCode(code)).thenReturn(expectedDiscount);
        Discount actualDiscount = discountService.getDiscountByCode(code);
        Assert.assertEquals(expectedDiscount, actualDiscount);
    }

    @Test
    public void updateDiscount_UpdatesAndReturnsDiscount() {
        int discountId = 1;
        Double percentage = 15.0;
        short maxUsesPerUser = 2;
        OffsetDateTime expiredAt = OffsetDateTime.now(ZoneId.of(TimeZoneConfig.ZONE_ID)).plusMinutes(10);
        Discount updatedDiscount = new Discount(discountId, null, percentage, maxUsesPerUser, expiredAt, true);
        when(mockDAO.update(Mockito.any(Discount.class))).thenReturn(updatedDiscount);
        Discount actualDiscount = discountService.updateDiscount(discountId, percentage, maxUsesPerUser, expiredAt);
        Assert.assertEquals(updatedDiscount, actualDiscount);
    }

    @Test
    public void updateStatus_UpdatesAndReturnsDiscount() {
        int discountId = 1;
        boolean status = false;
        Discount updatedDiscount = new Discount();
        when(mockDAO.updateStatus(discountId, status)).thenReturn(updatedDiscount);
        Discount actualDiscount = discountService.updateStatus(discountId, status);
        Assert.assertEquals(updatedDiscount, actualDiscount);
    }
}