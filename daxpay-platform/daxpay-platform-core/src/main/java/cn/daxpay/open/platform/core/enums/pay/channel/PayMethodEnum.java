package cn.daxpay.open.platform.core.enums.pay.channel;

import cn.daxpay.open.platform.core.exception.business.UnsupportedAbilityException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

/// # 支付方式
///
/// 字典: pay_method；`code` 全局唯一，且仅绑定一个支付渠道。
@Getter
@RequiredArgsConstructor
public enum PayMethodEnum implements I18nSupport {

    /* 聚合支付 */
    /// 聚合扫码支付
    AGGREGATE_PAY_QRCODE("aggregate_pay_qrcode"),
    /// 聚合付款码支付
    AGGREGATE_PAY_BARCODE("aggregate_pay_barcode"),

    /* 微信 */
    /// 微信小程序收银台
    WECHAT_CASHIER("wechat_cashier"),
    /// 微信扫码
    WECHAT_QR("wechat_qr"),
    /// 微信jsapi
    WECHAT_JSAPI("wechat_jsapi"),
    /// 微信小程序
    WECHAT_MINI("wechat_mini"),
    /// 微信H5
    WECHAT_H5("wechat_h5"),
    /// 微信应用支付
    WECHAT_APP("wechat_app"),
    /// 微信付款码
    WECHAT_BARCODE("wechat_barcode"),

    /* 支付宝 */
    /// 支付宝扫码
    ALIPAY_QR("alipay_qr"),
    /// 支付宝订单码
    ALIPAY_ORDER_QR("alipay_order_qr"),
    /// 支付宝jsapi
    ALIPAY_JSAPI("alipay_jsapi"),
    /// 支付宝小程序
    ALIPAY_MINI("alipay_mini"),
    /// 支付宝电脑支付
    ALIPAY_PC("alipay_pc"),
    /// 支付宝H5
    ALIPAY_H5("alipay_h5"),
    /// 支付宝应用支付
    ALIPAY_APP("alipay_app"),
    /// 支付宝付款码
    ALIPAY_BARCODE("alipay_barcode"),

    /* 银联 */
    /// 银联扫码
    UNION_QR("union_qr"),
    /// 银联jsapi
    UNION_JSAPI("union_jsapi"),
    /// 银联H5
    UNION_H5("union_h5"),
    /// 银联付款码
    UNION_PAY_BARCODE("union_pay_barcode"),

    /* 抖音 */
    /// 抖音扫码支付
    DOUYIN_QR("douyin_qr"),
    /// 抖音JSAPI支付
    DOUYIN_JSAPI("douyin_jsapi"),
    /// 抖音H5支付
    DOUYIN_H5("douyin_h5"),
    /// 抖音APP支付
    DOUYIN_APP("douyin_app"),

    /* 卡组 */
    /// Visa 网关支付
    VISA_CARD_GATEWAY("visa_card_gateway"),
    /// Visa 刷卡支付
    VISA_CARD_PRESENT("visa_card_present"),
    /// 万事达网关支付
    MASTERCARD_CARD_GATEWAY("mastercard_card_gateway"),
    /// 万事达刷卡支付
    MASTERCARD_CARD_PRESENT("mastercard_card_present"),

    /// 其他支付方式
    OTHER("other"),
    ;

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.pay_method";
    }

    /// 根据编码获取枚举
    public static PayMethodEnum findByCode(String code){
        return Arrays.stream(values())
                .filter(o -> Objects.equals(o.getCode(), code))
                .findFirst()
                // 不存在的支付方式
                .orElseThrow(() -> new UnsupportedAbilityException("pay.error.methodNotExist"));
    }
}
