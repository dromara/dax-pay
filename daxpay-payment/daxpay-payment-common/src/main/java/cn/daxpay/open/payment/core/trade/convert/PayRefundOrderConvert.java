package cn.daxpay.open.payment.core.trade.convert;

import cn.daxpay.open.payment.core.trade.entity.PayRefundOrder;
import cn.daxpay.open.payment.core.trade.result.PayRefundOrderResult;
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
