package cn.daxpay.single.service.core.record.flow.convert;

import cn.daxpay.single.service.core.record.flow.entity.TradeFlowRecord;
import cn.daxpay.single.service.dto.record.flow.TradeFlowRecordDto;
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

    TradeFlowRecordDto convert(TradeFlowRecord entity);
}
