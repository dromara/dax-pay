package cn.daxpay.open.plugin.easypay.convert;

import cn.daxpay.open.plugin.easypay.entity.EasyPayRefundOrder;
import cn.daxpay.open.plugin.easypay.result.order.EasyPayRefundOrderResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 易支付协议退款订单转换
///
@Mapper
public interface EasyPayRefundOrderConvert {
    EasyPayRefundOrderConvert CONVERT = Mappers.getMapper(EasyPayRefundOrderConvert.class);

    EasyPayRefundOrderResult toResult(EasyPayRefundOrder entity);
}
