package cn.bootx.platform.daxpay.core.channel.wechat.service;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.common.context.AsyncPayLocal;
import cn.bootx.platform.daxpay.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.core.channel.wechat.dao.WeChatPaymentManager;
import cn.bootx.platform.daxpay.core.channel.wechat.entity.WeChatPayment;
import cn.bootx.platform.daxpay.core.order.pay.dao.PayOrderManager;
import cn.bootx.platform.daxpay.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.core.order.pay.entity.PayOrderChannel;
import cn.bootx.platform.daxpay.common.entity.OrderRefundableInfo;
import cn.bootx.platform.daxpay.param.pay.PayWayParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    private final PayOrderManager paymentService;

    private final WeChatPaymentManager weChatPaymentManager;

    /**
     * 支付调起成功 更新payment中异步支付类型信息, 如果支付完成, 创建微信支付单
     */
    public void updatePaySuccess(PayOrder payOrder, PayWayParam payWayParam) {
        AsyncPayLocal asyncPayInfo = PaymentContextLocal.get().getAsyncPayInfo();;
        payOrder.setAsyncPayMode(true).setAsyncPayChannel(PayChannelEnum.WECHAT.getCode());

        List<PayOrderChannel> payTypeInfos = new ArrayList<>();
        List<OrderRefundableInfo> refundableInfos = new ArrayList<>();
        // 清除已有的异步支付类型信息
        payTypeInfos.removeIf(payTypeInfo -> PayChannelEnum.ASYNC_TYPE_CODE.contains(payTypeInfo.getChannel()));
        refundableInfos.removeIf(payTypeInfo -> PayChannelEnum.ASYNC_TYPE_CODE.contains(payTypeInfo.getChannel()));
        // 添加微信支付类型信息
        payTypeInfos.add(new PayOrderChannel().setChannel(PayChannelEnum.WECHAT.getCode())
            .setPayWay(payWayParam.getWay())
            .setAmount(payWayParam.getAmount())
            .setChannelExtra(payWayParam.getChannelExtra()));
//        TODO 更新支付方式列表
        // 更新微信可退款类型信息
        refundableInfos.add(
                new OrderRefundableInfo().setChannel(PayChannelEnum.WECHAT.getCode())
                        .setAmount(payWayParam.getAmount()));
        payOrder.setRefundableInfos(refundableInfos);
        // 如果支付完成(付款码情况) 调用 updateSyncSuccess 创建微信支付记录
        if (Objects.equals(payOrder.getStatus(), PayStatusEnum.SUCCESS.getCode())) {
            this.createWeChatPayment(payOrder, payWayParam, asyncPayInfo.getTradeNo());
        }
    }

    /**
     * 异步支付成功, 更新支付记录成功状态, 并创建微信支付记录
     */
    public void updateAsyncSuccess(Long id, PayWayParam payWayParam, String tradeNo) {
        PayOrder payOrder = paymentService.findById(id).orElseThrow(() -> new BizException("支付记录不存在"));
        this.createWeChatPayment(payOrder, payWayParam, tradeNo);
    }

    /**
     * 并创建微信支付记录
     */
    private void createWeChatPayment(PayOrder payment, PayWayParam payWayParam, String tradeNo) {
        // 创建微信支付记录
        WeChatPayment wechatPayment = new WeChatPayment();
        wechatPayment.setTradeNo(tradeNo)
            .setPaymentId(payment.getId())
            .setAmount(payWayParam.getAmount())
            .setRefundableBalance(payWayParam.getAmount())
            .setBusinessNo(payment.getBusinessNo())
            .setStatus(PayStatusEnum.SUCCESS.getCode())
            .setPayTime(LocalDateTime.now());
        weChatPaymentManager.save(wechatPayment);
    }

    /**
     * 取消状态
     */
    public void updateClose(Long paymentId) {
        Optional<WeChatPayment> weChatPaymentOptional = weChatPaymentManager.findByPaymentId(paymentId);
        weChatPaymentOptional.ifPresent(weChatPayment -> {
            weChatPayment.setStatus(PayStatusEnum.CLOSE.getCode());
            weChatPaymentManager.updateById(weChatPayment);
        });
    }

    /**
     * 更新退款
     */
    public void updatePayRefund(Long paymentId, int amount) {
        Optional<WeChatPayment> weChatPayment = weChatPaymentManager.findByPaymentId(paymentId);
        weChatPayment.ifPresent(payment -> {
            int refundableBalance = payment.getRefundableBalance() - amount;
            payment.setRefundableBalance(refundableBalance);
            if (refundableBalance == 0) {
                payment.setStatus(PayStatusEnum.REFUNDED.getCode());
            }
            else {
                payment.setStatus(PayStatusEnum.PARTIAL_REFUND.getCode());
            }
            weChatPaymentManager.updateById(payment);
        });
    }

}
