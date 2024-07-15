package cn.daxpay.single.service.core.payment.callback.service;

import cn.daxpay.single.core.code.RefundStatusEnum;
import cn.daxpay.single.service.code.PayCallbackStatusEnum;
import cn.daxpay.single.service.code.RefundAdjustWayEnum;
import cn.daxpay.single.service.code.TradeAdjustSourceEnum;
import cn.daxpay.single.service.common.context.CallbackLocal;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.order.refund.dao.RefundOrderManager;
import cn.daxpay.single.service.core.order.refund.entity.RefundOrder;
import cn.daxpay.single.service.core.payment.adjust.param.RefundAdjustParam;
import cn.daxpay.single.service.core.payment.adjust.service.RefundAdjustService;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 退款回调
 * @author xxm
 * @since 2024/1/24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RefundCallbackService {
    private final RefundOrderManager refundOrderManager;

    private final RefundAdjustService reflectionService;

    private final LockTemplate lockTemplate;

    /**
     * 退款回调统一处理
     */
    public void refundCallback() {

        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        // 加锁
        LockInfo lock = lockTemplate.lock("callback:refund:" + callbackInfo.getTradeNo(),10000, 200);
        if (Objects.isNull(lock)){
            callbackInfo.setCallbackStatus(PayCallbackStatusEnum.IGNORE).setErrorMsg("回调正在处理中，忽略本次回调请求");
            log.warn("订单号: {} 回调正在处理中，忽略本次回调请求", callbackInfo.getTradeNo());
            return;
        }
        try {
            // 获取退款单
            RefundOrder refundOrder = refundOrderManager.findByRefundNo(callbackInfo.getTradeNo()).orElse(null);
            // 退款单不存在,记录回调记录
            if (Objects.isNull(refundOrder)) {
                callbackInfo.setCallbackStatus(PayCallbackStatusEnum.NOT_FOUND).setErrorMsg("退款单不存在,记录回调记录");
                return;
            }
            // 退款单已经被处理, 记录回调记录
            if (!Objects.equals(RefundStatusEnum.PROGRESS.getCode(), refundOrder.getStatus())) {
                callbackInfo.setCallbackStatus(PayCallbackStatusEnum.IGNORE).setErrorMsg("退款单状态已处理,记录回调记录");
                return;
            }

            // 退款成功还是失败
            RefundAdjustParam param = new RefundAdjustParam()
                    .setOrder(refundOrder)
                    .setOutTradeNo(callbackInfo.getOutTradeNo())
                    .setSource(TradeAdjustSourceEnum.CALLBACK)
                    .setFinishTime(callbackInfo.getFinishTime());
            if (Objects.equals(RefundStatusEnum.SUCCESS.getCode(), callbackInfo.getOutStatus())) {
                param.setAdjustWay(RefundAdjustWayEnum.SUCCESS);
                String adjustNo = reflectionService.adjust(param);
                callbackInfo.setAdjustNo(adjustNo);
            }  else {
                param.setAdjustWay(RefundAdjustWayEnum.FAIL);
                String adjustNo = reflectionService.adjust(param);
                callbackInfo.setAdjustNo(adjustNo);
            }

        } finally {
            lockTemplate.releaseLock(lock);
        }
    }
}
