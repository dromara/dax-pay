package cn.daxpay.open.channel.mock.client.enums;

/// # 支付内容类型
///
/// 与通道子应用 one 侧 `cn.daxpay.open.channel.mock.enums.MockPayBodyType` 镜像,
/// 跨 HTTP 传输时按枚举名(name)序列化对齐。
///
/// payBody 形态按支付方式分化: QR/SCAN/H5/PC/APP→URL(收款确认页地址);
/// MP/JSAPI/MINI_APP→JSON(拟真 prepay 结构); BARCODE 即时判定无支付内容。
///
/// **契约约束**: 本枚举值集须与子应用同名枚举完全一致(少值会在子应用返回该值时反序列化失败), 改一侧必须同步改另一侧。
public enum MockPayBodyType {

    /// 跳转链接(收款确认页地址, 前端可直接打开或渲染成二维码)
    URL,
    /// 拉起参数串(APP 方式历史形态, 如 mock://pay?tradeNo=xxx&prepayId=xxx)
    ///
    /// 当前 mock 子应用 APP 方式已改与 H5 同址(URL), 本值不再产生; 保留枚举值仅维持镜像一致。
    PARAMS,
    /// JSON 结构(JSAPI/公众号/小程序方式, 拟真 prepay 结构)
    JSON,
    ;
}
