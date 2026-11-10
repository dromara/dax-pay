package cn.daxpay.open.payment.merchant.check.checker;

import cn.daxpay.open.payment.common.check.checker.MerchantConfigChecker;
import cn.daxpay.open.payment.common.check.enums.ConfigCheckCategoryEnum;
import cn.daxpay.open.payment.common.check.model.ConfigCheckItem;
import cn.daxpay.open.payment.merchant.dao.config.MerchantCredentialManager;
import cn.daxpay.open.payment.merchant.entity.config.MerchantCredential;
import cn.daxpay.open.payment.merchant.service.config.MerchantCredentialService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/// # 商户 API 凭证检查器
///
/// 检测商户通信凭证(公钥/密钥)是否已生成。任一为空视为未配置。
/// 判定口径复用 [MerchantCredentialService#isConfigured], 与凭证配置页/商户工作台的配置状态一致。
@Component
@Order(2)
@RequiredArgsConstructor
public class MchCredentialChecker implements MerchantConfigChecker {

    private final MerchantCredentialManager merchantCredentialManager;

    @Override
    public ConfigCheckCategoryEnum getCategory() {
        return ConfigCheckCategoryEnum.MCH_CREDENTIAL;
    }

    @Override
    public ConfigCheckItem check(String mchNo) {
        // 只读查询(不走 MerchantCredentialService#findByMchNo, 避免无记录时自动建空行)
        MerchantCredential credential = merchantCredentialManager.findByMchNo(mchNo).orElse(null);
        // 无凭证记录 或 公钥/密钥任一为空 => 告警
        if (!MerchantCredentialService.isConfigured(credential)) {
            return ConfigCheckItem.of(
                    ConfigCheckCategoryEnum.MCH_CREDENTIAL,
                    ConfigCheckCategoryEnum.MCH_CREDENTIAL.getCode(),
                    "configCheck.mchCredential.title",
                    "configCheck.mchCredential.description",
                    "/mch/credential"
            );
        }
        return null;
    }
}
