package cn.daxpay.open.payment.merchant.service.user;

import cn.daxpay.open.payment.merchant.dao.info.MerchantUserManager;
import cn.daxpay.open.payment.merchant.entity.info.MerchantUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 商户用户管理服务
///
/// 商户账号不提供公开自助注册(商户开通统一走管理端 /admin/merchant/add)。
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

    /// 注册商户(已下线: 商户开通统一由管理端操作, 见 MerchantAdminService#add)
    /// 历史公开自助注册入口 /mch/user/register 已于 2026-08 移除

    /// 忘记密码(已下线: 2026-08 移除, 待短信通道对接后重新实现)
    /// 历史公开找回入口 /mch/user/forgot/change-pwd 已于 2026-08 移除;
    /// 移除原因: 无短信通道时"手机号核验"仅为输入比对, 不构成持有验证;
    /// 现行兜底: 商户用户密码由商户管理员(merchant 端)或平台运营(admin 端)线下重置。
}
