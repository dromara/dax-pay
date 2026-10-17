package cn.daxpay.open.channel.jeepay.client.enums;

/// # Jeepay支付内容类型(主应用侧)
///
/// 标识 `JeepayPayResp.payBody`(即 Jeepay 响应的 payData 字段)的内容形态,
/// 与子应用侧 `cn.daxpay.open.channel.jeepay.enums.JeepayPayBodyType` 镜像, 经 JSON 传输。
public enum JeepayPayBodyType {
    /// 跳转链接(payUrl: 收银台跳转 / codeImgUrl: 二维码图片地址)
    LINK,
    /// 二维码内容(codeUrl)
    QR_CODE,
    /// JSAPI/小程序/APP 调起参数 JSON(wxapp/aliapp/ysfapp)
    JSAPI,
    /// 表单数据(form: 自动提交的表单)
    FROM;
}
