package cn.daxpay.open.payment.core.trade.order.convert;

import cn.daxpay.open.payment.core.trade.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.core.trade.order.result.NormalPayOrderResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 普通支付业务单转换器(管理)
///
/// 仅做容器(NormalPayOrder)到列表 Result 的映射;
/// 详情场景的资金凭证(PayTrade)字段由 Service 层手动补充, 避免多参数源同名字段歧义
@Mapper
public interface NormalPayOrderConvert {

    NormalPayOrderConvert CONVERT = Mappers.getMapper(NormalPayOrderConvert.class);

    /// NormalPayOrder → Result (列表用, 不含资金凭证字段)
    NormalPayOrderResult toResult(NormalPayOrder entity);
}
