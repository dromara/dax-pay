package cn.bootx.platform.daxpay.service.core.order.transfer.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/3/21
 */
@Mapper
public interface TransferOrderConvert {
    TransferOrderConvert CONVERT = Mappers.getMapper(TransferOrderConvert.class);

}
