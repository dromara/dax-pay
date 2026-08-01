package cn.daxpay.open.channel.union.client.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/// # 云闪付支付内容类型
///
/// 与子应用 `cn.daxpay.open.channel.union.enums.UnionPayBodyType` 镜像。
@Getter
@AllArgsConstructor
public enum UnionPayBodyType {

    /// 二维码内容(主扫支付)
    QR_CODE("QR_CODE"),

    /// 跳转链接(H5 支付)
    LINK("LINK");

    private final String code;
}
