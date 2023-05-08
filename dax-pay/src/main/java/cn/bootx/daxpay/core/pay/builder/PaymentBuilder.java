package cn.bootx.daxpay.core.pay.builder;

import cn.bootx.common.spring.util.WebServletUtil;
import cn.bootx.daxpay.code.pay.PayChannelCode;
import cn.bootx.daxpay.code.pay.PayStatusCode;
import cn.bootx.daxpay.core.pay.local.AsyncPayInfoLocal;
import cn.bootx.daxpay.core.payment.entity.Payment;
import cn.bootx.daxpay.dto.pay.PayResult;
import cn.bootx.daxpay.dto.payment.PayChannelInfo;
import cn.bootx.daxpay.dto.payment.RefundableInfo;
import cn.bootx.daxpay.param.pay.PayModeParam;
import cn.bootx.daxpay.param.pay.PayParam;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.extra.servlet.ServletUtil;
import lombok.experimental.UtilityClass;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 支付对象构建器
 *
 * @author xxm
 * @date 2021/2/25
 */
@UtilityClass
public class PaymentBuilder {

    /**
     * 构建payment记录
     */
    public Payment buildPayment(PayParam payParam) {
        Payment payment = new Payment();

        HttpServletRequest request = WebServletUtil.getRequest();
        String ip = ServletUtil.getClientIP(request);
        // 基础信息
        payment.setBusinessId(payParam.getBusinessId())
            .setUserId(payParam.getUserId())
            .setTitle(payParam.getTitle())
            .setDescription(payParam.getDescription());

        // 支付方式和状态
        List<PayChannelInfo> payTypeInfos = buildPayTypeInfo(payParam.getPayModeList());
        List<RefundableInfo> refundableInfos = buildRefundableInfo(payParam.getPayModeList());
        // 计算总价
        BigDecimal sumAmount = payTypeInfos.stream()
            .map(PayChannelInfo::getAmount)
            .filter(Objects::nonNull)
            .reduce(BigDecimal::add)
            .orElse(BigDecimal.ZERO);
        // 支付通道信息
        payment.setPayChannelInfo(payTypeInfos)
            .setRefundableInfo(refundableInfos)
            .setPayStatus(PayStatusCode.TRADE_PROGRESS)
            .setAmount(sumAmount)
            .setClientIp(ip)
            .setRefundableBalance(sumAmount);
        return payment;
    }

    /**
     * 构建PayTypeInfo
     */
    private List<PayChannelInfo> buildPayTypeInfo(List<PayModeParam> payModeParamList) {
        return CollectionUtil.isEmpty(payModeParamList) ? Collections.emptyList()
                : payModeParamList.stream().map(PayModeParam::toPayTypeInfo).collect(Collectors.toList());
    }

    /**
     * 构建RefundableInfo
     */
    private List<RefundableInfo> buildRefundableInfo(List<PayModeParam> payModeParamList) {
        return CollectionUtil.isEmpty(payModeParamList) ? Collections.emptyList()
                : payModeParamList.stream().map(PayModeParam::toRefundableInfo).collect(Collectors.toList());
    }

    /**
     * 根据Payment构建PayParam支付参数
     */
    public PayParam buildPayParamByPayment(Payment payment) {
        PayParam payParam = new PayParam();
        // 恢复 payModeList
        List<PayModeParam> payModeParams = payment.getPayChannelInfo()
            .stream()
            .map(payTypeInfo -> new PayModeParam().setAmount(payTypeInfo.getAmount())
                .setPayChannel(payTypeInfo.getPayChannel())
                .setExtraParamsJson(payTypeInfo.getExtraParamsJson()))
            .collect(Collectors.toList());
        payParam.setPayModeList(payModeParams)
            .setBusinessId(payment.getBusinessId())
            .setUserId(payment.getUserId())
            .setTitle(payment.getTitle())
            .setTitle(payment.getTitle())
            .setDescription(payment.getDescription());
        return payParam;
    }

    /**
     * 根据Payment构建PaymentResult
     * @param payment payment
     * @return paymentVO
     */
    public PayResult buildResultByPayment(Payment payment) {
        PayResult paymentResult;
        try {
            paymentResult = new PayResult();
            // 异步支付信息
            paymentResult.setAsyncPayChannel(payment.getAsyncPayChannel())
                .setAsyncPayMode(payment.isAsyncPayMode())
                .setPayStatus(payment.getPayStatus());

            List<PayChannelInfo> channelInfos = payment.getPayChannelInfo();

            // 设置异步支付参数
            List<PayChannelInfo> moneyPayTypeInfos = channelInfos.stream()
                .filter(payTypeInfo -> PayChannelCode.ASYNC_TYPE.contains(payTypeInfo.getPayChannel()))
                .collect(Collectors.toList());
            if (!CollUtil.isEmpty(moneyPayTypeInfos)) {
                paymentResult.setAsyncPayInfo(AsyncPayInfoLocal.get());
            }
            // 清空线程变量
        }
        finally {
            AsyncPayInfoLocal.clear();
        }
        return paymentResult;
    }

}
