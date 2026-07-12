package cn.daxpay.open.payment.core.trade.order.convert;

import cn.daxpay.open.payment.core.trade.order.entity.PayRefundOrder;
import cn.daxpay.open.payment.core.trade.order.result.PayRefundOrderResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 退款订单转换器(管理)
///
@Mapper
public interface PayRefundOrderConvert {

    PayRefundOrderConvert CONVERT = Mappers.getMapper(PayRefundOrderConvert.class);

    /// PayRefundOrder → Result
    PayRefundOrderResult toResult(PayRefundOrder entity);
}
