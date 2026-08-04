package cn.daxpay.open.payment.route.service.support;

import cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum;
import cn.hutool.core.util.StrUtil;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/// # 通道路由配置支付渠道白名单
///
/// 仅通道路由配置页使用，非平台全量 `PayProviderEnum`。
@UtilityClass
public class PayRouteConfigProviders {

    public static final List<String> CODES = List.of(
            // 聚合扫码(通道原生通扫码目录)
            PayProviderEnum.AGGREGATE_PAY.getCode(),
            PayProviderEnum.WECHAT.getCode(),
            PayProviderEnum.ALIPAY.getCode(),
            PayProviderEnum.UNION_PAY.getCode(),
            PayProviderEnum.DOUYIN.getCode(),
            // 国际信用卡(Stripe 通道, 路由配置页可配置 Visa / MasterCard)
            PayProviderEnum.VISA.getCode(),
            PayProviderEnum.MASTERCARD.getCode()
    );

    /// 是否属于通道路由配置白名单支付渠道
    public boolean contains(String providerCode) {
        return StrUtil.isNotBlank(providerCode) && CODES.contains(providerCode);
    }

    /// 按枚举声明顺序返回白名单内的支付渠道
    public List<PayProviderEnum> enumsInWhitelistOrder() {
        return Arrays.stream(PayProviderEnum.values())
                .filter(provider -> CODES.contains(provider.getCode()))
                .toList();
    }
}
