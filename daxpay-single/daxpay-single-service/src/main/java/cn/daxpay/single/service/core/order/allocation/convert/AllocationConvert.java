package cn.daxpay.single.service.core.order.allocation.convert;

import cn.daxpay.single.service.core.order.allocation.entity.AllocationOrder;
import cn.daxpay.single.service.core.order.allocation.entity.AllocationOrderDetail;
import cn.daxpay.single.service.dto.order.allocation.AllocationOrderDetailDto;
import cn.daxpay.single.service.dto.order.allocation.AllocationOrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 分账实体类转换
 * @author xxm
 * @since 2024/4/7
 */
@Mapper
public interface AllocationConvert {
    AllocationConvert CONVERT = Mappers.getMapper(AllocationConvert.class);


    AllocationOrderDto convert(AllocationOrder in);

    AllocationOrderDetailDto convert(AllocationOrderDetail in);

}
