package cn.daxpay.open.payment.trade.transfer.convert;

import cn.daxpay.open.payment.trade.transfer.entity.AlipayTransferOrder;
import cn.daxpay.open.payment.trade.transfer.result.AlipayTransferOrderResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 支付宝转账单转换器
///
/// 仅做实体到 Result 的同名映射
@Mapper
public interface AlipayTransferOrderConvert {

    AlipayTransferOrderConvert CONVERT = Mappers.getMapper(AlipayTransferOrderConvert.class);

    /// AlipayTransferOrder → Result
    AlipayTransferOrderResult toResult(AlipayTransferOrder entity);
}
