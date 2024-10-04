package org.dromara.daxpay.service.service.trade.transfer;

import org.dromara.daxpay.core.enums.CallbackStatusEnum;
import org.dromara.daxpay.core.enums.TransferStatusEnum;
import org.dromara.daxpay.service.common.context.CallbackLocal;
import org.dromara.daxpay.service.common.local.PaymentContextLocal;
import org.dromara.daxpay.service.dao.order.transfer.TransferOrderManager;
import org.dromara.daxpay.service.entity.order.transfer.TransferOrder;
import org.dromara.daxpay.service.service.notice.MerchantNoticeService;
import org.dromara.daxpay.service.service.record.flow.TradeFlowRecordService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * 转账订单回调处理
 * @author xxm
 * @since 2024/7/30
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransferCallbackService {
    private final LockTemplate lockTemplate;
    private final TransferOrderManager transferOrderManager;
    private final MerchantNoticeService merchantNoticeService;
    private final TransferAssistService transferAssistService;
    private final TradeFlowRecordService tradeFlowRecordService;

    /**
     * 转账回调统一处理
     */
    public void transferCallback() {

        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        // 加锁
        LockInfo lock = lockTemplate.lock("callback:transfer:" + callbackInfo.getTradeNo(),10000, 200);
        if (Objects.isNull(lock)){
            callbackInfo.setCallbackStatus(CallbackStatusEnum.IGNORE).setCallbackErrorMsg("回调正在处理中，忽略本次回调请求");
            log.warn("订单号: {} 回调正在处理中，忽略本次回调请求", callbackInfo.getTradeNo());
            return;
        }
        try {
            // 获取转账单
            var transferOrder = transferOrderManager.findByTransferNo(callbackInfo.getTradeNo()).orElse(null);
            // 转账单不存在,记录回调记录
            if (Objects.isNull(transferOrder)) {
                callbackInfo.setCallbackStatus(CallbackStatusEnum.NOT_FOUND).setCallbackErrorMsg("转账单不存在,记录回调记录");
                return;
            }
            // 转账单已经被处理, 记录回调记录
            if (!Objects.equals(TransferStatusEnum.PROGRESS.getCode(), transferOrder.getStatus())) {
                callbackInfo.setCallbackStatus(CallbackStatusEnum.IGNORE).setCallbackErrorMsg("转账单状态已处理,记录回调记录");
                return;
            }
            // 转账成功
            if (Objects.equals(TransferStatusEnum.SUCCESS.getCode(), callbackInfo.getTradeStatus())) {
                SpringUtil.getBean(this.getClass()).success(transferOrder);
            }
            // 转账失败
            if (Objects.equals(TransferStatusEnum.FAIL.getCode(), callbackInfo.getTradeStatus())){
                this.fail(transferOrder);
            }
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 转账成功
     */
    @Transactional(rollbackFor = Exception.class)
    public void success(TransferOrder transferOrder) {
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        transferOrder.setStatus(TransferStatusEnum.SUCCESS.getCode())
                .setFinishTime(callbackInfo.getFinishTime());

        if (StrUtil.isNotBlank(callbackInfo.getOutTradeNo())){
            transferOrder.setTransferNo(callbackInfo.getTradeNo());
        }

        transferOrderManager.updateById(transferOrder);
        // 记录流水
        tradeFlowRecordService.saveTransfer(transferOrder);
        // 发送通知
        merchantNoticeService.registerTransferNotice(transferOrder);
    }


    /**
     * 转账失败或关闭
     */
    private void fail(TransferOrder transferOrder) {
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        transferOrder.setErrorMsg(callbackInfo.getTradeErrorMsg());
        transferAssistService.close(transferOrder,callbackInfo.getFinishTime());
        merchantNoticeService.registerTransferNotice(transferOrder);
    }
}

