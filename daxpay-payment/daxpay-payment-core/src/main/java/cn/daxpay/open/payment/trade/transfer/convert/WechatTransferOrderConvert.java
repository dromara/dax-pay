package cn.daxpay.open.payment.trade.transfer.convert;

import cn.daxpay.open.payment.trade.transfer.entity.WechatTransferOrder;
import cn.daxpay.open.payment.trade.transfer.result.WechatTransferOrderResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 微信转账单转换器
///
/// 仅做实体到 Result 的同名映射
@Mapper
public interface WechatTransferOrderConvert {

    WechatTransferOrderConvert CONVERT = Mappers.getMapper(WechatTransferOrderConvert.class);

    /// WechatTransferOrder → Result
    WechatTransferOrderResult toResult(WechatTransferOrder entity);
}
