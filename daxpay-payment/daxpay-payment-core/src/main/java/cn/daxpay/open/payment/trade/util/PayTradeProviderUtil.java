package cn.daxpay.open.payment.trade.util;

import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum;
import cn.hutool.core.util.StrUtil;

import java.util.Arrays;
import java.util.Objects;

/// # 资金凭证支付渠道解析
///
/// 报表/资金列表依赖 [cn.daxpay.open.payment.trade.order.entity.PayTrade#getProvider]；
/// 渠道在下单时即可由支付方式确定，不依赖三方 sync 透传。
/// [PayMethodEnum#OTHER] 无固定渠道归属时返回 null。
public final class PayTradeProviderUtil {

    private PayTradeProviderUtil() {
    }

    /// 由支付方式编码解析支付渠道编码；method 空/未知/OTHER 返回 null（不抛异常）
    public static String resolveProviderByMethod(String methodCode) {
        if (StrUtil.isBlank(methodCode)) {
            return null;
        }
        return Arrays.stream(PayMethodEnum.values())
                .filter(o -> Objects.equals(o.getCode(), methodCode))
                .findFirst()
                .map(PayMethodEnum::getProvider)
                .map(PayProviderEnum::getCode)
                .orElse(null);
    }

    /// 优先保留已有 provider；否则用容器已存值；再否则由 method 派生
    public static String coalesceProvider(String tradeProvider, String containerProvider, String methodCode) {
        if (StrUtil.isNotBlank(tradeProvider)) {
            return tradeProvider;
        }
        if (StrUtil.isNotBlank(containerProvider)) {
            return containerProvider;
        }
        return resolveProviderByMethod(methodCode);
    }
}
