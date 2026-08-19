package cn.daxpay.open.payment.unipay.trade.convert;

import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.unipay.param.trade.refund.RefundParam;
import cn.daxpay.open.payment.unipay.result.trade.refund.RefundOrderResult;
import cn.daxpay.open.payment.unipay.result.trade.refund.RefundResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 退款转换器(对外)
///
/// 汇聚退款场景的对外映射:
/// - 退款单实体 → 对外查询结果 [RefundOrderResult] (内部字段 mchNo/channelMchNo 等自动忽略)
/// - 退款单实体 → 发起响应 [RefundResult] (仅退款号/状态等对外字段)
/// - 对外签名入参 [RefundParam] → 内部编排参数 (mchNo 同名映射透传供商户维度幂等/归属校验; appId/sign 等签名字段丢弃)
@Mapper
public interface UnipayRefundOrderConvert {

    UnipayRefundOrderConvert CONVERT = Mappers.getMapper(UnipayRefundOrderConvert.class);

    /// 退款单实体 → 对外查询结果(同名映射)
    RefundOrderResult toResult(RefundOrder order);

    /// 退款单实体 → 发起响应(同名映射)
    RefundResult toRefundResult(RefundOrder order);

    /// 对外签名入参 → 内部编排参数(同名字段映射, mchNo 透传; 签名/应用字段丢弃)
    cn.daxpay.open.payment.trade.runtime.param.RefundParam toRuntime(RefundParam param);
}
