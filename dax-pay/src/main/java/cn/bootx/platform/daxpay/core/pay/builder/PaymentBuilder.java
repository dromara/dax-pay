package cn.bootx.platform.daxpay.core.pay.builder;

import cn.bootx.platform.common.spring.util.WebServletUtil;
import cn.bootx.platform.daxpay.code.pay.PayChannelEnum;
import cn.bootx.platform.daxpay.code.pay.PayStatusCode;
import cn.bootx.platform.daxpay.core.pay.local.AsyncPayInfoLocal;
import cn.bootx.platform.daxpay.core.payment.entity.Payment;
import cn.bootx.platform.daxpay.dto.pay.PayResult;
import cn.bootx.platform.daxpay.dto.payment.PayChannelInfo;
import cn.bootx.platform.daxpay.dto.payment.RefundableInfo;
import cn.bootx.platform.daxpay.param.pay.PayWayParam;
import cn.bootx.platform.daxpay.param.pay.PayParam;
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
                .setMchCode(payParam.getMchCode())
                .setMchAppCode(payParam.getMchAppCode())
                .setTitle(payParam.getTitle())
                .setDescription(payParam.getDescription());

        // 支付方式和状态
        List<PayChannelInfo> payTypeInfos = buildPayTypeInfo(payParam.getPayWayList());
        List<RefundableInfo> refundableInfos = buildRefundableInfo(payParam.getPayWayList());
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
                .setCombinationPayMode(payTypeInfos.size()>1)
                .setClientIp(ip)
                .setRefundableBalance(sumAmount);
        return payment;
    }

    /**
     * 构建PayTypeInfo
     */
    private List<PayChannelInfo> buildPayTypeInfo(List<PayWayParam> payWayParamList) {
        return CollectionUtil.isEmpty(payWayParamList) ? Collections.emptyList()
                : payWayParamList.stream().map(PayWayParam::toPayTypeInfo).collect(Collectors.toList());
    }

    /**
     * 构建RefundableInfo
     */
    private List<RefundableInfo> buildRefundableInfo(List<PayWayParam> payWayParamList) {
        return CollectionUtil.isEmpty(payWayParamList) ? Collections.emptyList()
                : payWayParamList.stream().map(PayWayParam::toRefundableInfo).collect(Collectors.toList());
    }

    /**
     * 根据Payment构建PayParam支付参数
     */
    public PayParam buildPayParamByPayment(Payment payment) {
        PayParam payParam = new PayParam();
        // 恢复 payModeList
        List<PayWayParam> payWayParams = payment.getPayChannelInfo()
                .stream()
                .map(payTypeInfo -> new PayWayParam().setAmount(payTypeInfo.getAmount())
                        .setPayChannel(payTypeInfo.getPayChannel())
                        .setExtraParamsJson(payTypeInfo.getExtraParamsJson()))
                .collect(Collectors.toList());
        payParam.setPayWayList(payWayParams)
                .setBusinessId(payment.getBusinessId())
                .setTitle(payment.getTitle())
                .setMchCode(payment.getMchCode())
                .setMchAppCode(payment.getMchAppCode())
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
                    .filter(payTypeInfo -> PayChannelEnum.ASYNC_TYPE_CODE.contains(payTypeInfo.getPayChannel()))
                    .collect(Collectors.toList());
            if (!CollUtil.isEmpty(moneyPayTypeInfos)) {
                paymentResult.setAsyncPayInfo(AsyncPayInfoLocal.get());
            }
        }
        finally {
            // 清空线程变量
            AsyncPayInfoLocal.clear();
        }
        return paymentResult;
    }

}
