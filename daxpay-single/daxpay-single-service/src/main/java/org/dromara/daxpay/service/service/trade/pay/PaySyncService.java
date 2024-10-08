package org.dromara.daxpay.service.service.trade.pay;

import cn.bootx.platform.core.exception.RepetitiveOperationException;
import cn.bootx.platform.core.util.DateTimeUtil;
import org.dromara.daxpay.core.enums.PayStatusEnum;
import org.dromara.daxpay.core.enums.TradeTypeEnum;
import org.dromara.daxpay.core.exception.OperationFailException;
import org.dromara.daxpay.core.exception.PayFailureException;
import org.dromara.daxpay.core.exception.SystemUnknownErrorException;
import org.dromara.daxpay.core.exception.TradeNotExistException;
import org.dromara.daxpay.core.param.trade.pay.PaySyncParam;
import org.dromara.daxpay.core.result.trade.pay.PaySyncResult;
import org.dromara.daxpay.service.bo.sync.PaySyncResultBo;
import org.dromara.daxpay.service.common.local.PaymentContextLocal;
import org.dromara.daxpay.service.dao.order.pay.PayOrderManager;
import org.dromara.daxpay.service.entity.order.pay.PayOrder;
import org.dromara.daxpay.service.entity.record.sync.TradeSyncRecord;
import org.dromara.daxpay.service.service.notice.MerchantNoticeService;
import org.dromara.daxpay.service.service.order.pay.PayOrderQueryService;
import org.dromara.daxpay.service.service.record.sync.TradeSyncRecordService;
import org.dromara.daxpay.service.strategy.AbsPayCloseStrategy;
import org.dromara.daxpay.service.strategy.AbsSyncPayOrderStrategy;
import org.dromara.daxpay.service.util.PaymentStrategyFactory;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

