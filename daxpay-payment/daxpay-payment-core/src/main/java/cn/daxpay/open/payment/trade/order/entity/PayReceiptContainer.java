package cn.daxpay.open.payment.trade.order.entity;

import java.time.OffsetDateTime;

/// # 支付回执容器契约
///
/// normal / gateway 双业务容器([NormalPayOrder] / [GatewayPayOrder])的同名回执与终态字段集合,
/// 解除统一处理服务对具体容器的双份平行写法依赖(回执落库/终态翻转/provider 兜底)。
/// 仅声明统一处理所需成员, 与 [cn.daxpay.open.payment.trade.alloc.runtime.bo.AllocatableContainer]
/// (分账凭证快照读取)分工互补; setter 返回容器自身, 与实体 @Accessors(chain=true) 链式风格协变。
public interface PayReceiptContainer {

    /// 支付方式(provider 兜底派生用)
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum
    String getMethod();

    /// 支付渠道(微信/支付宝/银联等)
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum
    String getProvider();

    PayReceiptContainer setStatus(String status);

    PayReceiptContainer setProvider(String provider);

    PayReceiptContainer setBuyerId(String buyerId);

    PayReceiptContainer setPayTime(OffsetDateTime payTime);

    PayReceiptContainer setCloseTime(OffsetDateTime closeTime);

    PayReceiptContainer setTradeProduct(String tradeProduct);

    PayReceiptContainer setTradeWay(String tradeWay);

    PayReceiptContainer setBankType(String bankType);

    PayReceiptContainer setPromotionType(String promotionType);

    /// 支付参数体(如微信 prepay_id 组装串, 仅落容器)
    PayReceiptContainer setPayBody(String payBody);

    /// 支付参数体类型(jsapi/sdk/app)
    PayReceiptContainer setPayBodyType(String payBodyType);

    /// 透传订单号(三方通道产生的透传订单号)
    PayReceiptContainer setTransOrderNo(String transOrderNo);

    /// 实际上送通道的商户订单号(展示冗余)
    PayReceiptContainer setRelationOrderNo(String relationOrderNo);

    PayReceiptContainer setErrorMsg(String errorMsg);
}
