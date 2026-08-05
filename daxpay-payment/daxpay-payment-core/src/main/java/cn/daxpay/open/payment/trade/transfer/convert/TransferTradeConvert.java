package cn.daxpay.open.payment.trade.transfer.convert;

import cn.daxpay.open.payment.trade.transfer.entity.TransferTrade;
import cn.daxpay.open.payment.trade.transfer.result.TransferTradeResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 转账资金凭证转换器
///
/// 仅做实体到 Result 的同名映射
@Mapper
public interface TransferTradeConvert {

    TransferTradeConvert CONVERT = Mappers.getMapper(TransferTradeConvert.class);

    /// TransferTrade → Result
    TransferTradeResult toResult(TransferTrade entity);
}
