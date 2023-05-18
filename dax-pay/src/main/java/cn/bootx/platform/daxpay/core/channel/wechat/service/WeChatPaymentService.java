package cn.bootx.platform.daxpay.core.channel.wechat.service;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.core.util.BigDecimalUtil;
import cn.bootx.platform.daxpay.code.pay.PayChannelCode;
import cn.bootx.platform.daxpay.code.pay.PayStatusCode;
import cn.bootx.platform.daxpay.core.pay.local.AsyncPayInfoLocal;
import cn.bootx.platform.daxpay.core.payment.entity.Payment;
import cn.bootx.platform.daxpay.core.payment.service.PaymentService;
import cn.bootx.platform.daxpay.core.channel.wechat.dao.WeChatPaymentManager;
import cn.bootx.platform.daxpay.core.channel.wechat.entity.WeChatPayment;
import cn.bootx.platform.daxpay.dto.pay.AsyncPayInfo;
import cn.bootx.platform.daxpay.dto.payment.PayChannelInfo;
import cn.bootx.platform.daxpay.dto.payment.RefundableInfo;
import cn.bootx.platform.daxpay.param.pay.PayModeParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 微信支付记录单
 *
 * @author xxm
 * @date 2021/6/21
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatPaymentService {

    private final PaymentService paymentService;

    private final WeChatPaymentManager weChatPaymentManager;

    /**
     * 支付调起成功 更新 payment 中 异步支付类型信息
     */
    public void updatePaySuccess(Payment payment, PayModeParam payModeParam) {
        AsyncPayInfo asyncPayInfo = AsyncPayInfoLocal.get();
        payment.setAsyncPayMode(true).setAsyncPayChannel(PayChannelCode.WECHAT);

        List<PayChannelInfo> payTypeInfos = payment.getPayChannelInfo();
        List<RefundableInfo> refundableInfos = payment.getRefundableInfo();
        // 清除已有的异步支付类型信息
        payTypeInfos.removeIf(payTypeInfo -> PayChannelCode.ASYNC_TYPE.contains(payTypeInfo.getPayChannel()));
        refundableInfos.removeIf(payTypeInfo -> PayChannelCode.ASYNC_TYPE.contains(payTypeInfo.getPayChannel()));
        // 添加微信支付类型信息
        payTypeInfos.add(new PayChannelInfo().setPayChannel(PayChannelCode.WECHAT)
            .setPayWay(payModeParam.getPayWay())
            .setAmount(payModeParam.getAmount())
            .setExtraParamsJson(payModeParam.getExtraParamsJson()));
        payment.setPayChannelInfo(payTypeInfos);
        // 更新微信可退款类型信息
        refundableInfos
            .add(new RefundableInfo().setPayChannel(PayChannelCode.WECHAT).setAmount(payModeParam.getAmount()));
        payment.setRefundableInfo(refundableInfos);
        // 如果支付完成(付款码情况) 调用 updateSyncSuccess 创建微信支付记录
        if (Objects.equals(payment.getPayStatus(), PayStatusCode.TRADE_SUCCESS)) {
            this.createWeChatPayment(payment, payModeParam, asyncPayInfo.getTradeNo());
        }
    }

    /**
     * 异步支付成功, 更新支付记录成功状态, 并创建微信支付记录
     */
    public void updateAsyncSuccess(Long id, PayModeParam payModeParam, String tradeNo) {
        Payment payment = paymentService.findById(id).orElseThrow(() -> new BizException("支付记录不存在"));
        this.createWeChatPayment(payment, payModeParam, tradeNo);
    }

    /**
     * 更新支付记录成功状态, 并创建微信支付记录
     */
    private void createWeChatPayment(Payment payment, PayModeParam payModeParam, String tradeNo) {

        // 创建微信支付记录
        WeChatPayment wechatPayment = new WeChatPayment();
        wechatPayment.setTradeNo(tradeNo)
            .setPaymentId(payment.getId())
            .setAmount(payModeParam.getAmount())
            .setRefundableBalance(payModeParam.getAmount())
            .setBusinessId(payment.getBusinessId())
            .setUserId(payment.getUserId())
            .setPayStatus(PayStatusCode.TRADE_SUCCESS)
            .setPayTime(LocalDateTime.now());
        weChatPaymentManager.save(wechatPayment);
    }

    /**
     * 取消状态
     */
    public void updateClose(Long paymentId) {
        Optional<WeChatPayment> weChatPaymentOptional = weChatPaymentManager.findByPaymentId(paymentId);
        weChatPaymentOptional.ifPresent(weChatPayment -> {
            weChatPayment.setPayStatus(PayStatusCode.TRADE_CANCEL);
            weChatPaymentManager.updateById(weChatPayment);
        });
    }

    /**
     * 更新退款
     */
    public void updatePayRefund(WeChatPayment weChatPayment, BigDecimal amount) {
        BigDecimal refundableBalance = weChatPayment.getRefundableBalance().subtract(amount);
        weChatPayment.setRefundableBalance(refundableBalance);
        if (BigDecimalUtil.compareTo(refundableBalance, BigDecimal.ZERO) == 0) {
            weChatPayment.setPayStatus(PayStatusCode.TRADE_REFUNDED);
        }
        else {
            weChatPayment.setPayStatus(PayStatusCode.TRADE_REFUNDING);
        }
        weChatPaymentManager.updateById(weChatPayment);
    }

}
