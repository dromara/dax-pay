package cn.daxpay.open.payment.check.checker.merchant;

import cn.daxpay.open.payment.check.checker.MerchantConfigChecker;
import cn.daxpay.open.payment.check.enums.ConfigCheckCategoryEnum;
import cn.daxpay.open.payment.check.model.ConfigCheckItem;
import cn.daxpay.open.payment.merchant.dao.appinfo.MchAppInfoManager;
import cn.daxpay.open.payment.merchant.dao.config.MchAppNotifyConfigManager;
import cn.daxpay.open.payment.merchant.entity.appinfo.MchAppInfo;
import cn.daxpay.open.payment.merchant.entity.config.MchAppNotifyConfig;
import cn.daxpay.open.platform.core.enums.merchant.MchAppStatusEnum;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/// # 商户通知配置检查器
///
/// 检测商户下启用的应用是否配置了有效的回调通知地址。
/// 任一启用应用的 notifyUrl 为空或未启用 => 告警。
@Component
@RequiredArgsConstructor
public class MchNotifyConfigChecker implements MerchantConfigChecker {

    private final MchAppInfoManager mchAppInfoManager;
    private final MchAppNotifyConfigManager mchAppNotifyConfigManager;

    @Override
    public ConfigCheckCategoryEnum getCategory() {
        return ConfigCheckCategoryEnum.MCH_NOTIFY;
    }

    @Override
    public ConfigCheckItem check(String mchNo) {
        List<MchAppInfo> enabledApps = mchAppInfoManager.findAllByMchNo(mchNo).stream()
                .filter(a -> MchAppStatusEnum.ENABLE.getCode().equals(a.getStatus()))
                .toList();
        // 无启用应用时由 MchAppChecker 告警, 通知检查器跳过
        if (enabledApps.isEmpty()) {
            return null;
        }
        // 统计启用应用中通知地址未配置或未启用的数量
        long missingCount = enabledApps.stream()
                .filter(app -> {
                    MchAppNotifyConfig cfg = mchAppNotifyConfigManager.findByAppId(app.getAppId()).orElse(null);
                    return cfg == null
                            || StrUtil.isBlank(cfg.getNotifyUrl())
                            || !Boolean.TRUE.equals(cfg.getStatus());
                })
                .count();
        if (missingCount > 0) {
            return ConfigCheckItem.of(
                    ConfigCheckCategoryEnum.MCH_NOTIFY,
                    ConfigCheckCategoryEnum.MCH_NOTIFY.getCode(),
                    "configCheck.mchNotify.title",
                    "configCheck.mchNotify.description",
                    "MchNotifyConfig"
            ).setCount((int) missingCount);
        }
        return null;
    }
}
