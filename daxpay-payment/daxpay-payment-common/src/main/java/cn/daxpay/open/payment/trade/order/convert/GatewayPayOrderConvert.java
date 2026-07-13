package cn.daxpay.open.payment.trade.order.convert;

import cn.daxpay.open.payment.trade.order.entity.GatewayPayOrder;
import cn.daxpay.open.payment.trade.order.result.GatewayPayOrderResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 网关支付业务单转换
@Mapper
public interface GatewayPayOrderConvert {

    GatewayPayOrderConvert CONVERT = Mappers.getMapper(GatewayPayOrderConvert.class);

    GatewayPayOrderResult toResult(GatewayPayOrder entity);
}
