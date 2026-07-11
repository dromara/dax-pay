package cn.daxpay.open.payment.core.trade.convert;

import cn.daxpay.open.payment.core.trade.entity.PayTrade;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPayResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/// # 支付交易转换器
///
/// 仅从 PayTrade 映射对外支付响应；同名字段(status/bizOrderNo/payBody 等)自动映射，
/// 仅声明改名字段
@Mapper
public interface PayTradeConvert {

    PayTradeConvert CONVERT = Mappers.getMapper(PayTradeConvert.class);

    /// PayTrade → NormalPayResult
    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "orderNo", source = "tradeNo")
    NormalPayResult toResult(PayTrade trade);
}
