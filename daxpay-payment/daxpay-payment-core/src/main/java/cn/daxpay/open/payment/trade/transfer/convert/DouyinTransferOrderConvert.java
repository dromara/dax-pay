package cn.daxpay.open.payment.trade.transfer.convert;

import cn.daxpay.open.payment.trade.transfer.entity.DouyinTransferOrder;
import cn.daxpay.open.payment.trade.transfer.result.DouyinTransferOrderResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 抖音转账单转换器
///
/// 仅做实体到 Result 的同名映射
@Mapper
public interface DouyinTransferOrderConvert {

    DouyinTransferOrderConvert CONVERT = Mappers.getMapper(DouyinTransferOrderConvert.class);

    /// DouyinTransferOrder → Result
    DouyinTransferOrderResult toResult(DouyinTransferOrder entity);
}
