package cn.daxpay.open.payment.trade.order.convert;

import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.order.result.PayTradeResult;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/// # 资金交易凭证 → 管理端 Result 转换器
///
/// 仅做凭证(PayTrade)到列表 Result 的同名映射;
/// 详情场景的容器字段由 [TradeOrderDetailAssembler] 补充, 避免多参数源同名字段歧义
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PayTradeResultConvert {

    PayTradeResultConvert CONVERT = Mappers.getMapper(PayTradeResultConvert.class);

    /// PayTrade → Result (列表用, 不含容器字段)
    PayTradeResult toResult(PayTrade entity);
}
