package cn.daxpay.single.service.core.record.repair.convert;

import cn.daxpay.single.service.core.record.repair.entity.TradeAdjustRecord;
import cn.daxpay.single.service.dto.record.repair.TradeAdjustRecordDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/7/15
 */
@Mapper
public interface TradeAdjustRecordConvert {
    TradeAdjustRecordConvert CONVERT = Mappers.getMapper(TradeAdjustRecordConvert.class);

    TradeAdjustRecordDto convert(TradeAdjustRecord in);
}
