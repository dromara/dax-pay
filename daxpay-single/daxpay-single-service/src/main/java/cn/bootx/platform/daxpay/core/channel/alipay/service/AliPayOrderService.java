package cn.bootx.platform.daxpay.core.channel.alipay.service;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.common.context.AsyncPayLocal;
import cn.bootx.platform.daxpay.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.core.channel.alipay.dao.AliPayOrderManager;
import cn.bootx.platform.daxpay.core.channel.alipay.entity.AliPayOrder;
import cn.bootx.platform.daxpay.core.order.pay.dao.PayOrderChannelManager;
import cn.bootx.platform.daxpay.core.order.pay.dao.PayOrderManager;
import cn.bootx.platform.daxpay.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.core.order.pay.entity.PayOrderChannel;
import cn.bootx.platform.daxpay.common.entity.OrderRefundableInfo;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.param.pay.PayWayParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    private final PayOrderChannelManager payOrderChannelManager;

    /**
     * 支付调起成功 更新payment中异步支付类型信息, 如果支付完成, 创建支付宝支付单
     */
    public void updatePaySuccess(PayOrder payOrder, PayWayParam payWayParam) {
        AsyncPayLocal asyncPayInfo = PaymentContextLocal.get().getAsyncPayInfo();
        payOrder.setAsyncPayMode(true)
                .setAsyncPayChannel(PayChannelEnum.ALI.getCode());

        // 更新支付宝异步支付类型信息
        Optional<PayOrderChannel> payOrderChannelOpt = payOrderChannelManager.findByPaymentIdAndChannel(payOrder.getId(), PayChannelEnum.ALI.getCode());
        if (!payOrderChannelOpt.isPresent()){
            payOrderChannelManager.deleteByPaymentIdAndAsync(payOrder.getId());
            payOrderChannelManager.save(new PayOrderChannel()
                   .setPaymentId(payOrder.getId())
                   .setChannel(PayChannelEnum.ALI.getCode())
                   .setAmount(payWayParam.getAmount())
                    .setPayWay(payWayParam.getWay())
                    .setChannelExtra(payWayParam.getChannelExtra())
                    .setAsync(true)
            );
        } else {
            payOrderChannelOpt.get()
                   .setChannelExtra(payWayParam.getChannelExtra())
                   .setPayWay(payWayParam.getWay());
            payOrderChannelManager.updateById(payOrderChannelOpt.get());
        }


        // 更新支付宝可退款类型信息
        List<OrderRefundableInfo> refundableInfos = payOrder.getRefundableInfos();
        refundableInfos.removeIf(payTypeInfo -> PayChannelEnum.ASYNC_TYPE_CODE.contains(payTypeInfo.getChannel()));
        refundableInfos.add(new OrderRefundableInfo()
                .setChannel(PayChannelEnum.ALI.getCode())
                .setAmount(payWayParam.getAmount())
        );
        payOrder.setRefundableInfos(refundableInfos);
        // 如果支付完成(付款码情况) 调用 updateSyncSuccess 创建支付宝支付记录
        if (Objects.equals(payOrder.getStatus(), PayStatusEnum.SUCCESS.getCode())) {
            this.createAliPayment(payOrder, payWayParam, asyncPayInfo.getTradeNo());
        }
    }

    /**
     * 更新异步支付记录成功状态, 并创建支付宝支付记录
     */
    public void updateAsyncSuccess(Long id, PayWayParam payWayParam, String tradeNo) {
        // 更新支付记录
        PayOrder payOrder = payOrderManager.findById(id).orElseThrow(() -> new PayFailureException("支付记录不存在"));
        this.createAliPayment(payOrder,payWayParam,tradeNo);
    }

    /**
     * 创建支付宝支付记录(支付调起成功后才会创建)
     */
    private void createAliPayment(PayOrder payOrder, PayWayParam payWayParam, String tradeNo) {
        // 创建支付宝支付记录
        AliPayOrder aliPayOrder = new AliPayOrder();
        aliPayOrder.setTradeNo(tradeNo)
                .setPaymentId(payOrder.getId())
                .setAmount(payWayParam.getAmount())
                .setRefundableBalance(payWayParam.getAmount())
                .setBusinessNo(payOrder.getBusinessNo())
                .setStatus(PayStatusEnum.SUCCESS.getCode())
                .setPayTime(LocalDateTime.now());
        aliPayOrderManager.save(aliPayOrder);
    }

    /**
     * 取消状态
     */
    public void updateClose(Long paymentId) {
        Optional<AliPayOrder> aliPaymentOptional = aliPayOrderManager.findByPaymentId(paymentId);
        aliPaymentOptional.ifPresent(aliPayOrder -> {
            aliPayOrder.setStatus(PayStatusEnum.CLOSE.getCode());
            aliPayOrderManager.updateById(aliPayOrder);
        });
    }

    /**
     * 更新退款
     */
    public void updatePayRefund(Long paymentId, int amount) {
        Optional<AliPayOrder> aliPaymentOptional = aliPayOrderManager.findByPaymentId(paymentId);
        aliPaymentOptional.ifPresent(payment -> {
            int refundableBalance = payment.getRefundableBalance() - amount;
            payment.setRefundableBalance(refundableBalance);
            // 退款完毕
            if (refundableBalance == 0) {
                payment.setStatus(PayStatusEnum.REFUNDED.getCode());
            }
            // 部分退款
            else {
                payment.setStatus(PayStatusEnum.PARTIAL_REFUND.getCode());
            }
            aliPayOrderManager.updateById(payment);
        });
    }
}
