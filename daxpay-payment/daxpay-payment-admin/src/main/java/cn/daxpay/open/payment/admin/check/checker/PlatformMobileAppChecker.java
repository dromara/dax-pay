package cn.daxpay.open.payment.admin.check.checker;

import cn.daxpay.open.payment.common.check.checker.AdminConfigChecker;
import cn.daxpay.open.payment.common.check.enums.ConfigCheckCategoryEnum;
import cn.daxpay.open.payment.common.check.model.ConfigCheckItem;
import cn.daxpay.open.payment.device.qrcode.dao.DeviceQrCodeManager;
import cn.daxpay.open.platform.system.dao.mobile.MobileAppManager;
import cn.daxpay.open.platform.system.entity.mobile.MobileApp;
import cn.daxpay.open.platform.system.enums.MobileAppTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/// # 收银台小程序应用检查器
///
/// 检测收银台小程序(appType=cashier)应用是否已启用。未启用时码牌扫码与小程序收银不可用。
/// 主口径: 存在 cashier 记录但无一条启用 => 告警(count=记录数);
/// 完全无 cashier 记录视为未使用该能力不告警, 但已存在码牌数据(部署在用收款码)时仍提醒。
@Component
@Order(10)
@RequiredArgsConstructor
public class PlatformMobileAppChecker implements AdminConfigChecker {

    private final MobileAppManager mobileAppManager;
    private final DeviceQrCodeManager deviceQrCodeManager;

    @Override
    public ConfigCheckCategoryEnum getCategory() {
        return ConfigCheckCategoryEnum.PLATFORM_MOBILE_APP;
    }

    @Override
    public ConfigCheckItem check() {
        String cashierType = MobileAppTypeEnum.CASHIER.getCode();
        // 收银台应用记录总数
        long totalCount = mobileAppManager.lambdaQuery()
                .eq(MobileApp::getAppType, cashierType)
                .count();
        // 已有启用的收银台应用 => 就绪, 不告警
        long enabledCount = mobileAppManager.lambdaQuery()
                .eq(MobileApp::getAppType, cashierType)
                .eq(MobileApp::getEnabled, true)
                .count();
        if (enabledCount > 0) {
            return null;
        }
        // 完全无收银台记录且无码牌数据 => 未使用该能力, 不告警
        if (totalCount == 0 && deviceQrCodeManager.lambdaQuery().count() == 0) {
            return null;
        }
        ConfigCheckItem item = ConfigCheckItem.of(
                ConfigCheckCategoryEnum.PLATFORM_MOBILE_APP,
                ConfigCheckCategoryEnum.PLATFORM_MOBILE_APP.getCode(),
                "configCheck.platformMobileApp.title",
                "configCheck.platformMobileApp.description",
                "/payment/config/mobile-app"
        );
        if (totalCount > 0) {
            item.setCount((int) totalCount);
        }
        return item;
    }
}
