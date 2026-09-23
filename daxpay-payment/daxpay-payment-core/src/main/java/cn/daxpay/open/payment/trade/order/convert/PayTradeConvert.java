package cn.daxpay.open.payment.trade.order.convert;

import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPayResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 支付交易转换器
///
/// 同名字段(tradeNo/status)自动映射；orderNo/payBody 等无源字段保持 null，
/// 由 [cn.daxpay.open.payment.trade.runtime.service.pay.normal.NormalPayAssistService#buildResult] 组装
@Mapper
public interface PayTradeConvert {

    PayTradeConvert CONVERT = Mappers.getMapper(PayTradeConvert.class);

    /// PayTrade → NormalPayResult（不含 orderNo/payBody, 需配合容器）
    NormalPayResult toResult(PayTrade trade);
}
