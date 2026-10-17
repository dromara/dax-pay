package cn.daxpay.open.channel.youzan.client.enums;

/// # 有赞支付方式
///
/// 对应有赞统一下单接口的 `pay_tool` 支付工具码, 由支付形态与底层渠道组合决定
/// (对齐参考实现: ALIPAY_JS / WX_JS / WX_APPLET / WX_H5 四种)。
public enum YouzanPayMethod {
    /// 支付宝 JS 支付(ext 传 buyer_id)
    ALIPAY_JS,
    /// 微信 JS 支付(ext 传 open_id + app_id)
    WX_JS,
    /// 微信小程序支付(ext 传 open_id + app_id)
    WX_APPLET,
    /// 微信 H5 支付(ext 传 type=Wap, 返回 awake_pay_url 唤醒链接)
    WX_H5;
}
