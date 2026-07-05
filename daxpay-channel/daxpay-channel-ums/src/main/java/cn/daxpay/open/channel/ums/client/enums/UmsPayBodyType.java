package cn.daxpay.open.channel.ums.client.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/// # 银联商务支付内容类型
///
/// 与子应用 `cn.daxpay.open.channel.ums.enums.UmsPayBodyType` 镜像。
@Getter
@AllArgsConstructor
public enum UmsPayBodyType {

    /// 二维码链接(扫码支付)
    QR_CODE("QR_CODE"),
    /// 跳转链接(H5 支付)
    LINK("LINK");

    private final String code;
}
