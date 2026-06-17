package org.dromara.daxpay.payment.old.pay.convert.order.pay;

import org.dromara.daxpay.payment.common.context.CallbackInfo;
import org.dromara.daxpay.payment.old.pay.bo.sync.PaySyncResultBo;
import org.dromara.daxpay.payment.unipay.param.trade.pay.PayParam;
import org.dromara.daxpay.payment.unipay.result.trade.pay.PayOrderResult;
import org.dromara.daxpay.payment.old.pay.entity.order.pay.PayOrder;
import org.dromara.daxpay.payment.old.pay.entity.order.pay.PayOrderExpand;
import org.dromara.daxpay.payment.old.pay.result.order.pay.PayOrderExpandResult;
import org.dromara.daxpay.payment.old.pay.result.order.pay.PayOrderVo;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import java.util.List;

/// # 支付订单
///
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PayOrderConvert {
    PayOrderConvert CONVERT = Mappers.getMapper(PayOrderConvert.class);

    PayOrderVo toVo(PayOrder payOrder);

    PayOrderResult toResult(PayOrder payOrder);

    PayOrderExpandResult toResult(PayOrderExpand payOrder);

    @Mapping(target = "limitPay", source = "limitPay")
    void copy(PayParam param, @MappingTarget PayOrder payOrder);

    /// 将 List<String> limitPay 转换为逗号分隔的字符串
    default String mapListToString(List<String> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return String.join(",", list);
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(CallbackInfo callbackInfo, @MappingTarget  PayOrderExpand orderExpand);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(PaySyncResultBo resultBo, @MappingTarget PayOrderExpand payOrderExpand);

    void copy(PayOrderExpand orderExpand, @MappingTarget PayOrderResult noticeResult);
}
