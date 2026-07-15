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
/// `provider` 显式声明每个支付方式所属的支付渠道，供通道路由零开销解析（替代旧版从支付产品反推）。
@Getter
@RequiredArgsConstructor
public enum PayMethodEnum implements I18nSupport {

    /* 聚合支付 */
    /// 聚合扫码（通道原生通扫码；付款码由平台按 authCode 前缀识别为分钱包 method）
    AGGREGATE_PAY_QRCODE("aggregate_pay_qrcode", PayProviderEnum.AGGREGATE_PAY),

    /* 微信 */
    /// 微信小程序收银台
    WECHAT_CASHIER("wechat_cashier", PayProviderEnum.WECHAT),
    /// 微信Native
    WECHAT_QR("wechat_qr", PayProviderEnum.WECHAT),
    /// 微信JSAPI
    WECHAT_JSAPI("wechat_jsapi", PayProviderEnum.WECHAT),
    /// 微信小程序
    WECHAT_MINI("wechat_mini", PayProviderEnum.WECHAT),
    /// 微信H5
    WECHAT_H5("wechat_h5", PayProviderEnum.WECHAT),
    /// 微信APP
    WECHAT_APP("wechat_app", PayProviderEnum.WECHAT),
    /// 微信付款码
    WECHAT_BARCODE("wechat_barcode", PayProviderEnum.WECHAT),

    /* 支付宝 */
    /// 支付宝扫码
    ALIPAY_QR("alipay_qr", PayProviderEnum.ALIPAY),
    /// 支付宝 JSAPI（含小程序；官方产品码 JSAPI_PAY）
    ALIPAY_JSAPI("alipay_jsapi", PayProviderEnum.ALIPAY),
    /// 支付宝PC
    ALIPAY_PC("alipay_pc", PayProviderEnum.ALIPAY),
    /// 支付宝H5
    ALIPAY_H5("alipay_h5", PayProviderEnum.ALIPAY),
    /// 支付宝APP
    ALIPAY_APP("alipay_app", PayProviderEnum.ALIPAY),
    /// 支付宝付款码
    ALIPAY_BARCODE("alipay_barcode", PayProviderEnum.ALIPAY),

    /* 银联 */
    /// 银联扫码
    UNION_QR("union_qr", PayProviderEnum.UNION_PAY),
    /// 银联JSAPI
    UNION_JSAPI("union_jsapi", PayProviderEnum.UNION_PAY),
    /// 银联H5
    UNION_H5("union_h5", PayProviderEnum.UNION_PAY),
    /// 银联付款码
    UNION_BARCODE("union_barcode", PayProviderEnum.UNION_PAY),

    /* 抖音 */
    /// 抖音扫码
    DOUYIN_QR("douyin_qr", PayProviderEnum.DOUYIN),
    /// 抖音JSAPI
    DOUYIN_JSAPI("douyin_jsapi", PayProviderEnum.DOUYIN),
    /// 抖音H5
    DOUYIN_H5("douyin_h5", PayProviderEnum.DOUYIN),
    /// 抖音APP
    DOUYIN_APP("douyin_app", PayProviderEnum.DOUYIN),

    /* 卡组 */
    /// Visa网关
    VISA_CARD_GATEWAY("visa_card_gateway", PayProviderEnum.VISA),
    /// Visa刷卡
    VISA_CARD_PRESENT("visa_card_present", PayProviderEnum.VISA),
    /// 万事达网关
    MASTERCARD_CARD_GATEWAY("mastercard_card_gateway", PayProviderEnum.MASTERCARD),
    /// 万事达刷卡
    MASTERCARD_CARD_PRESENT("mastercard_card_present", PayProviderEnum.MASTERCARD),

    /// 其他(无固定渠道归属)
    OTHER("other", null),
    ;

    /// 编码
    private final String code;

    /// 所属支付渠道(OTHER 等无固定归属时为 null)
    private final PayProviderEnum provider;

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
