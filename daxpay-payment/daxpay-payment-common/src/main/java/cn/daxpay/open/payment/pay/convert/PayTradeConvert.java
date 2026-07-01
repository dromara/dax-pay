package cn.daxpay.open.payment.pay.convert;

import cn.daxpay.open.payment.pay.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.pay.order.entity.PayTrade;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPayResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/// # 支付交易转换器
///
@Mapper
public interface PayTradeConvert {

    PayTradeConvert CONVERT = Mappers.getMapper(PayTradeConvert.class);

    /// PayTrade + NormalPayOrder → NormalPayResult
    @Mapping(target = "orderId", source = "trade.id")
    @Mapping(target = "orderNo", source = "trade.tradeNo")
    @Mapping(target = "status", source = "trade.status")
    @Mapping(target = "bizOrderNo", source = "normalOrder.bizOrderNo")
    @Mapping(target = "payBody", source = "trade.payBody")
    @Mapping(target = "payBodyType", source = "trade.payBodyType")
    NormalPayResult toResult(PayTrade trade, NormalPayOrder normalOrder);
}
