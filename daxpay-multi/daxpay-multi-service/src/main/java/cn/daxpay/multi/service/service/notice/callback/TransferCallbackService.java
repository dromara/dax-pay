package cn.daxpay.multi.service.service.notice.callback;

import cn.daxpay.multi.core.enums.CallbackStatusEnum;
import cn.daxpay.multi.core.enums.TransferStatusEnum;
import cn.daxpay.multi.service.common.context.CallbackLocal;
import cn.daxpay.multi.service.common.local.PaymentContextLocal;
import cn.daxpay.multi.service.dao.order.transfer.TransferOrderManager;
import cn.daxpay.multi.service.entity.order.transfer.TransferOrder;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    /**
     * 转账回调统一处理
     */
    public void transferCallback() {

        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        // 加锁
        LockInfo lock = lockTemplate.lock("callback:transfer:" + callbackInfo.getTradeNo(),10000, 200);
        if (Objects.isNull(lock)){
            callbackInfo.setCallbackStatus(CallbackStatusEnum.IGNORE).setErrorMsg("回调正在处理中，忽略本次回调请求");
            log.warn("订单号: {} 回调正在处理中，忽略本次回调请求", callbackInfo.getTradeNo());
            return;
        }
        try {
            // 获取转账单
            var transferOrder = transferOrderManager.findByTransferNo(callbackInfo.getTradeNo()).orElse(null);
            // 转账单不存在,记录回调记录
            if (Objects.isNull(transferOrder)) {
                callbackInfo.setCallbackStatus(CallbackStatusEnum.NOT_FOUND).setErrorMsg("转账单不存在,记录回调记录");
                return;
            }
            // 转账单已经被处理, 记录回调记录
            if (!Objects.equals(TransferStatusEnum.PROGRESS.getCode(), transferOrder.getStatus())) {
                callbackInfo.setCallbackStatus(CallbackStatusEnum.IGNORE).setErrorMsg("转账单状态已处理,记录回调记录");
                return;
            }
            // 转账成功
            if (Objects.equals(TransferStatusEnum.SUCCESS.getCode(), callbackInfo.getOutStatus())) {
                this.success(transferOrder);
            }
            // 转账关闭
            if (Objects.equals(TransferStatusEnum.FAIL.getCode(), callbackInfo.getOutStatus())){
                this.close(transferOrder);
            }
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 转账成功
     */
    private void success(TransferOrder transferOrder) {
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        transferOrder.setStatus(TransferStatusEnum.SUCCESS.getCode())
                .setFinishTime(callbackInfo.getFinishTime());
        transferOrderManager.updateById(transferOrder);
        // 发送通知
//        clientNoticeService.registerTransferNotice(transferOrder);
    }


    /**
     * 转账失败或关闭
     */
    private void close(TransferOrder transferOrder) {
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        transferOrder.setStatus(TransferStatusEnum.CLOSE.getCode())
                .setFinishTime(callbackInfo.getFinishTime());
        transferOrderManager.updateById(transferOrder);
        // 发送通知
//        clientNoticeService.registerTransferNotice(transferOrder);
    }
}

