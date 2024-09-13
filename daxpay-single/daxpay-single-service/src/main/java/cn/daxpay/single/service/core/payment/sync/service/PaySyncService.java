package cn.daxpay.single.service.core.payment.sync.service;

import cn.bootx.platform.common.core.exception.RepetitiveOperationException;
import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.daxpay.single.core.code.PayChannelEnum;
import cn.daxpay.single.core.code.PayOrderRefundStatusEnum;
import cn.daxpay.single.core.code.PayStatusEnum;
import cn.daxpay.single.core.code.PaySyncStatusEnum;
import cn.daxpay.single.core.exception.*;
import cn.daxpay.single.core.param.payment.pay.PaySyncParam;
import cn.daxpay.single.core.result.sync.PaySyncResult;
import cn.daxpay.single.service.code.TradeTypeEnum;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.order.pay.entity.PayOrder;
import cn.daxpay.single.service.core.order.pay.service.PayOrderQueryService;
import cn.daxpay.single.service.core.order.pay.service.PayOrderService;
import cn.daxpay.single.service.core.payment.sync.result.PayRemoteSyncResult;
import cn.daxpay.single.service.core.record.sync.entity.TradeSyncRecord;
import cn.daxpay.single.service.core.record.sync.service.TradeSyncRecordService;
import cn.daxpay.single.service.func.AbsPayCloseStrategy;
import cn.daxpay.single.service.func.AbsPaySyncStrategy;
import cn.daxpay.single.service.util.PayStrategyFactory;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static cn.daxpay.single.core.code.PaySyncStatusEnum.*;

