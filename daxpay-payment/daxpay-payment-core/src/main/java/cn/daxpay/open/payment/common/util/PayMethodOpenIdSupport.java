package cn.daxpay.open.payment.common.util;

import cn.daxpay.open.payment.merchant.enums.ClientEnvEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.hutool.core.util.StrUtil;

import java.util.Set;

/// # 支付方式是否需要 / 能获取买家 openId / userId
///
/// 供码牌 H5 OAuth 门控、聚合 meta、下单前校验共用。
/// 与交互形态绑定(JSAPI/小程序需用户标识；H5/主扫等一般不需要)，**不**挂产品/Gateway 策略。
///
/// 换票时序：授权回跳页立即 code→openId，支付只传 openId，禁止支付时再换 code。
///
/// ## 两个维度的区分
/// - [needsOpenId]: 业务上是否**必须** openId（JSAPI/MINI 永远 true）
/// - [canAcquireOpenId]: 技术上是否**能**拿到 openId（视 clientEnv 而定）
public final class PayMethodOpenIdSupport {

    /// 需要买家标识的支付方式(与聚合历史白名单一致)
    private static final Set<String> METHODS_NEED_OPEN_ID = Set.of(
            PayMethodEnum.WECHAT_JSAPI.getCode(),
            PayMethodEnum.WECHAT_MINI.getCode(),
            PayMethodEnum.ALIPAY_JSAPI.getCode(),
            PayMethodEnum.UNION_JSAPI.getCode(),
            PayMethodEnum.DOUYIN_JSAPI.getCode()
    );

    /// 完全无 OAuth 时机的支付方式（付款码被扫、APP 内 SDK、PC 网页、聚合码、卡组等）
    private static final Set<String> METHODS_NO_OAUTH_AT_ALL = Set.of(
            PayMethodEnum.WECHAT_BARCODE.getCode(),
            PayMethodEnum.ALIPAY_BARCODE.getCode(),
            PayMethodEnum.UNION_BARCODE.getCode(),
            PayMethodEnum.WECHAT_APP.getCode(),
            PayMethodEnum.ALIPAY_APP.getCode(),
            PayMethodEnum.DOUYIN_APP.getCode(),
            PayMethodEnum.ALIPAY_PC.getCode(),
            PayMethodEnum.WECHAT_CASHIER.getCode(),
            PayMethodEnum.AGGREGATE_PAY_QRCODE.getCode(),
            PayMethodEnum.VISA_CARD_GATEWAY.getCode(),
            PayMethodEnum.VISA_CARD_PRESENT.getCode(),
            PayMethodEnum.MASTERCARD_CARD_GATEWAY.getCode(),
            PayMethodEnum.MASTERCARD_CARD_PRESENT.getCode(),
            PayMethodEnum.OTHER.getCode()
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

    /// 该支付方式 + 客户端环境组合下, 是否可走 OAuth 静默获取 openId
    ///
    /// 用于网关层判断: openId 黑名单存在时, 是否对当前方式触发强制 OAuth 拦截。
    /// 与 [needsOpenId] 互补——前者回答"业务是否需要", 本方法回答"技术是否能拿到"。
    ///
    /// 矩阵:
    /// - JSAPI/MINI/QR/H5 在微信/支付宝/抖音内: ✅ 可静默 OAuth (snsapi_base / auth_base / silent_auth)
    /// - 付款码/APP/PC/聚合码/卡组: ❌ 始终无 OAuth 时机
    /// - union_pay: ❌ 一期无平台 OAuth
    /// - browser(外部浏览器): ❌ 无法 OAuth 拿微信/支付宝 openId
    ///
    /// @param methodCode 支付方式编码
    /// @param clientEnv  客户端环境, null 视为不可
    public static boolean canAcquireOpenId(String methodCode, ClientEnvEnum clientEnv) {
        if (clientEnv == null) {
            return false;
        }
        // 付款码 / APP / PC 等完全无 OAuth 时机
        if (cannotAcquireOpenIdAtAll(methodCode)) {
            return false;
        }
        // 客户端环境维度: 仅微信/支付宝/抖音内浏览器可静默授权
        return switch (clientEnv) {
            case WECHAT, ALIPAY, DOUYIN -> true;
            // 一期无平台 OAuth, 暂不可
            case UNION_PAY -> false;
            // 外部浏览器无法 OAuth 拿微信/支付宝 openId
            case BROWSER -> false;
        };
    }

    /// 这些方式完全无 OAuth 时机（付款码被扫、APP 内 SDK、PC 网页、聚合码、卡组）
    private static boolean cannotAcquireOpenIdAtAll(String methodCode) {
        if (StrUtil.isBlank(methodCode)) {
            // 未知方式保守起见视为不可, 避免无效 OAuth 跳转
            return true;
        }
        if (METHODS_NO_OAUTH_AT_ALL.contains(methodCode)) {
            return true;
        }
        try {
            PayMethodEnum method = PayMethodEnum.findByCode(methodCode);
            return switch (method) {
                case WECHAT_BARCODE, ALIPAY_BARCODE, UNION_BARCODE,
                     WECHAT_APP, ALIPAY_APP, DOUYIN_APP,
                     ALIPAY_PC, VISA_CARD_GATEWAY, VISA_CARD_PRESENT,
                     MASTERCARD_CARD_GATEWAY, MASTERCARD_CARD_PRESENT,
                     WECHAT_CASHIER, AGGREGATE_PAY_QRCODE, OTHER -> true;
                default -> false;
            };
        } catch (Exception e) {
            // 未知扩展 method: 保守视为不可
            return true;
        }
    }
}
