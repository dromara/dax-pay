package cn.daxpay.open.payment.unipay.trade.convert;

import cn.daxpay.open.payment.trade.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPayOrderResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/// # 普通支付订单转换器(对外查询)
///
/// 将 core 双表(NormalPayOrder 业务容器 + PayTrade 资金凭证)映射为对外契约 NormalPayOrderResult
/// 资金态字段(status / outOrderNo / refundableBalance 等)取自 PayTrade, 业务字段取自 NormalPayOrder
///
/// 仅声明：改名、多源同名消歧、以及目标独有字段的 ignore（由服务层后填或暂不输出）
@Mapper
public interface UnipayNormalPayOrderConvert {

    UnipayNormalPayOrderConvert CONVERT = Mappers.getMapper(UnipayNormalPayOrderConvert.class);

    /// NormalPayOrder + PayTrade → 对外 NormalPayOrderResult
    // 改名
    @Mapping(target = "orderNo", source = "trade.tradeNo")
    // 多源同名消歧：资金态取 trade，业务容器取 order
    @Mapping(target = "status", source = "trade.status")
    @Mapping(target = "bizOrderNo", source = "order.bizOrderNo")
    @Mapping(target = "channel", source = "order.channel")
    @Mapping(target = "method", source = "order.method")
    @Mapping(target = "amount", source = "order.amount")
    @Mapping(target = "payTime", source = "order.payTime")
    @Mapping(target = "closeTime", source = "order.closeTime")
    @Mapping(target = "expiredTime", source = "order.expiredTime")
    // 目标独有字段：源无对应属性，显式 ignore 标明不在此转换
    @Mapping(target = "realAmount", ignore = true)
    @Mapping(target = "refundStatus", ignore = true)
    NormalPayOrderResult toResult(NormalPayOrder order, PayTrade trade);
}
