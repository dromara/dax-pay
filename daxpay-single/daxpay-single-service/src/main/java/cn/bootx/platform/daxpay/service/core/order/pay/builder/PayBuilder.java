package cn.bootx.platform.daxpay.service.core.order.pay.builder;

import cn.bootx.platform.daxpay.code.PayOrderAllocationStatusEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.param.payment.pay.PayParam;
import cn.bootx.platform.daxpay.service.common.context.NoticeLocal;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrderExtra;
import cn.bootx.platform.daxpay.util.OrderNoGenerateUtil;
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
                .setAllocation(payParam.getAllocation())
                .setAmount(payParam.getAmount())
                .setChannel(payParam.getChannel())
                .setMethod(payParam.getMethod())
                .setExpiredTime(expiredTime)
                .setRefundableBalance(payParam.getAmount());
        // 如果支持分账, 设置分账状态为代分账
        if (payOrder.getAllocation()) {
            payOrder.setAllocationStatus(PayOrderAllocationStatusEnum.WAITING.getCode());
        }
        return payOrder;
    }

    /**
     * 构建支付订单的额外信息
     * @param payParam 支付参数
     * @param payOrderId 支付订单id
     */
    public PayOrderExtra buildPayOrderExtra(PayParam payParam, Long payOrderId) {
        NoticeLocal noticeInfo = PaymentContextLocal.get().getNoticeInfo();
        PayOrderExtra payOrderExtra = new PayOrderExtra()
                .setClientIp(payParam.getClientIp())
                .setNotifyUrl(noticeInfo.getNotifyUrl())
                .setReturnUrl(noticeInfo.getReturnUrl())
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
