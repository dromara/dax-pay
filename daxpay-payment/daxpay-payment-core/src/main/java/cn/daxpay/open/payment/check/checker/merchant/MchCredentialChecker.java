package cn.daxpay.open.payment.check.checker.merchant;

import cn.daxpay.open.payment.check.checker.MerchantConfigChecker;
import cn.daxpay.open.payment.check.enums.ConfigCheckCategoryEnum;
import cn.daxpay.open.payment.check.model.ConfigCheckItem;
import cn.daxpay.open.payment.merchant.dao.config.MerchantCredentialManager;
import cn.daxpay.open.payment.merchant.entity.config.MerchantCredential;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/// # 商户 API 凭证检查器
///
/// 检测商户通信凭证(公钥/密钥)是否已生成。任一为空视为未配置。
@Component
@RequiredArgsConstructor
public class MchCredentialChecker implements MerchantConfigChecker {

    private final MerchantCredentialManager merchantCredentialManager;

    @Override
    public ConfigCheckCategoryEnum getCategory() {
        return ConfigCheckCategoryEnum.MCH_CREDENTIAL;
    }

    @Override
    public ConfigCheckItem check(String mchNo) {
        Optional<MerchantCredential> credential = merchantCredentialManager.findByMchNo(mchNo);
        // 无凭证记录 或 公钥/密钥任一为空 => 告警
        boolean unconfigured = credential.isEmpty()
                || StrUtil.isBlank(credential.get().getPublicKey())
                || StrUtil.isBlank(credential.get().getSecretKey());
        if (unconfigured) {
            return ConfigCheckItem.of(
                    ConfigCheckCategoryEnum.MCH_CREDENTIAL,
                    ConfigCheckCategoryEnum.MCH_CREDENTIAL.getCode(),
                    "configCheck.mchCredential.title",
                    "configCheck.mchCredential.description",
                    "MchCredential"
            );
        }
        return null;
    }
}
