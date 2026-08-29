package cn.daxpay.open.payment.trade.abnormal.convert;

import cn.daxpay.open.payment.trade.abnormal.entity.AbnormalOrder;
import cn.daxpay.open.payment.trade.abnormal.result.AbnormalOrderResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 异常订单转换
///
@Mapper
public interface AbnormalOrderConvert {

    AbnormalOrderConvert CONVERT = Mappers.getMapper(AbnormalOrderConvert.class);

    AbnormalOrderResult toResult(AbnormalOrder entity);
}
