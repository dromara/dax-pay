package cn.bootx.platform.daxpay.core.order.pay.builder;

import cn.bootx.platform.common.spring.util.WebServletUtil;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.core.order.pay.entity.PayOrderChannel;
import cn.bootx.platform.daxpay.core.order.pay.entity.PayOrderRefundableInfo;
import cn.bootx.platform.daxpay.param.pay.PayParam;
import cn.bootx.platform.daxpay.param.pay.PayWayParam;
import cn.bootx.platform.daxpay.result.pay.PayResult;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.extra.servlet.ServletUtil;
import lombok.experimental.UtilityClass;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
        PayOrder payOrder = new PayOrder();

        HttpServletRequest request = WebServletUtil.getRequest();
        String ip = ServletUtil.getClientIP(request);
        // 基础信息
        payOrder.setBusinessNo(payParam.getBusinessNo())
                .setTitle(payParam.getTitle());

        // 支付方式
        List<PayOrderRefundableInfo> refundableInfos = buildRefundableInfo(payParam.getPayWays());
        List<PayOrderChannel> payOrderChannels = buildPayChannel(payParam.getPayWays());
        // 计算总价
        int sumAmount = payOrderChannels.stream()
                .map(PayOrderChannel::getAmount)
                .filter(Objects::nonNull)
                .reduce(Integer::sum)
                .orElse(0);
        // 支付渠道信息
        payOrder.setRefundableInfos(refundableInfos)
                .setStatus(PayStatusEnum.PROGRESS.getCode())
                .setAmount(sumAmount)
                .setCombinationPayMode(payParam.getPayWays().size()>1)
                .setRefundableBalance(sumAmount);
        return payOrder;
    }

    /**
     * 构建订单关联通道信息
     */
    private List<PayOrderChannel> buildPayChannel(List<PayWayParam> payWayParamList) {
        if (CollectionUtil.isEmpty(payWayParamList)) {
            return Collections.emptyList();
        }
        return payWayParamList.stream()
                .map(o-> new PayOrderChannel()
                       .setChannel(o.getPayChannel())
                       .setPayWay(o.getPayWay())
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
                        .setChannel(o.getPayChannel())
                        .setAmount(o.getAmount()))
                .collect(Collectors.toList());
    }


    /**
     * 根据Payment构建PaymentResult
     * @param payment payment
     * @return paymentVO
     */
    public PayResult buildResultByPayment(PayOrder payment) {
//        PayResult paymentResult;
//        try {
//            paymentResult = new PayResult();
//            // 异步支付信息
//            paymentResult.setAsyncPayChannel(payment.getAsyncPayChannel())
//                    .setAsyncPayMode(payment.isAsyncPayMode())
//                    .setStatus(payment.getStatus());
//
//
//            // 设置异步支付参数
//            List<PayOrderChannel> moneyPayTypeInfos = channelInfos.stream()
//                    .filter(payTypeInfo -> PayChannelEnum.ASYNC_TYPE_CODE.contains(payTypeInfo.getPayChannel()))
//                    .collect(Collectors.toList());
//            if (!CollUtil.isEmpty(moneyPayTypeInfos)) {
//                paymentResult.setAsyncPayInfo(AsyncPayInfoLocal.get());
//            }
//        }
//        finally {
//            // 清空线程变量
//            AsyncPayInfoLocal.clear();
//        }
//        return paymentResult;
        return null;
    }

}
