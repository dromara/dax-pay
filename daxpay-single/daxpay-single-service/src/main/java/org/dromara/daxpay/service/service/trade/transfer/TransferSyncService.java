package org.dromara.daxpay.service.service.trade.transfer;

import cn.bootx.platform.core.exception.BizException;
import cn.bootx.platform.core.exception.RepetitiveOperationException;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.core.enums.TradeTypeEnum;
import org.dromara.daxpay.core.enums.TransferStatusEnum;
import org.dromara.daxpay.core.exception.TradeNotExistException;
import org.dromara.daxpay.core.param.trade.transfer.TransferSyncParam;
import org.dromara.daxpay.core.result.trade.transfer.TransferSyncResult;
import org.dromara.daxpay.service.bo.sync.TransferSyncResultBo;
import org.dromara.daxpay.service.common.local.PaymentContextLocal;
import org.dromara.daxpay.service.dao.order.transfer.TransferOrderManager;
import org.dromara.daxpay.service.entity.order.transfer.TransferOrder;
import org.dromara.daxpay.service.entity.record.sync.TradeSyncRecord;
import org.dromara.daxpay.service.service.notice.MerchantNoticeService;
import org.dromara.daxpay.service.service.order.transfer.TransferOrderQueryService;
import org.dromara.daxpay.service.service.record.flow.TradeFlowRecordService;
import org.dromara.daxpay.service.service.record.sync.TradeSyncRecordService;
import org.dromara.daxpay.service.strategy.AbsSyncTransferOrderStrategy;
import org.dromara.daxpay.service.util.PaymentStrategyFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static org.dromara.daxpay.core.enums.TradeStatusEnum.FAIL;

