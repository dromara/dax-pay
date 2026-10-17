package cn.daxpay.open.channel.ysep.client.enums;

import lombok.Getter;

import java.util.Arrays;

/// # 银盛支付方式(主应用侧, 与子应用镜像)
///
/// `code` 与平台 [cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum] 一致, 两产品族取值并集。
/// 各产品族实际支持项(超出部分在子应用对应 Service 内报不支持):
/// - ysep_pay(开放平台): wechat_qr/wechat_jsapi/wechat_mini/alipay_qr/alipay_jsapi/alipay_h5/union_qr/union_jsapi
/// - yseqt_pay(e企通): wechat_qr/wechat_jsapi/wechat_mini/wechat_h5/alipay_qr/alipay_jsapi/union_qr/union_jsapi
@Getter
public enum YsepPayMethod {

    /// 微信Native扫码
    WECHAT_QR("wechat_qr"),
    /// 微信公众号JSAPI
    WECHAT_JSAPI("wechat_jsapi"),
    /// 微信小程序
    WECHAT_MINI("wechat_mini"),
    /// 微信H5(仅 yseqt_pay 聚合收银台)
    WECHAT_H5("wechat_h5"),
    /// 支付宝扫码
    ALIPAY_QR("alipay_qr"),
    /// 支付宝JSAPI(生活号)
    ALIPAY_JSAPI("alipay_jsapi"),
    /// 支付宝H5(仅 ysep_pay WAP 跳转)
    ALIPAY_H5("alipay_h5"),
    /// 银联扫码
    UNION_QR("union_qr"),
    /// 银联JS
    UNION_JSAPI("union_jsapi"),
    ;

    /// 编码(与平台 PayMethodEnum 一致)
    private final String code;

    YsepPayMethod(String code) {
        this.code = code;
    }

    /// 根据编码获取枚举, 不存在返回 null(供上层判空后抛通道级不支持异常, 勿用异常做控制流)
    public static YsepPayMethod findByCodeOrNull(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }
}
