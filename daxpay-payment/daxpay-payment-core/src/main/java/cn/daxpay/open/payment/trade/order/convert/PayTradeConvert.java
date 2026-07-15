package cn.daxpay.open.payment.trade.order.convert;

import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPayResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/// # 支付交易转换器
///
/// 仅从 PayTrade 映射部分字段; orderNo/payBody 属容器, 由 [NormalPayAssistService#buildResult] 组装
@Mapper
public interface PayTradeConvert {

    PayTradeConvert CONVERT = Mappers.getMapper(PayTradeConvert.class);

    /// PayTrade → NormalPayResult（不含 orderNo/payBody, 需配合容器）
    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "tradeNo", source = "tradeNo")
    @Mapping(target = "orderNo", ignore = true)
    @Mapping(target = "bizOrderNo", ignore = true)
    @Mapping(target = "payBody", ignore = true)
    @Mapping(target = "payBodyType", ignore = true)
    NormalPayResult toResult(PayTrade trade);
}
