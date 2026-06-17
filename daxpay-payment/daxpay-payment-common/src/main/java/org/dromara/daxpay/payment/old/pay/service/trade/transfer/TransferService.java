package org.dromara.daxpay.payment.old.pay.service.trade.transfer;

import org.dromara.daxpay.platform.core.util.ValidationUtil;
import org.dromara.daxpay.platform.core.enums.pay.transfer.TransferStatusEnum;
import org.dromara.daxpay.payment.old.pay.exception.TradeProcessingException;
import org.dromara.daxpay.payment.unipay.param.trade.transfer.TransferParam;
import org.dromara.daxpay.payment.unipay.result.trade.transfer.TransferResult;
import org.dromara.daxpay.payment.old.pay.bo.trade.TransferResultBo;
import org.dromara.daxpay.platform.core.code.DaxPayErrorCode;
import org.dromara.daxpay.payment.old.pay.dao.order.transfer.TransferOrderManager;
import org.dromara.daxpay.payment.old.pay.entity.order.transfer.TransferOrder;
import org.dromara.daxpay.payment.old.pay.service.notice.MerchantNoticeService;
import org.dromara.daxpay.payment.old.pay.service.record.flow.TradeFlowRecordService;
import org.dromara.daxpay.payment.strategy.transfer.AbsTransferStrategy;
import org.dromara.daxpay.payment.common.util.PaymentStrategyFactory;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/// # 转账服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class TransferService {

    private final TransferAssistService transferAssistService;

    private final TransferOrderManager transferOrderManager;

    private final LockTemplate lockTemplate;
    private final MerchantNoticeService merchantNoticeService;
    private final TradeFlowRecordService tradeFlowRecordService;
//    private final DelayJobService delayJobService;

    /// 转账
    public TransferResult transfer(TransferParam param) {
        // 参数校验 跳过来源校验
        String source = param.getSource();
        param.setSource(null);
        ValidationUtil.validateParam(param);
        param.setSource(source);
        // 加锁
        LockInfo lock = lockTemplate.lock("payment:transfer:" + param.getBizTransferNo(),10000,200);
        if (Objects.isNull(lock)){
            // 转账处理中，请勿重复操作
            throw new TradeProcessingException();
        }
        try {
            // 判断是否是首次发起转账
            var transferOrder = transferOrderManager.findByBizTransferNo(param.getBizTransferNo(),param.getAppId());
            if (transferOrder.isPresent()){
                return this.repeatTransfer(transferOrder.get(),param);
            } else {
                return this.firstTransfer(param);
            }
        } finally {
            lockTemplate.releaseLock(lock);
        }

    }

    /// 首次转账发起
    private TransferResult firstTransfer(TransferParam param) {
        // 获取策略
        var transferStrategy = PaymentStrategyFactory.createByProduct(param.getProduct(), AbsTransferStrategy.class);
        // 初始化
        transferStrategy.setTransferParam(param);
        // 执行预处理
        transferStrategy.doBeforeHandler();
        // 检查转账参数
        transferStrategy.doValidateParam();
        // 创建转账订单
        TransferOrder order = transferAssistService.createOrder(param);
        transferStrategy.setTransferOrder(order);
        TransferResultBo transferResultBo;
        try {
            // 执行转账策略
            transferResultBo = transferStrategy.doTransferHandler();
        } catch (Exception e) {
            log.error("转账出现错误", e);
            // 更新转账失败的记录
            transferAssistService.updateOrderByError(order,e.getMessage());
            return transferAssistService.buildResult(order);
        }
        SpringUtil.getBean(this.getClass()).successHandler(order, transferResultBo);
        return transferAssistService.buildResult(order);
    }

    /// 重复发起转账
    private TransferResult repeatTransfer(TransferOrder order, TransferParam param) {
        // 只有转账失败才可可以重新发起
        if (!Objects.equals(TransferStatusEnum.FAIL.getCode(), order.getStatus())){
            // 只有失败状态的才可以重新发起转账
            throw new TradeProcessingException(DaxPayErrorCode.TRADE_PROCESSING, "pay.error.transfer.onlyFailRetry");
        }
        // 获取策略
        var transferStrategy = PaymentStrategyFactory.createByProduct(param.getProduct(), AbsTransferStrategy.class);
        // 参数设置
        transferStrategy.setTransferParam(param);
        transferStrategy.setTransferOrder(order);
        // 执行预处理
        transferStrategy.doBeforeHandler();
        // 检查转账参数
        transferStrategy.doValidateParam();
        TransferResultBo transferResultBo;
        try {
            // 执行转账策略
            transferResultBo = transferStrategy.doTransferHandler();
        } catch (Exception e) {
            log.error("重现转账出现错误", e);
            // 更新转账失败的记录
            transferAssistService.updateOrderByError(order,e.getMessage());
            return transferAssistService.buildResult(order);
        }
        SpringUtil.getBean(this.getClass()).successHandler(order, transferResultBo);
        return transferAssistService.buildResult(order);
    }

    /// 成功处理
    @Transactional(rollbackFor = Exception.class)
    public void successHandler(TransferOrder order, TransferResultBo transferInfo){
        order.setStatus(transferInfo.getStatus().getCode())
                .setFinishTime(transferInfo.getFinishTime())
                .setTransferBody(transferInfo.getTransferBody())
                .setOutTransferNo(transferInfo.getOutTransferNo());
        // 是否直接返回转账成功
        if (Objects.equals(transferInfo.getStatus(), TransferStatusEnum.SUCCESS)){
            // 发送转账订单通知消息
            merchantNoticeService.registerTransferNotice(order);
            tradeFlowRecordService.saveTransfer(order);
        } else {
            // 注册延时同步事件
//            delayJobService.registerByTransaction(order.getId(), DaxPayCode.Event.ORDER_TRANSFER_SYNC, 2*60*1000L);
        }
        transferOrderManager.updateById(order);
    }
}
