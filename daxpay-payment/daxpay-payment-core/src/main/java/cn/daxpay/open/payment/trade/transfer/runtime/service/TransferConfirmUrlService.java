package cn.daxpay.open.payment.trade.transfer.runtime.service;

import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 微信转账确认收款链接生成服务
///
/// 生成确认收款链接供商户发给收款人在微信内打开,
/// 路径约定: `{paymentGatewayBaseUrl}/transfer-confirm/{transferNo}`(H5 确认页路由)。
///
/// 链接不落库(由 transferNo + 平台配置实时拼接), 供发起返回与订单详情查询复用。
@Service
@RequiredArgsConstructor
public class TransferConfirmUrlService {

    private final PlatformUrlConfigService platformUrlConfigService;

    /// 生成微信转账确认收款链接
    ///
    /// @param transferNo 平台转账单号
    /// @return 确认收款链接, 平台未配置网关地址时返回 null
    public String buildConfirmUrl(String transferNo) {
        String base = platformUrlConfigService.getUrlConfig().getPaymentGatewayBaseUrl();
        if (StrUtil.isBlank(base)) {
            return null;
        }
        return StrUtil.format("{}/transfer-confirm/{}", base, transferNo);
    }
}
