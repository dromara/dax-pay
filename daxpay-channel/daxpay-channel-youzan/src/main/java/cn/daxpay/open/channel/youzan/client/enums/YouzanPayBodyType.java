package cn.daxpay.open.channel.youzan.client.enums;

/// # 有赞支付内容类型
///
/// 标识 `YouzanPayResp.payBody` 的内容形态, 主应用据此决定如何渲染支付页。
public enum YouzanPayBodyType {
    /// JSAPI/小程序调起参数 JSON(deeplink)
    JSAPI,
    /// 支付宝 JS 唤起标识码(deeplink)
    IDENTIFIER,
    /// 跳转链接(H5 的 awake_pay_url 唤醒链接)
    LINK;
}
