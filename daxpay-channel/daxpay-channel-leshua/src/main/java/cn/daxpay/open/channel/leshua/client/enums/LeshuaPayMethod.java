package cn.daxpay.open.channel.leshua.client.enums;

/// # 乐刷支付方式(主应用侧镜像)
///
/// 与子应用 dax-pay-channel-two 的 `LeshuaPayMethod` 镜像, 跨 HTTP 传输时按枚举名(name)序列化对齐。
public enum LeshuaPayMethod {
    /// 付款码支付(被扫, 走 upload_authcode)
    UPLOAD_AUTHCODE,
    /// 预下单(扫码/JSAPI/H5/小程序, 走 get_tdcode)
    GET_TDCODE;
}
