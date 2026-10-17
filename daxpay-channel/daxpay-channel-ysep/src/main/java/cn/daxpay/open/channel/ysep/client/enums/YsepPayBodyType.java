package cn.daxpay.open.channel.ysep.client.enums;

/// # 银盛支付内容类型(主应用侧, 与子应用镜像)
public enum YsepPayBodyType {
    /// 二维码内容
    QR_CODE,
    /// JSAPI/小程序调起参数 JSON
    JSAPI,
    /// 通用标识码
    IDENTIFIER,
    /// 跳转链接
    LINK,
    /// 自动提交表单 HTML(ysep_pay WAP 跳转)
    FORM;
}
