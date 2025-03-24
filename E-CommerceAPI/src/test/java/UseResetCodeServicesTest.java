
import com.revature.config.TimeZoneConfig;
import com.revature.models.UserResetCode;
import com.revature.repos.UserResetCodeDAOImpl;
import com.revature.services.UserResetCodeService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.OffsetDateTime;
import java.time.ZoneId;

import static org.mockito.Mockito.when;

public class UseResetCodeServicesTest {

    private UserResetCodeService userResetCodeService;
    private UserResetCodeDAOImpl mockDAO;

    @Before
    public void setup() {
        mockDAO = Mockito.mock(UserResetCodeDAOImpl.class);
        userResetCodeService = new UserResetCodeService(mockDAO);
    }

    @Test
    public void validateResetCode_InvalidLength_ReturnsFalse() {
        String resetCode = "short";

        boolean res = userResetCodeService.validateResetCode(resetCode);

        Assert.assertFalse(res);
    }

    @Test
    public void validateResetCode_ValidCode_ReturnsTrue() {
        String resetCode = "aA1!bCdEfg";

        boolean res = userResetCodeService.validateResetCode(resetCode);

        Assert.assertTrue(res);
    }

    @Test
    public void isResetCodeExist_CodeDoesNotExist_ReturnsTrue() {
        String resetCode = "uniqueCode";
        when(mockDAO.getResetCodeByCode(resetCode)).thenReturn(null);

        boolean res = userResetCodeService.isResetCodeExist(resetCode);
        Assert.assertTrue(res);
    }

    @Test
    public void isResetCodeExist_CodeExists_ReturnsFalse() {
        String resetCode = "existingCode";
        when(mockDAO.getResetCodeByCode(resetCode)).thenReturn(new UserResetCode());
        Assert.assertFalse(userResetCodeService.isResetCodeExist(resetCode));
    }

    @Test
    public void isResetCodeAvailable_CodeDoesNotExist_ReturnsFalse() {
        String resetCode = "nonexistentCode";
        when(mockDAO.getResetCodeByCode(resetCode)).thenReturn(null);
        Assert.assertFalse(userResetCodeService.isResetCodeAvailable(resetCode));
    }

    @Test
    public void isResetCodeAvailable_CodeUsed_ReturnsFalse() {
        String resetCode = "usedCode";
        UserResetCode usedCode = new UserResetCode();
        usedCode.setUsed(true);
        when(mockDAO.getResetCodeByCode(resetCode)).thenReturn(usedCode);
        Assert.assertFalse(userResetCodeService.isResetCodeAvailable(resetCode));
    }

    @Test
    public void isResetCodeAvailable_CodeExpired_ReturnsFalse() {
        String resetCode = "expiredCode";
        UserResetCode expiredCode = new UserResetCode();
        expiredCode.setExpiredAt(OffsetDateTime.now(ZoneId.of(TimeZoneConfig.ZONE_ID)).minusMinutes(1));
        when(mockDAO.getResetCodeByCode(resetCode)).thenReturn(expiredCode);
        Assert.assertFalse(userResetCodeService.isResetCodeAvailable(resetCode));
    }

    @Test
    public void isResetCodeAvailable_CodeValid_ReturnsTrue() {
        String resetCode = "validCode";
        UserResetCode validCode = new UserResetCode();
        validCode.setExpiredAt(OffsetDateTime.now(ZoneId.of(TimeZoneConfig.ZONE_ID)).plusMinutes(1));
        when(mockDAO.getResetCodeByCode(resetCode)).thenReturn(validCode);
        Assert.assertTrue(userResetCodeService.isResetCodeAvailable(resetCode));
    }

    @Test
    public void registerNewResetCode_CreatesAndReturnsCode() {
        int userId = 1;
        String resetCode = "newCode";
        UserResetCode expectedCode = new UserResetCode(1, userId, resetCode, false, OffsetDateTime.now(ZoneId.of(TimeZoneConfig.ZONE_ID)).plusMinutes(5));
        when(mockDAO.create(Mockito.any(UserResetCode.class))).thenReturn(expectedCode);
        UserResetCode actualCode = userResetCodeService.registerNewResetCode(userId, resetCode);
        Assert.assertEquals(expectedCode, actualCode);
    }

    @Test
    public void getResetCodeByCode_ReturnsCode() {
        String resetCode = "getCode";
        UserResetCode expectedCode = new UserResetCode();
        when(mockDAO.getResetCodeByCode(resetCode)).thenReturn(expectedCode);
        UserResetCode actualCode = userResetCodeService.getResetCodeByCode(resetCode);
        Assert.assertEquals(expectedCode, actualCode);
    }

    @Test
    public void getActiveUserResetCode_ReturnsCode() {
        int userId = 1;
        UserResetCode expectedCode = new UserResetCode();
        when(mockDAO.getActiveResetCodeByUser(userId)).thenReturn(expectedCode);
        UserResetCode actualCode = userResetCodeService.getActiveUserResetCode(userId);
        Assert.assertEquals(expectedCode, actualCode);
    }
}