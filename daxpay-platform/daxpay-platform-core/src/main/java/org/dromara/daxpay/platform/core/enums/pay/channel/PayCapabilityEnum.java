package org.dromara.daxpay.platform.core.enums.pay.channel;

import org.dromara.daxpay.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

/// # 支付能力
///
/// 字典: pay_md_capability；`code` 为独立发版契约，与 `PayMethodEnum` / `PayProviderEnum` **无类型级绑定**。
/// 与支付产品的关联见 `pay_md_product_capability`；与渠道支付方式目录、通道路由的整合另行维护。
@Getter
@RequiredArgsConstructor
public enum PayCapabilityEnum implements I18nSupport {

    /* 聚合支付 */
    /// 聚合扫码
    AGGREGATE_PAY_QRCODE("aggregate_pay_qrcode"),
    /// 聚合付款码
    AGGREGATE_PAY_BARCODE("aggregate_pay_barcode"),

    /* 微信 */
    /// 微信小程序收银台
    WECHAT_CASHIER("wechat_cashier"),
    /// 微信 JSAPI
    WECHAT_JSAPI("wechat_jsapi"),
    /// 微信 APP
    WECHAT_APP("wechat_app"),
    /// 微信 H5
    WECHAT_H5("wechat_h5"),
    /// 微信扫码
    WECHAT_QR("wechat_qr"),
    /// 微信小程序
    WECHAT_MINI("wechat_mini"),
    /// 微信付款码
    WECHAT_BARCODE("wechat_barcode"),

    /* 支付宝 */
    /// 支付宝付款码
    ALIPAY_BARCODE("alipay_barcode"),
    /// 支付宝订单码
    ALIPAY_ORDER_QR("alipay_order_qr"),
    /// 支付宝 APP
    ALIPAY_APP("alipay_app"),
    /// 支付宝 H5
    ALIPAY_H5("alipay_h5"),
    /// 支付宝电脑
    ALIPAY_PC("alipay_pc"),
    /// 支付宝 JSAPI
    ALIPAY_JSAPI("alipay_jsapi"),
    /// 支付宝小程序
    ALIPAY_MINI("alipay_mini"),

    /* 银联 */
    /// 银联扫码
    UNION_PAY_QR("union_pay_qr"),
    /// 银联付款码
    UNION_PAY_BARCODE("union_pay_barcode"),
    /// 银联 H5
    UNION_PAY_H5("union_pay_h5"),
    /// 银联 JSAPI
    UNION_PAY_JSAPI("union_pay_jsapi"),

    /* 抖音 */
    /// 抖音扫码
    DOUYIN_QR("douyin_qr"),
    /// 抖音 JSAPI
    DOUYIN_JSAPI("douyin_jsapi"),
    /// 抖音 H5
    DOUYIN_H5("douyin_h5"),
    /// 抖音 APP
    DOUYIN_APP("douyin_app"),

    /* 卡组 */
    /// Visa 网关
    VISA_CARD_GATEWAY("visa_card_gateway"),
    /// Visa 线下
    VISA_CARD_PRESENT("visa_card_present"),
    /// 万事达网关
    MASTERCARD_CARD_GATEWAY("mastercard_card_gateway"),
    /// 万事达线下
    MASTERCARD_CARD_PRESENT("mastercard_card_present"),
    ;

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.pay_capability";
    }

    /// 根据编码获取枚举；未知编码返回 null
    public static PayCapabilityEnum findByCode(String code) {
        if (code == null) {
            return null;
        }
        return Arrays.stream(values())
                .filter(e -> Objects.equals(e.code, code))
                .findFirst()
                .orElse(null);
    }
}
