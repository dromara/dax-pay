package cn.daxpay.open.payment.check.checker.merchant;

import cn.daxpay.open.payment.check.checker.MerchantConfigChecker;
import cn.daxpay.open.payment.check.enums.ConfigCheckCategoryEnum;
import cn.daxpay.open.payment.check.model.ConfigCheckItem;
import cn.daxpay.open.payment.merchant.dao.appinfo.MchAppInfoManager;
import cn.daxpay.open.payment.merchant.entity.appinfo.MchAppInfo;
import cn.daxpay.open.platform.core.enums.merchant.MchAppStatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/// # 商户应用检查器
///
/// 检测当前商户下是否存在启用状态的应用。无启用应用则视为未配置。
@Component
@RequiredArgsConstructor
public class MchAppChecker implements MerchantConfigChecker {

    private final MchAppInfoManager mchAppInfoManager;

    @Override
    public ConfigCheckCategoryEnum getCategory() {
        return ConfigCheckCategoryEnum.MCH_APP;
    }

    @Override
    public ConfigCheckItem check(String mchNo) {
        List<MchAppInfo> apps = mchAppInfoManager.findAllByMchNo(mchNo);
        // 无任何应用 或 无启用状态的应用 => 告警
        long enabledCount = apps.stream()
                .filter(a -> MchAppStatusEnum.ENABLE.getCode().equals(a.getStatus()))
                .count();
        if (enabledCount == 0) {
            return ConfigCheckItem.of(
                    ConfigCheckCategoryEnum.MCH_APP,
                    ConfigCheckCategoryEnum.MCH_APP.getCode(),
                    "configCheck.mchApp.title",
                    "configCheck.mchApp.description",
                    "MchAppInfo"
            ).setCount(apps.isEmpty() ? 0 : (int) (apps.size() - enabledCount));
        }
        return null;
    }
}
