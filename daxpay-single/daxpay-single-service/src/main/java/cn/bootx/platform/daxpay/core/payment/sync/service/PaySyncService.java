package cn.bootx.platform.daxpay.core.payment.sync.service;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.code.PaySyncStatusEnum;
import cn.bootx.platform.daxpay.core.order.pay.dao.PayOrderManager;
import cn.bootx.platform.daxpay.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.core.order.sync.service.PaySyncOrderService;
import cn.bootx.platform.daxpay.core.payment.sync.factory.PaySyncStrategyFactory;
import cn.bootx.platform.daxpay.func.AbsPaySyncStrategy;
import cn.bootx.platform.daxpay.core.payment.sync.result.SyncResult;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.func.AbsPayStrategy;
import cn.bootx.platform.daxpay.param.pay.PaySyncParam;
import cn.bootx.platform.daxpay.result.pay.PaySyncResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 支付同步服务
 * @author xxm
 * @since 2023/12/18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaySyncService {
    private final PayOrderManager payOrderManager;

    private final PaySyncOrderService syncOrderService;

    /**
     * 支付同步
     */
    public PaySyncResult sync(PaySyncParam param) {
        PayOrder payOrder = null;
        if (Objects.nonNull(param.getPaymentId())){
            payOrder = payOrderManager.findById(param.getPaymentId())
                    .orElseThrow(() -> new PayFailureException("未查询到支付订单"));
        }
        if (Objects.isNull(payOrder)){
            payOrder = payOrderManager.findByBusinessNo(param.getBusinessNo())
                    .orElseThrow(() -> new PayFailureException("未查询到支付订单"));
        }
        // 如果不是异步支付, 直接返回不需要同步的结果
        if (!payOrder.isAsyncPay()){
            return new PaySyncResult().setSuccess(true).setStatus(PaySyncStatusEnum.NOT_SYNC.getCode());
        }

        // 执行逻辑
        return this.syncPayOrder(payOrder);
    }
    /**
     * 同步支付状态 传入 payment 对象
     */
    public PaySyncResult syncPayOrder(PayOrder order) {
        // 获取同步策略类
        AbsPaySyncStrategy syncPayStrategy = PaySyncStrategyFactory.create(order.getAsyncPayChannel());
        syncPayStrategy.initPayParam(order);
        // 同步支付状态
        SyncResult syncResult = syncPayStrategy.doSyncPayStatusHandler();
        // 根据同步记录对支付单进行处理处理
        this.resultHandler(syncResult,order);

        // 记录同步的结果
        syncOrderService.saveRecord(syncResult,order);
        return null;
    }

    /**
     * 根据同步的结果对支付单进行处理
     */
    public void resultHandler(SyncResult syncResult, PayOrder payment){
        PaySyncStatusEnum syncStatusEnum = PaySyncStatusEnum.getByCode(syncResult.getSyncStatus());
        // 对同步结果处理
        switch (syncStatusEnum) {
            // 支付成功 支付宝退款时也是支付成功状态, 除非支付完成
            case PaySyncStatusEnum.PAY_SUCCESS: {
                this.paymentSuccess(payment, syncPayStrategy, syncResult);
                break;
            }
            // 待付款/ 支付中
            case PaySyncStatusEnum.PAY_WAIT: {
                log.info("依然是付款状态");
                break;
            }
            // 订单已经关闭超时关闭 和 网关没找到记录, 支付宝退款完成也是这个状态
            case PaySyncStatusEnum.CLOSED:
            case PaySyncStatusEnum.NOT_FOUND: {
                // 判断下是否超时, 同时payment 变更为取消支付
                this.paymentCancel(payment, paymentStrategyList);
                break;
            }
            // 交易退款 支付宝没这个状态
            case PaySyncStatusEnum.REFUND: {
                this.paymentRefund(payment, syncPayStrategy, syncResult);
                break;
            }
            // 调用出错
            case PaySyncStatusEnum.FAIL: {
                // 不进行处理
                log.warn("支付状态同步接口调用出错");
                break;
            }
            case PaySyncStatusEnum.NOT_SYNC:
            default: {
                throw new BizException("代码有问题");
            }
        }
    }

    /**
     * payment 变更为已支付
     */
    private void paymentSuccess(PayOrder payment, AbsPayStrategy syncPayStrategy, SyncResult syncResult) {

        // 已支付不在重复处理
        if (Objects.equals(payment.getStatus(), PayStatusEnum.SUCCESS.getCode())) {
            return;
        }
        // 退款的不处理
        if (Objects.equals(payment.getStatus(), PayStatusEnum.PARTIAL_REFUND.getCode())
                || Objects.equals(payment.getStatus(), PayStatusEnum.REFUNDED.getCode())) {
            return;
        }
        // 修改payment支付状态为成功
        syncPayStrategy.doAsyncSuccessHandler(syncResult.getMap());
        payment.setStatus(PayStatusEnum.SUCCESS.getCode());
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
