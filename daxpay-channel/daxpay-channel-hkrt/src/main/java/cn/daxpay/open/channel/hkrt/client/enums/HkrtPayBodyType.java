package cn.daxpay.open.channel.hkrt.client.enums;

/// # 海科融通支付内容类型(主应用侧, 与子应用镜像)
///
/// 海科融通无 LINK(跳转链接) 类型, 仅 QR_CODE / JSAPI / IDENTIFIER 三种。
public enum HkrtPayBodyType {
    /// 二维码内容
    QR_CODE,
    /// JSAPI/小程序调起参数 JSON
    JSAPI,
    /// 通用标识码
    IDENTIFIER;
}
