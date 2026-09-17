package cn.daxpay.open.channel.sheng.client.enums;

import lombok.Getter;

import java.util.Arrays;

/// # 盛付通支付方式(主应用侧, 与子应用镜像)
///
/// `code` 与平台 [cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum] 一致,
/// 共 15 项(种子 16 项能力减去 union_h5: 一期不接入银联 H5, SQL 种子保留不动)。
///
/// 被扫三值(wechat_barcode / alipay_barcode / union_barcode)由子应用分流 authPay 接口,
/// 其余走 unifiedorderOffline; 主应用只透传 payMethod, 不做接口选型。
@Getter
public enum ShengPayMethod {

    /// 微信Native扫码
    WECHAT_QR("wechat_qr"),
    /// 微信公众号JSAPI
    WECHAT_JSAPI("wechat_jsapi"),
    /// 微信小程序
    WECHAT_MINI("wechat_mini"),
    /// 微信APP
    WECHAT_APP("wechat_app"),
    /// 微信H5
    WECHAT_H5("wechat_h5"),
    /// 微信付款码被扫
    WECHAT_BARCODE("wechat_barcode"),

    /// 支付宝扫码
    ALIPAY_QR("alipay_qr"),
    /// 支付宝JSAPI(含小程序)
    ALIPAY_JSAPI("alipay_jsapi"),
    /// 支付宝APP
    ALIPAY_APP("alipay_app"),
    /// 支付宝H5
    ALIPAY_H5("alipay_h5"),
    /// 支付宝PC
    ALIPAY_PC("alipay_pc"),
    /// 支付宝付款码被扫
    ALIPAY_BARCODE("alipay_barcode"),

    /// 银联扫码
    UNION_QR("union_qr"),
    /// 银联JS
    UNION_JSAPI("union_jsapi"),
    /// 银联付款码被扫
    UNION_BARCODE("union_barcode"),
    ;

    /// 编码(与平台 PayMethodEnum 一致)
    private final String code;

    ShengPayMethod(String code) {
        this.code = code;
    }

    /// 根据编码获取枚举, 不存在返回 null(供上层判空后抛通道级不支持异常, 勿用异常做控制流)
    public static ShengPayMethod findByCodeOrNull(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }
}
