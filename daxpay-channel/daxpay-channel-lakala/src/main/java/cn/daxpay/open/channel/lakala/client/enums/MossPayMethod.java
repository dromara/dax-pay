package cn.daxpay.open.channel.lakala.client.enums;

/// # 拉卡拉 MOSS 支付方式(主应用侧, 与子应用镜像)
///
/// MOSS 统一下单通过 pay_scene 区分两种形态:
/// - CASHIER: 收银台场景(pay_scene=0), 返回 counter_url 聚合收银台链接
/// - PREORDER: 直连下单(pay_scene=1), 配合 accountType + transType 产出二维码/JSAPI 调起参数
public enum MossPayMethod {
    /// 收银台链接(pay_scene=0)
    CASHIER,
    /// 直连下单(pay_scene=1)
    PREORDER;
}
