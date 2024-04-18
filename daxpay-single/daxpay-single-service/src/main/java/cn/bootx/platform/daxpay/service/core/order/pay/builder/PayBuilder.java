package cn.bootx.platform.daxpay.service.core.order.pay.builder;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayOrderAllocationStatusEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.param.payment.pay.PayParam;
import cn.bootx.platform.daxpay.result.pay.PayResult;
import cn.bootx.platform.daxpay.service.common.context.ApiInfoLocal;
import cn.bootx.platform.daxpay.service.common.context.PayLocal;
import cn.bootx.platform.daxpay.service.common.context.NoticeLocal;
import cn.bootx.platform.daxpay.service.common.context.PlatformLocal;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayChannelOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrderExtra;
import cn.bootx.platform.daxpay.util.OrderNoGenerateUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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
        // 计算总价
        int sumAmount = payParam.getPayChannels().stream()
                .map(PayChannelParam::getAmount)
                .filter(Objects::nonNull)
                .reduce(Integer::sum)
                .orElse(0);
        // 是否有异步支付方式
        Optional<String> asyncPay = payParam.getPayChannels().stream()
                .map(PayChannelParam::getChannel)
                .filter(PayChannelEnum.ASYNC_TYPE_CODE::contains)
                .findFirst();
        // 构建支付订单对象
        PayOrder payOrder = new PayOrder()
                .setBusinessNo(payParam.getBusinessNo())
                .setOrderNo(OrderNoGenerateUtil.trade())
                .setTitle(payParam.getTitle())
                .setStatus(PayStatusEnum.PROGRESS.getCode())
                .setAllocation(payParam.isAllocation())
                .setAmount(sumAmount)
                .setExpiredTime(expiredTime)
                .setCombinationPay(payParam.getPayChannels().size() > 1)
                .setAsyncPay(asyncPay.isPresent())
                .setRefundableBalance(sumAmount);
        // 设置分账状态
        if (payOrder.isAllocation()) {
            payOrder.setAllocationStatus(PayOrderAllocationStatusEnum.WAITING.getCode());
        }
        return payOrder;
    }

    /**
     * 构建支付订单的额外信息
     * @param payParam 支付参数
     * @param paymentId 支付订单id
     */
    public PayOrderExtra buildPayOrderExtra(PayParam payParam, Long paymentId) {
        PlatformLocal platform = PaymentContextLocal.get().getPlatformInfo();
        NoticeLocal noticeInfo = PaymentContextLocal.get().getNoticeInfo();
        ApiInfoLocal apiInfo = PaymentContextLocal.get().getApiInfo();
        PayOrderExtra payOrderExtra = new PayOrderExtra()
                .setClientIp(payParam.getClientIp())
                .setDescription(payParam.getDescription())
                .setNoticeSign(apiInfo.isNoticeSign())
                .setNotifyUrl(noticeInfo.getNotifyUrl())
                .setReturnUrl(noticeInfo.getReturnUrl())
                .setReqSign(payParam.getSign())
                .setReqSignType(platform.getSignType())
                .setAttach(payParam.getAttach())
                .setReqTime(payParam.getReqTime());
        payOrderExtra.setId(paymentId);
        return payOrderExtra;
    }

    /**
     * 构建订单关联通道信息
     */
    public PayChannelOrder buildPayChannelOrder(PayChannelParam channelParam) {
        PayChannelOrder payChannelOrder = new PayChannelOrder()
                .setAsync(PayChannelEnum.ASYNC_TYPE_CODE.contains(channelParam.getChannel()))
                .setChannel(channelParam.getChannel())
                .setPayWay(channelParam.getWay())
                .setAmount(channelParam.getAmount())
                .setRefundableBalance(channelParam.getAmount());

        Map<String, Object> cp = channelParam.getChannelParam();
        if (CollUtil.isNotEmpty(cp)){
            payChannelOrder.setChannelExtra(JSONUtil.toJsonStr(cp));
        }

        return payChannelOrder;
    }


    /**
     * 根据支付订单构建支付结果
     * @param payOrder 支付订单
     * @return PayResult 支付结果
     */
    public PayResult buildPayResultByPayOrder(PayOrder payOrder) {
        PayResult paymentResult;
        paymentResult = new PayResult();
        paymentResult.setPaymentId(payOrder.getId());
        paymentResult.setAsyncPay(payOrder.isAsyncPay());
        paymentResult.setAsyncChannel(payOrder.getAsyncChannel());
        paymentResult.setStatus(payOrder.getStatus());

        // 设置异步支付参数
        PayLocal asyncPayInfo = PaymentContextLocal.get().getPayInfo();;
        if (StrUtil.isNotBlank(asyncPayInfo.getPayBody())) {
            paymentResult.setPayBody(asyncPayInfo.getPayBody());
        }
        return paymentResult;
    }
}
