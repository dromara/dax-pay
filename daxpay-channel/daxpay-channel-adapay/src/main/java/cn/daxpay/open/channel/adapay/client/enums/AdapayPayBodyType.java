package cn.daxpay.open.channel.adapay.client.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/// # Adapay 支付内容类型(主应用侧)
///
/// 与子应用镜像, 主应用 service 将其映射为平台 [cn.daxpay.open.platform.core.enums.unipay.PayBodyTypeEnum]。
@Getter
@AllArgsConstructor
public enum AdapayPayBodyType {

    /// 二维码链接
    QR_CODE("QR_CODE"),
    /// 跳转链接
    LINK("LINK"),
    /// JSAPI 调起参数
    JSAPI("JSAPI"),
    /// 支付宝交易号标识
    IDENTIFIER("IDENTIFIER");

    private final String code;
}
