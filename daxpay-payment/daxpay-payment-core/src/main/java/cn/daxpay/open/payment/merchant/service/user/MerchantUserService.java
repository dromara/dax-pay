package cn.daxpay.open.payment.merchant.service.user;

import cn.daxpay.open.payment.merchant.dao.info.MerchantUserManager;
import cn.daxpay.open.payment.merchant.entity.info.MerchantUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 商户用户管理服务
///
/// 商户账号不提供公开自助注册(商户开通统一走管理端 /admin/merchant/add);
/// 忘记密码无自助找回入口, 由商户管理员(merchant 端)或平台运营(admin 端)线下重置。
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantUserService {

    private final MerchantUserManager merchantUserManager;

    /// 根据用户id查询商户号
    public String findMchNoByUserId(Long userId) {
        return merchantUserManager.findByUserId(userId)
                .map(MerchantUser::getMchNo)
                .orElse(null);
    }

}
