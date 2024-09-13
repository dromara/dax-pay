package cn.daxpay.single.service.core.order.allocation.convert;

import cn.daxpay.single.core.result.order.AllocOrderDetailResult;
import cn.daxpay.single.core.result.order.AllocOrderResult;
import cn.daxpay.single.service.core.order.allocation.entity.AllocOrder;
import cn.daxpay.single.service.core.order.allocation.entity.AllocOrderDetail;
import cn.daxpay.single.service.core.payment.notice.result.AllocDetailNoticeResult;
import cn.daxpay.single.service.core.payment.notice.result.AllocNoticeResult;
import cn.daxpay.single.service.dto.order.allocation.AllocOrderDto;
import cn.daxpay.single.service.dto.order.allocation.AllocOrderDetailDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 分账实体类转换
 * @author xxm
 * @since 2024/4/7
 */
@Mapper
public interface AllocOrderConvert {
    AllocOrderConvert CONVERT = Mappers.getMapper(AllocOrderConvert.class);

    AllocOrderDto convert(AllocOrder in);

    AllocOrderResult toResult(AllocOrder in);

    AllocOrderDetailResult toResult(AllocOrderDetail in);

    AllocOrderDetailDto convert(AllocOrderDetail in);

    AllocNoticeResult toNotice(AllocOrder in);

    AllocDetailNoticeResult toNotice(AllocOrderDetail in);

}
