package cn.daxpay.open.channel.lakala.client.enums;

/// # 拉卡拉支付内容类型(主应用侧, 与子应用镜像)
public enum LakalaPayBodyType {
    /// 跳转链接
    LINK,
    /// 二维码内容
    QR_CODE,
    /// JSAPI/小程序调起参数 JSON
    JSAPI,
    /// 通用标识码
    IDENTIFIER;
}