import static org.dromara.daxpay.core.enums.PayStatusEnum.*;


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

    private final PayOrderManager payOrderManager;

    private final TradeSyncRecordService tradeSyncRecordService;

    private final LockTemplate lockTemplate;

    private final MerchantNoticeService merchantNoticeService;

    /**
     * 支付同步, 开启一个新的事务, 不受外部抛出异常的影响
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public PaySyncResult sync(PaySyncParam param) {
        PayOrder payOrder = payOrderQueryService.findByBizOrOrderNo(param.getOrderNo(), param.getBizOrderNo(), param.getAppId())
                .orElseThrow(() -> new TradeNotExistException("支付订单不存在"));
        // 执行订单同步逻辑
        return this.syncPayOrder(payOrder);
    }
    /**
     * 同步支付状态, 开启一个新的事务, 不受外部抛出异常的影响
     * 1. 如果状态一致, 不进行处理， 直接返回
     * 2. 如果状态不一致, 更新状态/完成时间/关联网关订单号
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public PaySyncResult syncPayOrder(PayOrder payOrder) {
        // 加锁
        LockInfo lock = lockTemplate.lock("sync:pay" + payOrder.getId(),10000,200);
        if (Objects.isNull(lock)){
            throw new RepetitiveOperationException("支付同步处理中，请勿重复操作");
        }
        // 获取支付同步策略类并初始化
        AbsSyncPayOrderStrategy syncPayStrategy = PaymentStrategyFactory.create(payOrder.getChannel(), AbsSyncPayOrderStrategy.class);
        syncPayStrategy.setOrder(payOrder);
        try {
            // 执行操作, 获取支付网关同步的结果
            PaySyncResultBo syncResult = syncPayStrategy.doSync();
            // 判断是否同步成功
            if (!syncResult.isSyncSuccess()){
                // 同步失败, 返回失败响应, 同时记录失败的日志
                this.saveRecord(payOrder, syncResult, false);
                throw new OperationFailException(syncResult.getSyncErrorMsg());
            }
            // 支付订单的网关订单号是否一致, 不一致进行更新
            if (!Objects.equals(syncResult.getOutOrderNo(), payOrder.getOutOrderNo())){
                payOrder.setOutOrderNo(syncResult.getOutOrderNo());
                payOrderManager.updateById(payOrder);
            }
            // 判断网关状态是否和支付单一致, 同时特定情况下更新网关同步状态或记录异常信息
            boolean statusSync = this.checkAndAdjust(syncResult,payOrder);
            try {
                // 状态不一致，执行支付单调整逻辑
                if (!statusSync){
                    this.adjustHandler(syncResult, payOrder);
                }
            } catch (PayFailureException e) {
                // 同步失败, 返回失败响应, 同时记录失败的日志
                syncResult.setSyncSuccess(false);
                this.saveRecord(payOrder, syncResult, false);
                throw e;
            }
            // 同步成功记录日志
            this.saveRecord(payOrder, syncResult, !statusSync);
            return new PaySyncResult()
                    .setOrderStatus(payOrder.getStatus())
                    .setAdjust(statusSync);
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 判断支付单和网关状态是否一致, 同时待支付状态下, 支付单支付超时进行状态的更改
     * 如果本地订单状态为支付中, 会对订单信息进行调整
     * 如果本地订单状态不为为支付中, 状态不一致, 未来将 记录异常情况, 现在不会进行任何操作
     */
    private boolean checkAndAdjust(PaySyncResultBo payRemoteSyncResult, PayOrder order){
        var payStatus = payRemoteSyncResult.getPayStatus();
        String orderStatus = order.getStatus();

        // 本地订单为支付中时, 对状态进行比较,
        if (orderStatus.equals(PROGRESS.getCode())){
            // 如果返回订单也是支付中
            if (Objects.equals(PROGRESS, payStatus)){
                // 判断支付单是否支付超时, 如果待支付状态下触发超时
                if (DateTimeUtil.le(order.getExpiredTime(), LocalDateTime.now())){
                    // 将支付单同步状态状态调整为支付超时, 进行订单的关闭
                    payRemoteSyncResult.setPayStatus(PayStatusEnum.TIMEOUT);
                    return false;
                }
                return true;
            }
        } else {
            return true;
        }
        return false;
    }

    /**
     * 根据同步的结果对支付单进行调整处理
     */
    private void adjustHandler(PaySyncResultBo payRemoteSyncResult, PayOrder payOrder){
        var payStatus = payRemoteSyncResult.getPayStatus();
        // 对支付网关同步的结果进行处理
        switch (payStatus) {
            // 支付成功 支付宝退款时也是支付成功状态, 除非支付完成
            case PROGRESS -> {}
            case SUCCESS -> this.success(payOrder, payRemoteSyncResult);
            case CLOSE, CANCEL -> this.closeLocal(payOrder);
            // 超时关闭和交易不存在(特殊) 关闭本地支付订单, 同时调用网关进行关闭, 确保后续这个订单不能被支付
            case TIMEOUT -> this.closeRemote(payOrder);
            default -> throw new SystemUnknownErrorException("代码有问题");
        }
    }


    /**
     * 变更为已支付
     * 同步: 将异步支付状态修改为成功
     * 回调: 将异步支付状态修改为成功
     */
    private void success(PayOrder order, PaySyncResultBo param) {
        // 修改订单支付状态为成功
        order.setStatus(SUCCESS.getCode())
                .setPayTime(param.getFinishTime())
                .setCloseTime(null);
        payOrderManager.updateById(order);
        merchantNoticeService.registerPayNotice(order);
    }

    /**
     * 关闭支付
     * 同步/对账: 执行支付单所有的支付通道关闭支付逻辑, 不需要调用网关关闭,
     */
    private void closeLocal(PayOrder order) {
        // 执行策略的关闭方法
        order.setStatus(CLOSE.getCode())
                .setCloseTime(LocalDateTime.now());
        payOrderManager.updateById(order);
        merchantNoticeService.registerPayNotice(order);
    }
    /**
     * 关闭网关交易, 同时也会关闭本地支付
     * 回调: 执行所有的支付通道关闭支付逻辑
     */
    private void closeRemote(PayOrder order) {
        // 初始化调整参数
        AbsPayCloseStrategy strategy = PaymentStrategyFactory.create(order.getChannel(), AbsPayCloseStrategy.class);
        strategy.setOrder(order);
        strategy.doBeforeCloseHandler();
        // 执行策略的关闭方法
        strategy.doCloseHandler();
        order.setStatus(CLOSE.getCode())
                .setCloseTime(LocalDateTime.now());
        payOrderManager.updateById(order);
        merchantNoticeService.registerPayNotice(order);
    }

    /**
     * 保存同步记录
     * @param payOrder 支付单
     * @param payRemoteSyncResult 同步结果
     * @param adjust 调整号
     */
    private void saveRecord(PayOrder payOrder, PaySyncResultBo payRemoteSyncResult, boolean adjust){
        TradeSyncRecord tradeSyncRecord = new TradeSyncRecord()
                .setBizTradeNo(payOrder.getBizOrderNo())
                .setTradeNo(payOrder.getOrderNo())
                .setOutTradeNo(payOrder.getOutOrderNo())
                .setTradeType(TradeTypeEnum.PAY.getCode())
                .setChannel(payOrder.getChannel())
                .setSyncInfo(payRemoteSyncResult.getSyncData())
                .setAdjust(adjust)
                .setErrorCode(payRemoteSyncResult.getSyncErrorCode())
                .setErrorMsg(payRemoteSyncResult.getSyncErrorMsg())
                .setClientIp(PaymentContextLocal.get().getClientInfo().getClientIp());
        if (payRemoteSyncResult.isSyncSuccess()){
            tradeSyncRecord.setOutTradeStatus(payRemoteSyncResult.getPayStatus().getCode());
        }
        tradeSyncRecordService.saveRecord(tradeSyncRecord);
    }

}
