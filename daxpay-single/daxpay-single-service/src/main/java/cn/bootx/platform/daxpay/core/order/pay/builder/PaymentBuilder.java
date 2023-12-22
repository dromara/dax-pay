package cn.bootx.platform.daxpay.core.order.pay.builder;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.core.order.pay.entity.PayOrderChannel;
import cn.bootx.platform.daxpay.core.order.pay.entity.PayOrderExtra;
import cn.bootx.platform.daxpay.core.order.pay.entity.PayOrderRefundableInfo;
import cn.bootx.platform.daxpay.core.payment.pay.local.AsyncPayInfo;
import cn.bootx.platform.daxpay.core.payment.pay.local.AsyncPayInfoLocal;
import cn.bootx.platform.daxpay.param.pay.PayParam;
import cn.bootx.platform.daxpay.param.pay.PayWayParam;
import cn.bootx.platform.daxpay.result.pay.PayResult;
import cn.hutool.core.collection.CollectionUtil;
import lombok.experimental.UtilityClass;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 支付对象构建器
 *
 * @author xxm
 * @since 2021/2/25
 */
@UtilityClass
public class PaymentBuilder {

    /**
     * 构建payment记录
     */
    public PayOrder buildPayOrder(PayParam payParam) {
        // 可退款信息
        List<PayOrderRefundableInfo> refundableInfos = buildRefundableInfo(payParam.getPayWays());
        // 计算总价
        int sumAmount = payParam.getPayWays().stream()
                .map(PayWayParam::getAmount)
                .filter(Objects::nonNull)
                .reduce(Integer::sum)
                .orElse(0);
        // 是否有异步支付方式
        Optional<String> asyncPayMode = payParam.getPayWays().stream()
                .map(PayWayParam::getChannel)
                .filter(PayChannelEnum.ASYNC_TYPE_CODE::contains)
                .findFirst();
        // 构建支付订单对象
        return new PayOrder()
                .setBusinessNo(payParam.getBusinessNo())
                .setTitle(payParam.getTitle())
                .setRefundableInfos(refundableInfos)
                .setStatus(PayStatusEnum.PROGRESS.getCode())
                .setAmount(sumAmount)
                .setCombinationPayMode(payParam.getPayWays().size() > 1)
                .setAsyncPayMode(asyncPayMode.isPresent())
                .setAsyncPayChannel(asyncPayMode.orElse(null))
                .setRefundableBalance(sumAmount);
    }

    /**
     * 构建支付订单的额外信息
     * @param payParam 支付参数
     * @param paymentId 支付订单id
     */
    public PayOrderExtra buildPayOrderExtra(PayParam payParam, Long paymentId) {
        PayOrderExtra payOrderExtra = new PayOrderExtra()
                .setClientIp(payParam.getClientIp())
                .setDescription(payParam.getDescription())
                .setNotReturn(payParam.isNotReturn())
                .setReturnUrl(payParam.getReturnUrl())
                .setNotNotify(payParam.isNotNotify())
                .setNotifyUrl(payParam.getNotifyUrl())
                .setSign(payParam.getSign())
                .setSignType(payParam.getSignType())
                .setSignType(payParam.getSign())
                .setReqTime(payParam.getReqTime());
        payOrderExtra.setId(paymentId);
        return payOrderExtra;
    }

    /**
     * 构建订单关联通道信息
     */
    public List<PayOrderChannel> buildPayChannel(List<PayWayParam> payWayParams) {
        if (CollectionUtil.isEmpty(payWayParams)) {
            return Collections.emptyList();
        }
        return payWayParams.stream()
                .map(o-> new PayOrderChannel()
                        .setChannel(o.getChannel())
                        .setPayWay(o.getWay())
                        .setAmount(o.getAmount())
                        .setChannelExtra(o.getChannelExtra()))
                .collect(Collectors.toList());
    }

    /**
     * 构建支付订单的可退款信息列表
     */
    private List<PayOrderRefundableInfo> buildRefundableInfo(List<PayWayParam> payWayParamList) {
        if (CollectionUtil.isEmpty(payWayParamList)) {
            return Collections.emptyList();
        }
        return payWayParamList.stream()
                .map(o-> new PayOrderRefundableInfo()
                        .setChannel(o.getChannel())
                        .setAmount(o.getAmount()))
                .collect(Collectors.toList());
    }


    /**
     * 根据支付订单构建支付结果
     * @param payOrder 支付订单
     * @return PayResult 支付结果
     */
    public PayResult buildPayResultByPayOrder(PayOrder payOrder) {
        PayResult paymentResult;
        try {
            paymentResult = new PayResult()
                    .setPaymentId(payOrder.getId())
                    .setAsyncPayMode(payOrder.isAsyncPayMode())
                    .setAsyncPayChannel(payOrder.getAsyncPayChannel())
                    .setStatus(payOrder.getStatus());

            // 设置异步支付参数
            AsyncPayInfo asyncPayInfo = AsyncPayInfoLocal.get();
            if (Objects.nonNull(asyncPayInfo)) {
                paymentResult.setPayBody(AsyncPayInfoLocal.get().getPayBody());
            }
        }
        finally {
            // 清空线程变量
            AsyncPayInfoLocal.clear();
        }
        return paymentResult;
    }

    /**
     * 根据新的新传入的支付参数和支付订单构建出当前需要支付参数
     * 主要是针对其中的异步支付参数进行处理
     *
     * @param newPayParam 新传入的参数
     * @param payOrder 支付订单
     * @return PayParam 支付参数
     */
    public PayParam buildPayParam(PayParam newPayParam, PayOrder payOrder) {


        return null;
    }
}