package cn.daxpay.open.payment.pay.convert;

import cn.daxpay.open.payment.pay.order.entity.PayTrade;
import cn.daxpay.open.payment.pay.result.order.PayTradeResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 资金交易凭证转换器(管理)
///
/// 仅做凭证(PayTrade)到列表 Result 的映射;
/// 详情场景的容器(NormalPayOrder)字段由 Service 层手动补充, 避免多参数源同名字段歧义
@Mapper
public interface PayTradeAdminConvert {

    PayTradeAdminConvert CONVERT = Mappers.getMapper(PayTradeAdminConvert.class);

    /// PayTrade → Result (列表用, 不含容器字段)
    PayTradeResult toResult(PayTrade entity);
}
