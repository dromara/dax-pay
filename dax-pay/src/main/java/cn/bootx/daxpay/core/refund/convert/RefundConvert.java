package cn.bootx.daxpay.core.refund.convert;

import cn.bootx.daxpay.core.refund.entity.RefundRecord;
import cn.bootx.daxpay.dto.refund.RefundRecordDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author xxm
 * @date 2022/3/2
 */
@Mapper
public interface RefundConvert {

    RefundConvert CONVERT = Mappers.getMapper(RefundConvert.class);

    RefundRecordDto convert(RefundRecord in);

}
