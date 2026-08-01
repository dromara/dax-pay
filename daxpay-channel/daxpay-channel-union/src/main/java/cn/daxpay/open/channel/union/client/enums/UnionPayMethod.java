package cn.daxpay.open.channel.union.client.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/// # 云闪付通道支付方式
///
/// 与子应用 `cn.daxpay.open.channel.union.enums.UnionPayMethod` 镜像。
@Getter
@AllArgsConstructor
public enum UnionPayMethod {

    /// 主扫支付(申请二维码)
    QRCODE("QRCODE"),
    /// 被扫支付(付款码消费)
    BARCODE("BARCODE"),
    /// H5/WAP 网关支付
    H5("H5");

    private final String code;
}
