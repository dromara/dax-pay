package cn.bootx.platform.daxpay.core.pay.service;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.daxpay.code.pay.PaySyncStatus;
import cn.bootx.platform.daxpay.core.pay.builder.PayEventBuilder;
import cn.bootx.platform.daxpay.core.pay.builder.PaymentBuilder;
import cn.bootx.platform.daxpay.core.pay.factory.PayStrategyFactory;
import cn.bootx.platform.daxpay.core.pay.func.AbsPayStrategy;
import cn.bootx.platform.daxpay.core.pay.result.PaySyncResult;
import cn.bootx.platform.daxpay.core.payment.entity.Payment;
import cn.bootx.platform.daxpay.core.payment.service.PaymentService;
import cn.bootx.platform.daxpay.exception.payment.PayFailureException;
import cn.bootx.platform.daxpay.exception.payment.PayUnsupportedMethodException;
import cn.bootx.platform.daxpay.mq.PaymentEventSender;
import cn.bootx.platform.daxpay.param.pay.PayWayParam;
import cn.bootx.platform.daxpay.param.pay.PayParam;
import cn.bootx.platform.daxpay.util.PayWaylUtil;
import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static cn.bootx.platform.daxpay.code.pay.PayStatusCode.*;

/**
 * 未完成的异步支付单与支付网关进行对比
 *
 * @author xxm
 * @date 2021/4/21
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaySyncService {

    private final PaymentService paymentService;

    private final PaymentEventSender eventSender;

    /**
     * 同步订单的支付状态
     */
    public void syncByPaymentId(Long id) {
        Payment payment = paymentService.findById(id).orElse(null);
        if (Objects.isNull(payment)) {
            return;
        }
        this.syncPayment(payment);
    }

    /**
     * 同步订单的支付状态
     */
    public void syncByBusinessId(String businessId) {
        Payment payment = paymentService.findByBusinessId(businessId).orElse(null);
        if (Objects.isNull(payment)) {
            return;
        }
        this.syncPayment(payment);
    }

    /**
     * 同步支付状态 传入 payment 对象
     */
    public void syncPayment(Payment payment) {
        PayParam payParam = PaymentBuilder.buildPayParamByPayment(payment);
        // 1.获取支付方式，通过工厂生成对应的策略组
        List<AbsPayStrategy> paymentStrategyList = PayStrategyFactory.create(payParam.getPayWayList());
        if (CollUtil.isEmpty(paymentStrategyList)) {
            throw new PayUnsupportedMethodException();
        }

        // 2.初始化支付的参数
        for (AbsPayStrategy paymentStrategy : paymentStrategyList) {
            paymentStrategy.initPayParam(payment, payParam);
        }

        // 3 拿到异步支付方法, 与支付网关进行同步
        PayWayParam asyncPayMode = PayWaylUtil.getAsyncPayModeParam(payParam);
        AbsPayStrategy syncPayStrategy = PayStrategyFactory.create(asyncPayMode);
        syncPayStrategy.initPayParam(payment, payParam);
        PaySyncResult paySyncResult = syncPayStrategy.doSyncPayStatusHandler();
        String paySyncStatus = paySyncResult.getPaySyncStatus();

        switch (paySyncStatus) {
            // 支付成功 支付宝退款时也是支付成功状态, 除非支付完成
            case PaySyncStatus.TRADE_SUCCESS: {
                this.paymentSuccess(payment, syncPayStrategy, paySyncResult);
                break;
            }
            // 待付款/ 支付中
            case PaySyncStatus.WAIT_BUYER_PAY: {
                log.info("依然是付款状态");
                break;
            }
            // 订单已经关闭超时关闭 和 网关没找到记录, 支付宝退款完成也是这个状态
            case PaySyncStatus.TRADE_CLOSED:
            case PaySyncStatus.NOT_FOUND: {
                // 判断下是否超时, 同时payment 变更为取消支付
                this.paymentCancel(payment, paymentStrategyList);
                break;
            }
            // 交易退款 支付宝没这个状态
            case PaySyncStatus.TRADE_REFUND: {
                this.paymentRefund(payment, syncPayStrategy, paySyncResult);
                break;
            }
            // 调用出错
            case PaySyncStatus.FAIL: {
                // 不进行处理
                log.warn("支付状态同步接口调用出错");
                break;
            }
            case PaySyncStatus.NOT_SYNC:
            default: {
                throw new BizException("代码有问题");
            }
        }
    }

    /**
     * payment 变更为已支付
     */
    private void paymentSuccess(Payment payment, AbsPayStrategy syncPayStrategy, PaySyncResult paySyncResult) {

        // 已支付不在重复处理
        if (Objects.equals(payment.getPayStatus(), TRADE_SUCCESS)) {
            return;
        }
        // 退款的不处理
        if (Objects.equals(payment.getPayStatus(), TRADE_REFUNDED)
                || Objects.equals(payment.getPayStatus(), TRADE_REFUNDING)) {
            return;
        }
        // 修改payment支付状态为成功
        syncPayStrategy.doAsyncSuccessHandler(paySyncResult.getMap());
        payment.setPayStatus(TRADE_SUCCESS);
        payment.setPayTime(LocalDateTime.now());
        paymentService.updateById(payment);

        // 发送成功事件
        eventSender.sendPayComplete(PayEventBuilder.buildPayComplete(payment));
    }

    /**
     * payment 变更为取消支付
     */
    private void paymentCancel(Payment payment, List<AbsPayStrategy> absPayStrategies) {
        try {
            // 已关闭的不再进行关闭
            if (Objects.equals(payment.getPayStatus(), TRADE_CANCEL)) {
                return;
            }
            // 修改payment支付状态为取消, 退款状态则不进行更新
            if (Objects.equals(payment.getPayStatus(), TRADE_REFUNDED)
                    || Objects.equals(payment.getPayStatus(), TRADE_REFUNDING)) {
                return;
            }
            payment.setPayStatus(TRADE_CANCEL);
            // 执行策略的关闭方法
            absPayStrategies.forEach(AbsPayStrategy::doCloseHandler);
            paymentService.updateById(payment);
            // 发送事件
            eventSender.sendPayCancel(PayEventBuilder.buildPayCancel(payment));
        }
        catch (Exception e) {
            log.warn("支付状态同步后关闭支付单报错了", e);
            throw new PayFailureException("支付状态同步后关闭支付单报错了");
        }
    }

    /**
     * payment 退款处理 TODO 需要考虑退款详情的合并处理
     */
    private void paymentRefund(Payment payment, AbsPayStrategy syncPayStrategy, PaySyncResult paySyncResult) {

    }

}
