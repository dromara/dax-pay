package cn.daxpay.multi.service.convert.record;

import cn.daxpay.multi.service.entity.record.flow.TradeFlowRecord;
import cn.daxpay.multi.service.result.record.flow.TradeFlowRecordResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/4/21
 */
@Mapper
public interface TradeFlowRecordConvert {
    TradeFlowRecordConvert CONVERT = Mappers.getMapper(TradeFlowRecordConvert.class);

    TradeFlowRecordResult convert(TradeFlowRecord entity);
}
