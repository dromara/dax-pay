package org.dromara.daxpay.platform.core.enums.pay.channel;

import org.dromara.daxpay.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 支付产品枚举
///
/// 字典值: pay_product
@Getter
@RequiredArgsConstructor
public enum ProductEnum implements I18nSupport {

    // ===== 支付宝 =====
    /// 支付宝(服务商)
    ALIPAY_ISV("alipay_isv"),
    /// 支付宝(直连)
    ALIPAY("alipay"),

    // ===== 微信支付 =====
    /// 微信支付(服务商)
    WECHAT_ISV("wechat_isv"),
    /// 微信支付(直连)
    WECHAT_PAY("wechat_pay"),

    // ===== 抖音支付 =====
    /// 抖音支付(直连)
    DOUYIN_PAY("douyin_pay"),

    // ===== 银联商务 =====
    /// 银联商务(C扫B)
    UMS_QRCODE("ums_qrcode"),
    /// 银联商务(公众号)
    UMS_JSAPI("ums_jsapi"),
    /// 银联商务(APP)
    UMS_APP("ums_app"),
    /// 银联商务(小程序)
    UMS_MINI("ums_mini"),
    /// 银联商务(H5)
    UMS_H5("ums_h5"),
    /// 银联商务(B扫C)
    UMS_BARCODE("ums_barcode"),

    // ===== 拉卡拉 =====
    /// 拉卡拉支付
    LAKALA_PAY("lakala_pay"),
    ;

    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.product";
    }

    /// 获取所属通道编码
    public String getChannel() {
        return switch (this) {
            case ALIPAY_ISV, ALIPAY -> ChannelEnum.ALIPAY.getCode();
            case WECHAT_ISV, WECHAT_PAY -> ChannelEnum.WECHAT.getCode();
            // 银联商务系列
            case UMS_QRCODE, UMS_JSAPI, UMS_APP, UMS_MINI, UMS_H5, UMS_BARCODE -> ChannelEnum.UMS_PAY.getCode();
            // 拉卡拉
            case LAKALA_PAY -> ChannelEnum.LAKALA_PAY.getCode();
            // 抖音支付
            case DOUYIN_PAY -> ChannelEnum.DOUYIN_PAY.getCode();
        };
    }

    /// 根据编码获取枚举
    public static ProductEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }
}
