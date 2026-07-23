package cn.daxpay.open.payment.merchant.convert.trade;

import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.order.result.PayTradeResult;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/// # 资金交易凭证转换器(商户端)
///
/// 列表页同名映射；详情联表由后续订单页再补
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MchPayTradeConvert {

    MchPayTradeConvert CONVERT = Mappers.getMapper(MchPayTradeConvert.class);

    /// PayTrade → Result（工作台最近交易列表）
    PayTradeResult toResult(PayTrade entity);
}
