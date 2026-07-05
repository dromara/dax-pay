package cn.daxpay.open.channel.lakala.client.enums;

/// # 拉卡拉支付方式(主应用侧, 与子应用镜像)
public enum LakalaPayMethod {
    /// 条码支付(付款码被扫)
    MICROPAY,
    /// 预下单(扫码/JSAPI/APP/小程序)
    PREORDER;
}
