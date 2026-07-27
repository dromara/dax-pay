package cn.daxpay.open.payment.common.check.checker.merchant;

import cn.daxpay.open.payment.common.check.checker.MerchantConfigChecker;
import cn.daxpay.open.payment.common.check.enums.ConfigCheckCategoryEnum;
import cn.daxpay.open.payment.common.check.model.ConfigCheckItem;
import cn.daxpay.open.payment.merchant.dao.appinfo.MchAppInfoManager;
import cn.daxpay.open.payment.merchant.entity.appinfo.MchAppInfo;
import cn.daxpay.open.payment.route.dao.strategy.PayRouteStrategyManager;
import cn.daxpay.open.platform.core.enums.merchant.MchAppStatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/// # 支付路由检查器
///
/// 检测商户下启用的应用是否配置了支付路由策略。
/// 任一启用应用无路由策略记录 => 告警(深入子表校验由支付运行时承担, 配置态仅校验策略记录存在性)。
@Component
@RequiredArgsConstructor
public class PayRouteChecker implements MerchantConfigChecker {

    private final MchAppInfoManager mchAppInfoManager;
    private final PayRouteStrategyManager payRouteStrategyManager;

    @Override
    public ConfigCheckCategoryEnum getCategory() {
        return ConfigCheckCategoryEnum.PAY_ROUTE;
    }

    @Override
    public ConfigCheckItem check(String mchNo) {
        List<MchAppInfo> enabledApps = mchAppInfoManager.findAllByMchNo(mchNo).stream()
                .filter(a -> MchAppStatusEnum.ENABLE.getCode().equals(a.getStatus()))
                .toList();
        // 无启用应用时由 MchAppChecker 告警, 路由检查器跳过
        if (enabledApps.isEmpty()) {
            return null;
        }
        // 统计启用应用中无路由策略记录的数量
        long missingCount = enabledApps.stream()
                .filter(app -> payRouteStrategyManager.findByAppId(app.getAppId()).isEmpty())
                .count();
        if (missingCount > 0) {
            return ConfigCheckItem.of(
                    ConfigCheckCategoryEnum.PAY_ROUTE,
                    ConfigCheckCategoryEnum.PAY_ROUTE.getCode(),
                    "configCheck.payRoute.title",
                    "configCheck.payRoute.description",
                    "PayRouteConfig"
            ).setCount((int) missingCount);
        }
        return null;
    }
}
