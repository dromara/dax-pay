package cn.daxpay.open.payment.old.pay.convert.order.pay;

import cn.daxpay.open.payment.common.callback.CallbackData;
import cn.daxpay.open.payment.old.pay.bo.sync.PaySyncResultBo;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPayOrderResult;
import cn.daxpay.open.payment.old.pay.entity.order.pay.PayOrder;
import cn.daxpay.open.payment.old.pay.entity.order.pay.PayOrderExpand;
import cn.daxpay.open.payment.old.pay.result.order.pay.PayOrderExpandResult;
import cn.daxpay.open.payment.old.pay.result.order.pay.PayOrderVo;
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

    NormalPayOrderResult toResult(PayOrder payOrder);

    PayOrderExpandResult toResult(PayOrderExpand payOrder);

    @Mapping(target = "limitPay", source = "limitPay")
    void copy(NormalPayParam param, @MappingTarget PayOrder payOrder);

    /// 将 List<String> limitPay 转换为逗号分隔的字符串
    default String mapListToString(List<String> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return String.join(",", list);
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(CallbackData callbackData, @MappingTarget  PayOrderExpand orderExpand);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(PaySyncResultBo resultBo, @MappingTarget PayOrderExpand payOrderExpand);

    void copy(PayOrderExpand orderExpand, @MappingTarget NormalPayOrderResult noticeResult);
}
