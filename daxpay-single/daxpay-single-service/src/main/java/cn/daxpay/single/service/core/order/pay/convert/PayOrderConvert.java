package cn.daxpay.single.service.core.order.pay.convert;

import cn.daxpay.single.result.order.PayOrderResult;
import cn.daxpay.single.service.core.order.pay.entity.PayOrder;
import cn.daxpay.single.service.core.payment.notice.result.PayNoticeResult;
import cn.daxpay.single.service.dto.order.pay.PayOrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/1/9
 */
@Mapper
public interface PayOrderConvert {
    PayOrderConvert CONVERT = Mappers.getMapper(PayOrderConvert.class);

    PayOrderDto convert(PayOrder in);

    PayOrderResult convertResult(PayOrder in);

    PayNoticeResult convertNotice(PayOrder order);
}
