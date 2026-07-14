package cn.daxpay.open.payment.trade.order.convert;

import cn.daxpay.open.payment.trade.order.entity.GatewayPayOrder;
import cn.daxpay.open.payment.trade.order.result.GatewayPayOrderResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 网关支付业务单转换(管理)
///
/// 仅做容器(GatewayPayOrder)到列表 Result 的映射;
/// 详情场景的资金凭证(PayTrade)字段由 Service 层手动补充, 与 [NormalPayOrderConvert] 一致
@Mapper
public interface GatewayPayOrderConvert {

    GatewayPayOrderConvert CONVERT = Mappers.getMapper(GatewayPayOrderConvert.class);

    /// GatewayPayOrder → Result (列表用, 不含资金凭证字段)
    GatewayPayOrderResult toResult(GatewayPayOrder entity);
}
