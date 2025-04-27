package org.dromara.daxpay.service.convert.allocation;

import org.dromara.daxpay.core.result.allocation.order.AllocDetailResult;
import org.dromara.daxpay.core.result.allocation.order.AllocOrderResult;
import org.dromara.daxpay.service.entity.allocation.order.AllocDetail;
import org.dromara.daxpay.service.entity.allocation.order.AllocOrder;
import org.dromara.daxpay.service.result.allocation.order.AllocDetailVo;
import org.dromara.daxpay.service.result.allocation.order.AllocOrderVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 *
 * @author xxm
 * @since 2024/11/15
 */
@Mapper
public interface AllocOrderConvert {
    AllocOrderConvert CONVERT = Mappers.getMapper(AllocOrderConvert.class);

    AllocOrderResult toResult(AllocOrder in);

    AllocDetailResult toResult(AllocDetail in);

    List<AllocDetailResult> toList(List<AllocDetail> in);

    AllocOrderVo toVo(AllocOrder in);

    AllocDetailVo toVo(AllocDetail in);
}
