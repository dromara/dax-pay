package cn.daxpay.open.payment.merchant.check.checker;

import cn.daxpay.open.payment.common.check.checker.MerchantConfigChecker;
import cn.daxpay.open.payment.common.check.enums.ConfigCheckCategoryEnum;
import cn.daxpay.open.payment.common.check.model.ConfigCheckItem;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/// # 商户通知配置检查器
///
/// 检测商户下启用的应用是否配置了有效的回调通知地址。
/// 任一启用应用的 notifyUrl 为空或未启用 => 告警。
///
/// 注意: 异步通知配置功能建设中(各端入口已切换为建设中占位, 不开放配置),
/// 配置不可用期间本检查器同步停用, 避免巡检告警引导用户前往不可用的配置页;
/// 恢复配置入口时需一并还原本检查逻辑(原实现见 git 历史)。
@Component
@Order(5)
public class MchNotifyConfigChecker implements MerchantConfigChecker {

    @Override
    public ConfigCheckCategoryEnum getCategory() {
        return ConfigCheckCategoryEnum.MCH_NOTIFY;
    }

    @Override
    public ConfigCheckItem check(String mchNo) {
        // 功能建设中停用巡检, 恒不产生告警项
        return null;
    }
}
