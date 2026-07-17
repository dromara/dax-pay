package cn.daxpay.open.payment.trade.order.convert;

import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.order.result.RefundOrderResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 退款订单转换器
///
/// 仅做实体到 Result 的同名映射
@Mapper
public interface RefundOrderConvert {

    RefundOrderConvert CONVERT = Mappers.getMapper(RefundOrderConvert.class);

    /// RefundOrder → Result
    RefundOrderResult toResult(RefundOrder entity);
}
