package cn.daxpay.open.payment.common.util;

import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.hutool.core.util.StrUtil;

import java.util.Set;

/// # 支付方式是否需要买家 openId / userId
///
/// 供码牌 H5 OAuth 门控、聚合 meta、下单前校验共用。
/// 与交互形态绑定(JSAPI/小程序需用户标识；H5/主扫等一般不需要)，**不**挂产品/Gateway 策略。
///
/// 换票时序：授权回跳页立即 code→openId，支付只传 openId，禁止支付时再换 code。
public final class PayMethodOpenIdSupport {

    /// 需要买家标识的支付方式(与聚合历史白名单一致)
    private static final Set<String> METHODS_NEED_OPEN_ID = Set.of(
            PayMethodEnum.WECHAT_JSAPI.getCode(),
            PayMethodEnum.WECHAT_MINI.getCode(),
            PayMethodEnum.ALIPAY_JSAPI.getCode(),
            PayMethodEnum.UNION_JSAPI.getCode(),
            PayMethodEnum.DOUYIN_JSAPI.getCode()
    );

    private PayMethodOpenIdSupport() {
    }

    /// 该支付方式下单前是否需要 openId/userId
    ///
    /// @param methodCode 支付方式编码，空则 false
    public static boolean needsOpenId(String methodCode) {
        if (StrUtil.isBlank(methodCode)) {
            return false;
        }
        if (METHODS_NEED_OPEN_ID.contains(methodCode)) {
            return true;
        }
        try {
            PayMethodEnum method = PayMethodEnum.findByCode(methodCode);
            return switch (method) {
                case WECHAT_JSAPI, WECHAT_MINI, ALIPAY_JSAPI, UNION_JSAPI, DOUYIN_JSAPI -> true;
                default -> false;
            };
        }
        catch (Exception e) {
            // 未知扩展 method: 含 jsapi/mini 视作需要
            String lower = methodCode.toLowerCase();
            return lower.contains("jsapi") || lower.contains("mini");
        }
    }
}
