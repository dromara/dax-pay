package cn.daxpay.single.service.core.order.allocation.convert;

import cn.daxpay.single.result.order.AllocOrderDetailResult;
import cn.daxpay.single.result.order.AllocOrderResult;
import cn.daxpay.single.service.core.order.allocation.entity.AllocationOrder;
import cn.daxpay.single.service.core.order.allocation.entity.AllocationOrderDetail;
import cn.daxpay.single.service.core.order.allocation.entity.AllocationOrderExtra;
import cn.daxpay.single.service.core.payment.notice.result.AllocDetailNoticeResult;
import cn.daxpay.single.service.core.payment.notice.result.AllocNoticeResult;
import cn.daxpay.single.service.dto.order.allocation.AllocationOrderDetailDto;
import cn.daxpay.single.service.dto.order.allocation.AllocationOrderDto;
import cn.daxpay.single.service.dto.order.allocation.AllocationOrderExtraDto;
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

    AllocationOrderExtraDto convert(AllocationOrderExtra in);

    AllocOrderResult toResult(AllocationOrder in);

    AllocOrderDetailResult toResult(AllocationOrderDetail in);

    AllocationOrderDetailDto convert(AllocationOrderDetail in);

    AllocNoticeResult toNotice(AllocationOrder in);

    AllocDetailNoticeResult toNotice(AllocationOrderDetail in);

}
