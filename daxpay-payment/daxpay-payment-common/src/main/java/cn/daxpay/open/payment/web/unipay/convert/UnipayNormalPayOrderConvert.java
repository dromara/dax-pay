package cn.daxpay.open.payment.web.unipay.convert;

import cn.daxpay.open.payment.core.trade.entity.NormalPayOrder;
import cn.daxpay.open.payment.core.trade.entity.PayTrade;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPayOrderResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/// # 普通支付订单转换器(对外查询)
///
/// 将 core 双表(NormalPayOrder 业务容器 + PayTrade 资金凭证)映射为对外契约 NormalPayOrderResult
/// 资金态字段(status / outOrderNo / refundableBalance 等)取自 PayTrade, 业务字段取自 NormalPayOrder
@Mapper
public interface UnipayNormalPayOrderConvert {

    UnipayNormalPayOrderConvert CONVERT = Mappers.getMapper(UnipayNormalPayOrderConvert.class);

    /// NormalPayOrder + PayTrade → 对外 NormalPayOrderResult
    @Mapping(target = "orderNo", source = "trade.tradeNo")
    @Mapping(target = "outOrderNo", source = "trade.outOrderNo")
    @Mapping(target = "otherMethod", source = "trade.otherMethod")
    @Mapping(target = "limitPay", source = "trade.limitPay")
    @Mapping(target = "refundableBalance", source = "trade.refundableBalance")
    @Mapping(target = "status", source = "trade.status")
    @Mapping(target = "provider", source = "trade.provider")
    @Mapping(target = "buyerId", source = "trade.buyerId")
    @Mapping(target = "errorMsg", source = "trade.errorMsg")
    @Mapping(target = "channel", source = "order.channel")
    @Mapping(target = "method", source = "order.method")
    @Mapping(target = "amount", source = "order.amount")
    @Mapping(target = "payTime", source = "order.payTime")
    @Mapping(target = "closeTime", source = "order.closeTime")
    @Mapping(target = "expiredTime", source = "order.expiredTime")
    @Mapping(target = "realAmount", ignore = true)
    @Mapping(target = "refundStatus", ignore = true)
    NormalPayOrderResult toResult(NormalPayOrder order, PayTrade trade);
}
