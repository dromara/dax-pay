package cn.bootx.platform.daxpay.core.channel.alipay.service;

import cn.bootx.platform.common.core.util.BigDecimalUtil;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.core.channel.alipay.dao.AliPayOrderManager;
import cn.bootx.platform.daxpay.core.channel.alipay.entity.AliPayOrder;
import cn.bootx.platform.daxpay.core.order.pay.dao.PayOrderManager;
import cn.bootx.platform.daxpay.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.core.order.pay.entity.PayOrderChannel;
import cn.bootx.platform.daxpay.core.order.pay.entity.PayRefundableInfo;
import cn.bootx.platform.daxpay.core.payment.pay.local.AsyncPayInfo;
import cn.bootx.platform.daxpay.core.payment.pay.local.AsyncPayInfoLocal;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.param.pay.PayWayParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 支付宝支付订单
 * 1.创建: 支付调起并支付成功后才会创建
 * 2.撤销: 关闭本地支付记录
 * 3.退款: 发起退款时记录
 *
 * @author xxm
 * @since 2021/2/26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AliPayOrderService {

    private final AliPayOrderManager aliPayOrderManager;

    private final PayOrderManager payOrderManager;

    /**
     * 支付调起成功 更新payment中异步支付类型信息, 如果支付完成, 创建支付宝支付单
     */
    public void updatePaySuccess(PayOrder payOrder, PayWayParam payWayParam) {
        AsyncPayInfo asyncPayInfo = AsyncPayInfoLocal.get();
        payOrder.setAsyncPayMode(true).setAsyncPayChannel(PayChannelEnum.ALI.getCode());

        // TODO 支付
        List<PayOrderChannel> payChannelInfo = new ArrayList<>();
        List<PayRefundableInfo> refundableInfos = new ArrayList<>();
        // 清除已有的异步支付类型信息
        payChannelInfo.removeIf(payTypeInfo -> PayChannelEnum.ASYNC_TYPE_CODE.contains(payTypeInfo.getPayChannel()));
        refundableInfos.removeIf(payTypeInfo -> PayChannelEnum.ASYNC_TYPE_CODE.contains(payTypeInfo.getPayChannel()));
        // 更新支付宝支付类型信息
        payChannelInfo.add(new PayChannelInfo().setPayChannel(PayChannelEnum.ALI.getCode())
            .setPayWay(payWayParam.getPayWay())
            .setAmount(payWayParam.getAmount())
            .setExtraParamsJson(payWayParam.getExtraParamsJson()));
        payOrder.setPayChannelInfo(payChannelInfo);
        // 更新支付宝可退款类型信息
        refundableInfos
            .add(new RefundableInfo().setPayChannel(PayChannelEnum.ALI.getCode()).setAmount(payWayParam.getAmount()));
        payOrder.setRefundableInfo(refundableInfos);
        // 如果支付完成(付款码情况) 调用 updateSyncSuccess 创建支付宝支付记录
        if (Objects.equals(payOrder.getPayStatus(), PayStatusCode.TRADE_SUCCESS)) {
            this.createAliPayment(payOrder, payWayParam, asyncPayInfo.getTradeNo());
        }
    }

    /**
     * 更新异步支付记录成功状态, 并创建支付宝支付记录
     */
    public void updateAsyncSuccess(Long id, PayWayParam payWayParam, String tradeNo) {
        // 更新支付记录
        Payment payment = payOrderManager.findById(id).orElseThrow(() -> new PayFailureException("支付记录不存在"));
        this.createAliPayment(payment,payWayParam,tradeNo);
    }

    /**
     * 创建支付宝支付记录(支付调起成功后才会创建)
     */
    private void createAliPayment(Payment payment, PayWayParam payWayParam, String tradeNo) {
        // 创建支付宝支付记录
        AliPayOrder aliPayOrder = new AliPayOrder();
        aliPayOrder.setTradeNo(tradeNo)
                .setPaymentId(payment.getId())
                .setAmount(payWayParam.getAmount())
                .setRefundableBalance(payWayParam.getAmount())
                .setBusinessNo(payment.getBusinessId())
                .setStatus(PayStatusCode.TRADE_SUCCESS)
                .setPayTime(LocalDateTime.now());
        aliPayOrderManager.save(aliPayOrder);
    }

    /**
     * 取消状态
     */
    public void updateClose(Long paymentId) {
        Optional<AliPayOrder> aliPaymentOptional = aliPayOrderManager.findByPaymentId(paymentId);
        aliPaymentOptional.ifPresent(aliPayOrder -> {
            aliPayOrder.setStatus(PayStatusCode.TRADE_CANCEL);
            aliPayOrderManager.updateById(aliPayOrder);
        });
    }

    /**
     * 更新退款
     */
    public void updatePayRefund(Long paymentId, BigDecimal amount) {
        Optional<AliPayOrder> aliPaymentOptional = aliPayOrderManager.findByPaymentId(paymentId);
        aliPaymentOptional.ifPresent(payment -> {
            BigDecimal refundableBalance = payment.getRefundableBalance().subtract(amount);
            payment.setRefundableBalance(refundableBalance);
            if (BigDecimalUtil.compareTo(refundableBalance, BigDecimal.ZERO) == 0) {
                payment.setStatus(PayStatusCode.TRADE_REFUNDED);
            }
            else {
                payment.setStatus(PayStatusCode.TRADE_REFUNDING);
            }
            aliPayOrderManager.updateById(payment);
        });
    }
}
