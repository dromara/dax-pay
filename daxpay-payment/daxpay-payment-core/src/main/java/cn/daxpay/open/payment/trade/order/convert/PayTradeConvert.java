package cn.daxpay.open.payment.trade.order.convert;

import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPayResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/// # 支付交易转换器
///
/// 仅改名映射 orderId ← id；其余同名字段自动映射；orderNo/payBody 等无源字段保持 null，
/// 由 [cn.daxpay.open.payment.trade.runtime.service.pay.normal.NormalPayAssistService#buildResult] 组装
@Mapper
public interface PayTradeConvert {

    PayTradeConvert CONVERT = Mappers.getMapper(PayTradeConvert.class);

    /// PayTrade → NormalPayResult（不含 orderNo/payBody, 需配合容器）
    @Mapping(target = "orderId", source = "id")
    NormalPayResult toResult(PayTrade trade);
}
