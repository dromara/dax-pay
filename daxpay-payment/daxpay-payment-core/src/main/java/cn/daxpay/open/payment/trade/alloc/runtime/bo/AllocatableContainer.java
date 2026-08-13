package cn.daxpay.open.payment.trade.alloc.runtime.bo;

/// # 分账源容器契约
///
/// 分账发起需定位原支付容器并读取通道凭证快照字段(bizOrderNo/title/product/capability/channelAppId/appId)
/// 及分账标记(allocation)。由 normal / gateway 等业务容器实现,
/// 解除 [cn.daxpay.open.payment.trade.alloc.runtime.service.AllocStartService] 对具体容器的写死依赖。
public interface AllocatableContainer {

    /// 商户业务单号
    String getBizOrderNo();

    /// 标题
    String getTitle();

    /// 支付产品编码
    String getProduct();

    /// 支付能力编码
    String getCapability();

    /// 通道应用 AppId(实际支付所用)
    String getChannelAppId();

    /// 应用号
    String getAppId();

    /// 是否分账订单
    Boolean getAllocation();
}