/**
 * 转账同步接口
 * @author xxm
 * @since 2024/6/17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransferSyncService {

    private final TransferOrderQueryService transferOrderService;
    private final LockTemplate lockTemplate;
    private final TradeSyncRecordService tradeSyncRecordService;
    private final TransferOrderManager transferOrderManager;
    private final MerchantNoticeService merchantNoticeService;
    private final TransferAssistService transferAssistService;
    private final TradeFlowRecordService tradeFlowRecordService;

    /**
     * 转账同步接口
     */
    public TransferSyncResult sync(TransferSyncParam param) {
        TransferOrder transferOrder = transferOrderService.findByBizOrTransferNo(param.getTransferNo(), param.getBizTransferNo(),param.getAppId())
                .orElseThrow(() -> new TradeNotExistException("转账订单不存在"));
        // 执行订单同步逻辑
        return this.syncTransferOrder(transferOrder);
    }

    /**
     * 转账订单同步接口
     */
    public TransferSyncResult syncTransferOrder(TransferOrder transferOrder){
        // 加锁
        LockInfo lock = lockTemplate.lock("sync:transfer" + transferOrder.getId(),10000,200);
        if (Objects.isNull(lock)){
            throw new RepetitiveOperationException("转账同步处理中，请勿重复操作");
        }
        // 获取转账同步策略类并初始化
        var syncPayStrategy = PaymentStrategyFactory.create(transferOrder.getChannel(), AbsSyncTransferOrderStrategy.class);
        syncPayStrategy.setTransferOrder(transferOrder);
        // 同步前处理, 主要预防请求过于迅速
        syncPayStrategy.doBeforeHandler();
        try {
            // 执行操作, 获取支付网关同步的结果
            var syncResult = syncPayStrategy.doSync();
            // 转账订单的网关订单号是否一致, 不一致进行更新
            if (!Objects.equals(syncResult.getOutTransferNo(), transferOrder.getOutTransferNo())){
                transferOrder.setOutTransferNo(syncResult.getOutTransferNo());
                transferOrderManager.updateById(transferOrder);
            }
            // 判断网关状态是否和转账订单一致, 同时特定情况下更新网关同步状态或记录异常信息
            boolean statusSync = this.checkStatus(syncResult,transferOrder);
            try {
                // 状态不一致，执行支付单调整逻辑
                if (!statusSync){
                    this.adjustHandler(syncResult, transferOrder);
                }
            } catch (Exception e) {
                // 同步失败, 返回失败响应, 同时记录失败的日志
                syncResult.setSyncSuccess(false);
                this.saveRecord(transferOrder, syncResult, false);
                throw e;
            }
            // 判断是否同步成功
            if (syncResult.isSyncSuccess()){
                // 同步成功
                this.saveRecord(transferOrder, syncResult, statusSync);
            } else {
                // 同步失败, 返回失败响应, 同时记录失败的日志
                this.saveRecord(transferOrder, syncResult, true);
            }
            return new TransferSyncResult()
                    .setOrderStatus(transferOrder.getStatus())
                    .setAdjust(statusSync);
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }


    /**
     * 检查状态是否一致
     * @see TransferStatusEnum 转账单状态
     */
    private boolean checkStatus(TransferSyncResultBo syncResult, TransferOrder order){
        var syncStatus = syncResult.getTransferStatus();
        String orderStatus = order.getStatus();
        // 如果本地订单为失败时, 直接返回需要进行调整
        if (orderStatus.equals(FAIL.getCode())){
            return false;
        }
        // 如果订单为转账中, 对状态进行比较
        if (Objects.equals(orderStatus, TransferStatusEnum.SUCCESS.getCode())){
            // 转账完成
            if (Objects.equals(syncStatus, TransferStatusEnum.SUCCESS)) {
                return true;
            }
            // 转账失败
            if (Objects.equals(syncStatus, TransferStatusEnum.FAIL)) {
                return true;
            }
            // 转账中
            if (Objects.equals(syncStatus, TransferStatusEnum.PROGRESS)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 进行转账订单和支付订单的调整
     */
    private void adjustHandler(TransferSyncResultBo syncResult, TransferOrder order){
        var syncStatusEnum = syncResult.getTransferStatus();
        // 对支付网关同步的结果进行处理
        switch (syncStatusEnum) {
            case SUCCESS -> SpringUtil.getBean(this.getClass()).success(order, syncResult);
            case PROGRESS -> {}
            case CLOSE -> transferAssistService.close(order,syncResult.getFinishTime());
            case FAIL -> transferAssistService.updateOrderByError(order,syncResult.getSyncErrorMsg());
            default -> throw new BizException("代码有问题");
        }
    }


    /**
     * 转账成功, 更新转账单和支付单
     */
    @Transactional(rollbackFor = Exception.class)
    public void success(TransferOrder order, TransferSyncResultBo syncResult) {
        // 修改订单支付状态为成功
        order.setStatus(TransferStatusEnum.SUCCESS.getCode())
                .setFinishTime(syncResult.getFinishTime());

        if (StrUtil.isNotBlank(syncResult.getOutTransferNo())){
            order.setTransferNo(syncResult.getOutTransferNo());
        }
        tradeFlowRecordService.saveTransfer(order);
        transferOrderManager.updateById(order);
        merchantNoticeService.registerTransferNotice(order);
    }



    /**
     * 保存同步记录
     * @param order 支付单
     * @param payRemoteSyncResult 同步结果
     * @param adjust 调整号
     */
    private void saveRecord(TransferOrder order, TransferSyncResultBo payRemoteSyncResult, boolean adjust){
        TradeSyncRecord tradeSyncRecord = new TradeSyncRecord()
                .setBizTradeNo(order.getBizTransferNo())
                .setTradeNo(order.getTransferNo())
                .setOutTradeNo(order.getOutTransferNo())
                .setTradeType(TradeTypeEnum.TRANSFER.getCode())
                .setChannel(order.getChannel())
                .setSyncInfo(payRemoteSyncResult.getSyncData())
                .setAdjust(adjust)
                .setErrorCode(payRemoteSyncResult.getSyncErrorCode())
                .setErrorMsg(payRemoteSyncResult.getSyncErrorMsg())
                .setClientIp(PaymentContextLocal.get().getClientInfo().getClientIp());
        if (payRemoteSyncResult.isSyncSuccess()){
            tradeSyncRecord.setOutTradeStatus(payRemoteSyncResult.getTransferStatus().getCode());
        }
        tradeSyncRecordService.saveRecord(tradeSyncRecord);
    }
}
