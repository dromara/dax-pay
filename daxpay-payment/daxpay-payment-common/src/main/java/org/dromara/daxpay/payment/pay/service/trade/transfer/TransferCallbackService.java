package org.dromara.daxpay.payment.pay.service.trade.transfer;

import org.dromara.daxpay.platform.core.enums.pay.notice.CallbackStatusEnum;
import org.dromara.daxpay.platform.core.enums.pay.transfer.TransferStatusEnum;
import org.dromara.daxpay.platform.common.i18n.util.I18nUtil;
import org.dromara.daxpay.payment.common.context.CallbackInfo;
import org.dromara.daxpay.payment.common.context.PaymentContext;
import org.dromara.daxpay.payment.pay.dao.order.transfer.TransferOrderManager;
import org.dromara.daxpay.payment.pay.entity.order.transfer.TransferOrder;
import org.dromara.daxpay.payment.pay.service.notice.MerchantNoticeService;
import org.dromara.daxpay.payment.pay.service.record.flow.TradeFlowRecordService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/// # 转账订单回调处理
///
@Slf4j
@Service
@RequiredArgsConstructor
public class TransferCallbackService {
    private final LockTemplate lockTemplate;
    private final TransferOrderManager transferOrderManager;
    private final MerchantNoticeService merchantNoticeService;
    private final TransferAssistService transferAssistService;
    private final TradeFlowRecordService tradeFlowRecordService;

    private final PaymentContext apiContext;

    /// 转账回调统一处理, 返回转账产品编码
    public String transferCallback() {

        CallbackInfo callbackInfo = apiContext.getCallbackInfo();
        // 加锁
        LockInfo lock = lockTemplate.lock("callback:transfer:" + callbackInfo.getTradeNo(),10000, 200);
        if (Objects.isNull(lock)){
            callbackInfo.setCallbackStatus(CallbackStatusEnum.IGNORE).setCallbackErrorMsg(I18nUtil.get("pay.error.callback.processing"));
            log.warn("订单号: {} 回调正在处理中，忽略本次回调请求", callbackInfo.getTradeNo());
            return null;
        }
        try {
            // 获取转账单
            var transferOrder = transferOrderManager.findByTransferNo(callbackInfo.getTradeNo()).orElse(null);
            // 转账单不存在,记录回调记录
            if (Objects.isNull(transferOrder)) {
                callbackInfo.setCallbackStatus(CallbackStatusEnum.NOT_FOUND).setCallbackErrorMsg(I18nUtil.get("pay.error.callback.transferNotFound"));
                return null;
            }
            // 转账单已经被处理, 记录回调记录
            if (!Objects.equals(TransferStatusEnum.PROGRESS.getCode(), transferOrder.getStatus())) {
                callbackInfo.setCallbackStatus(CallbackStatusEnum.IGNORE).setCallbackErrorMsg(I18nUtil.get("pay.error.callback.transferAlreadyProcessed"));
                return null;
            }
            // 转账成功
            if (Objects.equals(TransferStatusEnum.SUCCESS.getCode(), callbackInfo.getTradeStatus())) {
                SpringUtil.getBean(this.getClass()).success(transferOrder);
            }
            // 转账失败或关闭
            if (List.of(TransferStatusEnum.FAIL.getCode(), TransferStatusEnum.CLOSE.getCode()).contains(callbackInfo.getTradeStatus())){
                this.fail(transferOrder);
            }
            return transferOrder.getProduct();
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /// 转账成功
    @Transactional(rollbackFor = Exception.class)
    public void success(TransferOrder transferOrder) {
        CallbackInfo callbackInfo = apiContext.getCallbackInfo();
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

    /// 转账失败或关闭
    private void fail(TransferOrder transferOrder) {
        CallbackInfo callbackInfo = apiContext.getCallbackInfo();
        transferOrder.setErrorMsg(callbackInfo.getTradeErrorMsg());
        transferAssistService.close(transferOrder,callbackInfo.getFinishTime());
        merchantNoticeService.registerTransferNotice(transferOrder);
    }
}

