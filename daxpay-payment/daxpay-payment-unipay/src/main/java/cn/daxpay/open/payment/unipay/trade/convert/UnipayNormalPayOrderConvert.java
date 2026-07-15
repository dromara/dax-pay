package cn.daxpay.open.payment.unipay.trade.convert;

import cn.daxpay.open.payment.trade.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPayOrderResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 普通支付订单转换器(对外查询)
///
/// 仅同名映射业务容器(NormalPayOrder)；资金凭证字段(tradeNo / outOrderNo / status / refundableBalance)
/// 由 [cn.daxpay.open.payment.unipay.trade.service.NormalPayOrderQueryService] 手写补充
@Mapper
public interface UnipayNormalPayOrderConvert {

    UnipayNormalPayOrderConvert CONVERT = Mappers.getMapper(UnipayNormalPayOrderConvert.class);

    /// 仅同名映射业务容器；资金字段由 Service 补
    NormalPayOrderResult toResult(NormalPayOrder order);
}
