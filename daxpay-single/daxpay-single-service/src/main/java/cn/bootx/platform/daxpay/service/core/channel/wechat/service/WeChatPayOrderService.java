package cn.bootx.platform.daxpay.service.core.channel.wechat.service;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.service.common.context.AsyncPayLocal;
import cn.bootx.platform.daxpay.service.common.entity.OrderRefundableInfo;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.channel.wechat.dao.WeChatPayOrderManager;
import cn.bootx.platform.daxpay.service.core.channel.wechat.entity.WeChatPayOrder;
import cn.bootx.platform.daxpay.service.core.record.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.record.pay.service.PayOrderChannelService;
import cn.bootx.platform.daxpay.service.core.record.pay.service.PayOrderService;
import cn.bootx.platform.daxpay.param.pay.PayChannelParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 微信支付记录单
 *
 * @author xxm
 * @since 2021/6/21
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatPayOrderService {

    private final PayOrderService payOrderService;

    private final PayOrderChannelService payOrderChannelService;

    private final WeChatPayOrderManager weChatPayOrderManager;

    /**
     * 支付调起成功 更新payment中异步支付类型信息, 如果支付完成, 创建微信支付单
     */
    public void updatePaySuccess(PayOrder payOrder, PayChannelParam payChannelParam) {
        AsyncPayLocal asyncPayInfo = PaymentContextLocal.get().getAsyncPayInfo();;
        payOrder.setAsyncPay(true).setAsyncChannel(PayChannelEnum.WECHAT.getCode());

        payOrderChannelService.updateChannel(payChannelParam,payOrder);

        // 更新微信可退款类型信息
        List<OrderRefundableInfo> refundableInfos = payOrder.getRefundableInfos();
        refundableInfos.removeIf(payTypeInfo -> PayChannelEnum.ASYNC_TYPE_CODE.contains(payTypeInfo.getChannel()));
        refundableInfos.add(new OrderRefundableInfo()
                .setChannel(PayChannelEnum.WECHAT.getCode())
                .setAmount(payChannelParam.getAmount())
        );
        payOrder.setRefundableInfos(refundableInfos);
        // 如果支付完成(付款码情况) 调用 updateSyncSuccess 创建微信支付记录
        if (Objects.equals(payOrder.getStatus(), PayStatusEnum.SUCCESS.getCode())) {
            this.createWeChatOrder(payOrder, payChannelParam.getAmount());
        }
    }

    /**
     * 异步支付成功, 更新支付记录成功状态, 并创建微信支付记录
     */
    public void updateAsyncSuccess(PayOrder payOrder, int amount) {
        this.createWeChatOrder(payOrder, amount);
    }

    /**
     * 并创建微信支付记录
     */
    private void createWeChatOrder(PayOrder payOrder, int amount) {
        String tradeNo = PaymentContextLocal.get()
                .getAsyncPayInfo()
                .getTradeNo();
        // 创建微信支付记录
        WeChatPayOrder wechatPayOrder = new WeChatPayOrder();
        wechatPayOrder.setTradeNo(tradeNo)
                .setPayWay(PaymentContextLocal.get().getAsyncPayInfo().getPayWay().getCode())
                .setPaymentId(payOrder.getId())
                .setAmount(amount)
                .setRefundableBalance(amount)
                .setBusinessNo(payOrder.getBusinessNo())
                .setStatus(PayStatusEnum.SUCCESS.getCode())
                .setPayTime(LocalDateTime.now());
        weChatPayOrderManager.save(wechatPayOrder);
    }

    /**
     * 取消状态
     */
    public void updateClose(Long paymentId) {
        Optional<WeChatPayOrder> weChatPaymentOptional = weChatPayOrderManager.findByPaymentId(paymentId);
        weChatPaymentOptional.ifPresent(weChatPayment -> {
            weChatPayment.setStatus(PayStatusEnum.CLOSE.getCode());
            weChatPayOrderManager.updateById(weChatPayment);
        });
    }

    /**
     * 更新退款
     */
    public void updateRefund(Long paymentId, int amount) {
        Optional<WeChatPayOrder> weChatPayment = weChatPayOrderManager.findByPaymentId(paymentId);
        weChatPayment.ifPresent(payment -> {
            int refundableBalance = payment.getRefundableBalance() - amount;
            payment.setRefundableBalance(refundableBalance);
            if (refundableBalance == 0) {
                payment.setStatus(PayStatusEnum.REFUNDED.getCode());
            }
            else {
                payment.setStatus(PayStatusEnum.PARTIAL_REFUND.getCode());
            }
            weChatPayOrderManager.updateById(payment);
        });
    }

}
