package cn.daxpay.single.service.core.order.pay.builder;

import cn.daxpay.single.code.PayOrderAllocStatusEnum;
import cn.daxpay.single.code.PayOrderRefundStatusEnum;
import cn.daxpay.single.code.PayStatusEnum;
import cn.daxpay.single.param.payment.pay.PayParam;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.order.pay.entity.PayOrder;
import cn.daxpay.single.service.core.order.pay.entity.PayOrderExtra;
import cn.daxpay.single.util.OrderNoGenerateUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

/**
 * 支付对象构建器
 *
 * @author xxm
 * @since 2021/2/25
 */
@UtilityClass
public class PayBuilder {

    /**
     * 构建支付订单
     */
    public PayOrder buildPayOrder(PayParam payParam) {
        // 订单超时时间
        LocalDateTime expiredTime = PaymentContextLocal.get()
                .getPayInfo()
                .getExpiredTime();
        // 构建支付订单对象
        PayOrder payOrder = new PayOrder()
                .setBizOrderNo(payParam.getBizOrderNo())
                .setOrderNo(OrderNoGenerateUtil.pay())
                .setTitle(payParam.getTitle())
                .setDescription(payParam.getDescription())
                .setStatus(PayStatusEnum.PROGRESS.getCode())
                .setRefundStatus(PayOrderRefundStatusEnum.NO_REFUND.getCode())
                .setAllocation(payParam.getAllocation())
                .setAmount(payParam.getAmount())
                .setChannel(payParam.getChannel())
                .setMethod(payParam.getMethod())
                .setExpiredTime(expiredTime)
                .setRefundableBalance(payParam.getAmount());
        // 如果支持分账, 设置分账状态为代分账
        if (payOrder.getAllocation()) {
            payOrder.setAllocStatus(PayOrderAllocStatusEnum.WAITING.getCode());
        }
        return payOrder;
    }

    /**
     * 构建支付订单的额外信息
     * @param payParam 支付参数
     * @param payOrderId 支付订单id
     */
    public PayOrderExtra buildPayOrderExtra(PayParam payParam, Long payOrderId) {
        PayOrderExtra payOrderExtra = new PayOrderExtra()
                .setClientIp(payParam.getClientIp())
                .setNotifyUrl(payParam.getNotifyUrl())
                .setReturnUrl(payParam.getReturnUrl())
                .setAttach(payParam.getAttach())
                .setReqTime(payParam.getReqTime());
        // 扩展参数
        if (CollUtil.isNotEmpty(payParam.getExtraParam())) {
            payOrderExtra.setExtraParam(JSONUtil.toJsonStr(payParam.getExtraParam()));
        }

        payOrderExtra.setId(payOrderId);
        return payOrderExtra;
    }

}
