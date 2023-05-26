package cn.bootx.platform.daxpay.core.pay.service;

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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static cn.bootx.platform.daxpay.code.pay.PayStatusCode.*;

/**
 * 支付超时处理
 *
 * @author xxm
 * @date 2022/7/13
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayExpiredTimeService {

    private final PaymentService paymentService;

    private final PaymentEventSender eventSender;

    /**
     * 支付单超时支付单处理
     */
    @Async("bigExecutor")
    @Transactional(rollbackFor = Exception.class)
    public void expiredTime(Long paymentId) {

        Payment payment = paymentService.findById(paymentId).orElseThrow(() -> new PayFailureException("支付单未找到"));
        // 只处理支付中
        if (!Objects.equals(payment.getPayStatus(), TRADE_PROGRESS)) {
            return;
        }
        // 获取支付网关状态
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

        // 4 对返回的支付网关各种状态进行处理
        int paySyncStatus = paySyncResult.getPaySyncStatus();
        switch (paySyncStatus) {
            // 成功状态
            case PaySyncStatus.TRADE_SUCCESS: {
                this.paySuccess(payment, syncPayStrategy, paySyncResult);
                break;
            }
            // 待付款/ 支付中
            case PaySyncStatus.WAIT_BUYER_PAY: {
                this.payCancel(payment, paymentStrategyList);
                break;
            }
            // 超时关闭 和 网关没找到记录
            case PaySyncStatus.TRADE_CLOSED:
            case PaySyncStatus.NOT_FOUND: {
                this.payClose(payment, paymentStrategyList);
                break;
            }
            // 交易退款
            case PaySyncStatus.TRADE_REFUND: {
                log.info("交易退款不需要关闭: {}", payment.getId());
                break;
            }
            // 调用出错 进行重试
            case PaySyncStatus.FAIL: {
                log.warn("支付状态同步接口调用出错");
            }
            case PaySyncStatus.NOT_SYNC:
            default: {
                log.error("支付超时代码有问题");
            }
        }
    }

    /**
     * 如果支付网关是支付中状态, 关闭网关支付, 然后再关闭本地支付单, 排除退款
     */
    private void payCancel(Payment payment, List<AbsPayStrategy> payStrategies) {
        try {
            // 异常情况, 不继续进行处理
            if (!this.check(payment)) {
                return;
            }
            // 撤销和关闭支付单
            payStrategies.forEach(AbsPayStrategy::doCancelHandler);
            payment.setPayStatus(TRADE_CANCEL);
            paymentService.updateById(payment);
            // 发送事件
            eventSender.sendPayCancel(PayEventBuilder.buildPayCancel(payment));
        }
        catch (Exception e) {
            log.warn("支付状态同步后关闭支付单报错了", e);
        }
    }

    /**
     * 如果支付网关是关闭或未找到, 关闭本地支付单
     */
    private void payClose(Payment payment, List<AbsPayStrategy> absPayStrategies) {
        try {
            // 已关闭的不再进行关闭
            if (Objects.equals(payment.getPayStatus(), TRADE_CANCEL)) {
                return;
            }
            // 退款状态则不进行更新
            if (Objects.equals(payment.getPayStatus(), TRADE_REFUNDED)
                    || Objects.equals(payment.getPayStatus(), TRADE_REFUNDING)) {
                return;
            }
            // 异常情况, 不继续进行处理
            if (!this.check(payment)) {
                return;
            }
            // 关闭支付单
            absPayStrategies.forEach(AbsPayStrategy::doCloseHandler);
            payment.setPayStatus(TRADE_CANCEL);
            paymentService.updateById(payment);
            // 发送事件
            eventSender.sendPayCancel(PayEventBuilder.buildPayCancel(payment));
        }
        catch (Exception e) {
            log.warn("支付状态同步后关闭支付单报错了", e);
        }
    }

    /**
     * 如果是支付网关是支付完成状态, 记录为异常支付单
     */
    private void paySuccess(Payment payment, AbsPayStrategy syncPayStrategy, PaySyncResult paySyncResult) {
        // 修改payment支付状态为成功
        log.error("支付网关支付单已被支付, 需要线下处理: {}", payment.getId());
    }

    /**
     * 校验状态, 处理在发起撤销与正式处理之间订单被完成的情况. 理论上不会发生
     */
    private boolean check(Payment payment) {
        // 支付失败/撤销/退款不需要处理
        List<Integer> trades = Arrays.asList(TRADE_FAIL, TRADE_CANCEL, TRADE_REFUNDING, TRADE_REFUNDED);
        if (trades.contains(payment.getPayStatus())) {
            log.info("订单在超时撤销期间发生了操作, 需要人工介入处理");
            return false;
        }
        return true;
    }

}
