package cn.bootx.platform.daxpay.core.refund.record.convert;

import cn.bootx.platform.daxpay.core.refund.record.entity.PayRefundRecord;
import cn.bootx.platform.daxpay.dto.refund.PayRefundRecordDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author xxm
 * @since 2022/3/2
 */
@Mapper
public interface RefundConvert {

    RefundConvert CONVERT = Mappers.getMapper(RefundConvert.class);

    PayRefundRecordDto convert(PayRefundRecord in);

}