/**
 * 支付同步服务
 * @author xxm
 * @since 2023/12/18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaySyncService {
    private final PayOrderQueryService payOrderQueryService;

    private final PayOrderService payOrderService;

    private final TradeSyncRecordService tradeSyncRecordService;

    private final LockTemplate lockTemplate;

    /**
     * 支付同步, 开启一个新的事务, 不受外部抛出异常的影响
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public PaySyncResult sync(PaySyncParam param) {
        PayOrder payOrder = payOrderQueryService.findByBizOrOrderNo(param.getOrderNo(), param.getBizOrderNo())
                .orElseThrow(() -> new TradeNotExistException("支付订单不存在"));

        // 钱包支付钱包不需要同步
        if (PayChannelEnum.WALLET.getCode().equals(payOrder.getChannel())){
            throw new TradeStatusErrorException("支付订单不需要同步");
        }
        // 执行订单同步逻辑
        return this.syncPayOrder(payOrder);
    }
    /**
     * 同步支付状态, 开启一个新的事务, 不受外部抛出异常的影响
     * 1. 如果状态一致, 不进行处理
     * 2. 如果状态不一致, 调用调整逻辑进行调整, 更新状态和完成时间
     * 3. 会更新关联网关订单号
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public PaySyncResult syncPayOrder(PayOrder payOrder) {
        // 加锁
        LockInfo lock = lockTemplate.lock("sync:pay" + payOrder.getId(),10000,200);
        if (Objects.isNull(lock)){
            throw new RepetitiveOperationException("支付同步处理中，请勿重复操作");
        }
        try {
            // 获取支付同步策略类
            AbsPaySyncStrategy syncPayStrategy = PayStrategyFactory.create(payOrder.getChannel(), AbsPaySyncStrategy.class);
            syncPayStrategy.initPayParam(payOrder);
            // 执行操作, 获取支付网关同步的结果
            PayRemoteSyncResult payRemoteSyncResult = syncPayStrategy.doSyncStatus();
            // 判断是否同步成功
            if (Objects.equals(payRemoteSyncResult.getSyncStatus(), FAIL)){
                // 同步失败, 返回失败响应, 同时记录失败的日志
                this.saveRecord(payOrder, payRemoteSyncResult, false);
                throw new OperationFailException(payRemoteSyncResult.getErrorMsg());
            }
            // 支付订单的网关订单号是否一致, 不一致进行更新
            if (!Objects.equals(payRemoteSyncResult.getOutOrderNo(), payOrder.getOutOrderNo())){
                payOrder.setOutOrderNo(payRemoteSyncResult.getOutOrderNo());
                payOrderService.updateById(payOrder);
            }
            // 判断网关状态是否和支付单一致, 同时特定情况下更新网关同步状态
            boolean statusSync = this.checkAndAdjustSyncStatus(payRemoteSyncResult,payOrder);
            try {
                // 状态不一致，执行支付单调整逻辑
                if (!statusSync){
                    this.adjustHandler(payRemoteSyncResult, payOrder);
                }
            } catch (PayFailureException e) {
                // 同步失败, 返回失败响应, 同时记录失败的日志
                payRemoteSyncResult.setSyncStatus(FAIL);
                this.saveRecord(payOrder, payRemoteSyncResult, false);
                throw e;
            }
            // 同步成功记录日志
            this.saveRecord( payOrder, payRemoteSyncResult, statusSync);
            return new PaySyncResult().setStatus(payRemoteSyncResult.getSyncStatus().getCode());
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 判断支付单和网关状态是否一致, 同时待支付状态下, 支付单支付超时进行状态的更改
     */
    private boolean checkAndAdjustSyncStatus(PayRemoteSyncResult payRemoteSyncResult, PayOrder order){
        PaySyncStatusEnum syncStatus = payRemoteSyncResult.getSyncStatus();
        String orderStatus = order.getStatus();
        // 本地支付成功/网关支付成功
        if (orderStatus.equals(PayStatusEnum.SUCCESS.getCode()) && syncStatus.equals(SUCCESS)){
            return true;
        }

        /*
        本地支付中/网关支付中或者订单未找到(未知)  支付宝特殊情况，未找到订单可能是发起支付用户未操作、支付已关闭、交易未找到三种情况
        所以需要根据本地订单不同的状态进行特殊处理
         */
        List<PaySyncStatusEnum> syncWaitEnums = Arrays.asList(PROGRESS, NOT_FOUND_UNKNOWN);
        if (orderStatus.equals(PayStatusEnum.PROGRESS.getCode()) && syncWaitEnums.contains(syncStatus)){
            // 判断支付单是否支付超时, 如果待支付状态下触发超时
            if (LocalDateTimeUtil.le(order.getExpiredTime(), LocalDateTime.now())){
                // 将支付单同步状态状态调整为支付超时, 进行订单的关闭
                payRemoteSyncResult.setSyncStatus(TIMEOUT);
                return false;
            }
            return true;
        }
        /*
         关闭 /网关支付关闭、订单未找到、订单未找到(特殊)， 订单未找到(特殊)主要是支付宝特殊情况，
         未找到订单可能是发起支付用户未操作、支付已关闭、交易未找到三种情况
         所以需要根据本地订单不同的状态进行特殊处理, 此处视为支付已关闭、交易未找到这两种, 处理方式相同, 都作为支付关闭处理
         */
        List<String> payCloseEnums = Arrays.asList(PayStatusEnum.CLOSE.getCode(), PayStatusEnum.CANCEL.getCode());
        List<PaySyncStatusEnum> syncClose = Arrays.asList(CLOSED, NOT_FOUND, NOT_FOUND_UNKNOWN);
        if (payCloseEnums.contains(orderStatus) && syncClose.contains(syncStatus)){
            return true;
        }

        // 退款状态不做额外处理, 需要通过退款接口进行处理
        if (!Objects.equals(order.getRefundStatus(),PayOrderRefundStatusEnum.NO_REFUND.getCode())
                || syncStatus.equals(REFUND)){
            return true;
        }
        return false;
    }

    /**
     * 根据同步的结果对支付单进行调整处理
     */
    private void adjustHandler(PayRemoteSyncResult payRemoteSyncResult, PayOrder payOrder){
        PaySyncStatusEnum syncStatusEnum = payRemoteSyncResult.getSyncStatus();
        // 对支付网关同步的结果进行处理
        switch (syncStatusEnum) {
            // 支付成功 支付宝退款时也是支付成功状态, 除非支付完成
            case SUCCESS: {
                this.success(payOrder, payRemoteSyncResult);
            }
            case REFUND:
                throw new TradeStatusErrorException("支付订单为退款状态，请通过执行对应的退款订单进行同步，来更新具体为什么类型退款状态");
            // 交易关闭和未找到, 都对本地支付订单进行关闭, 不需要再调用网关进行关闭
            case CLOSED:
            case NOT_FOUND: {
                this.closeLocal(payOrder);
            }
            // 超时关闭和交易不存在(特殊) 关闭本地支付订单, 同时调用网关进行关闭, 确保后续这个订单不能被支付
            case TIMEOUT:
                this.closeRemote(payOrder);
            case NOT_FOUND_UNKNOWN:{
            }
            // 调用出错
            case FAIL: {
                // 不进行处理
                log.warn("支付状态同步接口调用出错");
                break;
            }
            default: {
                throw new SystemUnknownErrorException("代码有问题");
            }
        }
    }


    /**
     * 变更为已支付
     * 同步: 将异步支付状态修改为成功
     * 回调: 将异步支付状态修改为成功
     */
    private void success(PayOrder order, PayRemoteSyncResult param) {
        // 修改订单支付状态为成功
        order.setStatus(PayStatusEnum.SUCCESS.getCode())
                .setPayTime(param.getFinishTime())
                .setOutOrderNo(param.getOutOrderNo())
                .setCloseTime(null);
        payOrderService.updateById(order);
    }

    /**
     * 关闭支付
     * 同步/对账: 执行支付单所有的支付通道关闭支付逻辑, 不需要调用网关关闭,
     */
    private void closeLocal(PayOrder order) {
        // 执行策略的关闭方法
        order.setStatus(PayStatusEnum.CLOSE.getCode())
                .setCloseTime(LocalDateTime.now());
        payOrderService.updateById(order);
    }
    /**
     * 关闭网关交易, 同时也会关闭本地支付
     * 回调: 执行所有的支付通道关闭支付逻辑
     */
    private void closeRemote(PayOrder payOrder) {
        // 初始化调整参数
        AbsPayCloseStrategy strategy = PayStrategyFactory.create(payOrder.getChannel(), AbsPayCloseStrategy.class);
        strategy.setOrder(payOrder);
        // 执行策略的关闭方法
        strategy.doCloseHandler();
        payOrder.setStatus(PayStatusEnum.CLOSE.getCode())
                .setCloseTime(LocalDateTime.now());
        payOrderService.updateById(payOrder);
    }

    /**
     * 保存同步记录
     * @param payOrder 支付单
     * @param payRemoteSyncResult 同步结果
     * @param adjust 调整号
     */
    private void saveRecord(PayOrder payOrder, PayRemoteSyncResult payRemoteSyncResult, boolean adjust){
        TradeSyncRecord tradeSyncRecord = new TradeSyncRecord()
                .setBizTradeNo(payOrder.getBizOrderNo())
                .setTradeNo(payOrder.getOrderNo())
                .setOutTradeNo(payOrder.getOutOrderNo())
                .setOutTradeStatus(payRemoteSyncResult.getSyncStatus().getCode())
                .setType(TradeTypeEnum.PAY.getCode())
                .setChannel(payOrder.getChannel())
                .setSyncInfo(payRemoteSyncResult.getSyncInfo())
                .setAdjust(adjust)
                .setErrorCode(payRemoteSyncResult.getErrorCode())
                .setErrorMsg(payRemoteSyncResult.getErrorMsg())
                .setClientIp(PaymentContextLocal.get().getClientInfo().getClientIp());
        tradeSyncRecordService.saveRecord(tradeSyncRecord);
    }

}
