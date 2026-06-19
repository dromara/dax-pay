package cn.daxpay.open.payment.pay.convert;

import cn.daxpay.open.payment.pay.order.entity.PayNormalOrder;
import cn.daxpay.open.payment.pay.order.entity.PayTrade;
import cn.daxpay.open.payment.unipay.result.trade.pay.PayResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/// # 支付交易转换器
///
@Mapper
public interface PayTradeConvert {

    PayTradeConvert CONVERT = Mappers.getMapper(PayTradeConvert.class);

    /// PayTrade + PayNormalOrder → PayResult
    @Mapping(target = "orderId", source = "trade.id")
    @Mapping(target = "orderNo", source = "trade.tradeNo")
    @Mapping(target = "status", source = "trade.status")
    @Mapping(target = "bizOrderNo", source = "normalOrder.bizOrderNo")
    @Mapping(target = "payBody", source = "trade.payBody")
    @Mapping(target = "payBodyType", source = "trade.payBodyType")
    PayResult toResult(PayTrade trade, PayNormalOrder normalOrder);
}
