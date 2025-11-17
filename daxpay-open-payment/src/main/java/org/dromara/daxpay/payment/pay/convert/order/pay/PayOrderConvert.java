package org.dromara.daxpay.payment.pay.convert.order.pay;

import org.dromara.daxpay.payment.common.context.CallbackLocal;
import org.dromara.daxpay.payment.pay.bo.sync.PaySyncResultBo;
import org.dromara.daxpay.payment.pay.entity.order.pay.PayOrder;
import org.dromara.daxpay.payment.pay.entity.order.pay.PayOrderExpand;
import org.dromara.daxpay.payment.pay.result.order.pay.PayOrderExpandResult;
import org.dromara.daxpay.payment.pay.result.order.pay.PayOrderVo;
import org.dromara.daxpay.payment.unipay.param.trade.pay.PayParam;
import org.dromara.daxpay.payment.unipay.result.trade.pay.PayOrderResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/**
 * 支付订单
 * @author xxm
 * @since 2024/6/27
 */
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PayOrderConvert {
    PayOrderConvert CONVERT = Mappers.getMapper(PayOrderConvert.class);

    PayOrderVo toVo(PayOrder payOrder);

    PayOrderResult toResult(PayOrder payOrder);

    PayOrderExpandResult toResult(PayOrderExpand payOrder);

    void copy(PayParam param, @MappingTarget PayOrder payOrder);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(CallbackLocal callbackInfo, @MappingTarget  PayOrderExpand orderExpand);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(PaySyncResultBo resultBo, @MappingTarget PayOrderExpand payOrderExpand);
}
