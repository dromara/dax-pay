package cn.daxpay.open.payment.merchant.service.user;

import cn.daxpay.open.platform.iam.auth.service.IamSecurityConfigService;
import cn.daxpay.open.platform.iam.auth.service.PasswordDecryptService;
import cn.daxpay.open.platform.iam.auth.service.PasswordPolicyService;
import cn.daxpay.open.platform.iam.auth.service.email.UserEmailService;
import cn.daxpay.open.platform.iam.dao.role.RoleManager;
import cn.daxpay.open.platform.iam.dao.upms.UserRoleManager;
import cn.daxpay.open.platform.iam.dao.user.UserExpandInfoManager;
import cn.daxpay.open.platform.iam.dao.user.UserInfoManager;
import cn.daxpay.open.platform.iam.dao.user.UserPasswordSecurityManager;
import cn.daxpay.open.platform.iam.service.client.ClientCodeService;
import cn.daxpay.open.platform.iam.service.upms.UserRoleService;
import cn.daxpay.open.platform.iam.service.user.UserAdminService;
import cn.daxpay.open.platform.iam.service.user.UserQueryService;
import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.merchant.dao.info.MerchantInfoManager;
import cn.daxpay.open.payment.merchant.dao.info.MerchantUserManager;
import cn.daxpay.open.payment.merchant.entity.info.MerchantUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/// # 商户用户级联注销测试(跨主体护栏)
///
/// 覆盖 [MerchantUserAdminService#deleteByMchNo] 的两类分支:
/// 无其他挂靠正常注销 / 仍挂靠其他商户仅解绑。
/// (开源版无代理商域, plus 侧"仍挂靠代理商"分支不适用)
@ExtendWith(MockitoExtension.class)
class MerchantUserAdminServiceTest {

    @Mock
    private UserInfoManager userInfoManager;
    @Mock
    private UserExpandInfoManager userExpandInfoManager;
    @Mock
    private MerchantUserManager merchantUserManager;
    @Mock
    private MerchantInfoManager merchantInfoManager;
    @Mock
    private PasswordDecryptService passwordDecryptService;
    @Mock
    private PasswordPolicyService passwordPolicyService;
    @Mock
    private UserPasswordSecurityManager userPasswordSecurityManager;
    @Mock
    private UserRoleService userRoleService;
    @Mock
    private UserRoleManager userRoleManager;
    @Mock
    private RoleManager roleManager;
    @Mock
    private UserQueryService userQueryService;
    @Mock
    private UserAdminService userAdminService;
    @Mock
    private IamSecurityConfigService iamSecurityConfigService;
    @Mock
    private UserEmailService userEmailService;
    @Mock
    private PaymentContext paymentContext;
    @Mock
    private ClientCodeService clientCodeService;

    @InjectMocks
    private MerchantUserAdminService service;

    @Test
    @DisplayName("无其他挂靠的用户随主体正常注销")
    void deleteByMchNo_noOtherAffiliation_shouldDelegateIamDelete() {
        MerchantUser user = new MerchantUser(103L, "M123", false);
        when(merchantUserManager.findAllByField(any(), eq("M123"))).thenReturn(List.of(user));
        when(merchantUserManager.findByUserId(103L)).thenReturn(Optional.empty());

        service.deleteByMchNo("M123");

        verify(merchantUserManager).deleteByField(any(), eq(103L));
        verify(userAdminService).delete(103L);
    }

    @Test
    @DisplayName("仍挂靠其他商户的用户仅解绑不注销")
    void deleteByMchNo_userStillInOtherMerchant_shouldSkipIamDelete() {
        MerchantUser user = new MerchantUser(101L, "M123", false);
        when(merchantUserManager.findAllByField(any(), eq("M123"))).thenReturn(List.of(user));
        when(merchantUserManager.findByUserId(101L)).thenReturn(Optional.of(new MerchantUser(101L, "M999", false)));

        service.deleteByMchNo("M123");

        verify(merchantUserManager).deleteByField(any(), eq(101L));
        verify(userAdminService, never()).delete(101L);
    }
}
