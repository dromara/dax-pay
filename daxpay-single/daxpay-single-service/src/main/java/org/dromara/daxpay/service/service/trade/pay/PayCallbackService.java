package org.dromara.daxpay.service.service.trade.pay;

import cn.bootx.platform.core.util.DateTimeUtil;
import org.dromara.daxpay.core.enums.CallbackStatusEnum;
import org.dromara.daxpay.core.enums.PayStatusEnum;
import org.dromara.daxpay.service.common.context.CallbackLocal;
import org.dromara.daxpay.service.common.local.PaymentContextLocal;
import org.dromara.daxpay.service.dao.order.pay.PayOrderManager;
import org.dromara.daxpay.service.entity.order.pay.PayOrder;
import org.dromara.daxpay.service.service.allocation.AllocationService;
import org.dromara.daxpay.service.service.notice.MerchantNoticeService;
import org.dromara.daxpay.service.service.order.pay.PayOrderQueryService;
import org.dromara.daxpay.service.service.record.flow.TradeFlowRecordService;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 支付回调处理
 *
 * @author xxm
 * @since 2021/2/27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayCallbackService {

    private final PayOrderQueryService payOrderQueryService;

    private final LockTemplate lockTemplate;

    private final PayOrderManager payOrderManager;

    private final TradeFlowRecordService tradeFlowRecordService;

    private final MerchantNoticeService merchantNoticeService;
    private final AllocationService allocationService;

    /**
     * 支付统一回调处理
     */
    public void payCallback() {
        var callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        // 加锁
        LockInfo lock = lockTemplate.lock("callback:payment:" + callbackInfo.getTradeNo(),10000, 200);
        if (Objects.isNull(lock)){
            callbackInfo.setCallbackStatus(CallbackStatusEnum.IGNORE).setCallbackErrorMsg("回调正在处理中，忽略本次回调请求");
            log.warn("订单号: {} 回调正在处理中，忽略本次回调请求", callbackInfo.getTradeNo());
            return;
        }
        try {
            // 获取支付单
            PayOrder payOrder = payOrderQueryService.findByOrderNo(callbackInfo.getTradeNo()).orElse(null);
            // 本地支付单不存在,记录回调记录, TODO 需要补单或进行退款
            if (Objects.isNull(payOrder)) {
                callbackInfo.setCallbackStatus(CallbackStatusEnum.NOT_FOUND).setCallbackErrorMsg("支付单不存在,记录回调记录");
                return;
            }
            // 设置订单关联网关订单号
            payOrder.setOutOrderNo(callbackInfo.getOutTradeNo());

            // 成功状态
            if (Objects.equals(CallbackStatusEnum.SUCCESS.getCode(), callbackInfo.getTradeStatus())) {
                // 支付成功处理
                this.success(payOrder);
            } else {
                // 失败状态
                this.fail(payOrder);
            }
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 成功处理 将支付订单调整为支付成功状态
     */
    private void success(PayOrder payOrder) {
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        // 回调时间超出了支付单超时时间, 记录一下, 不做处理 TODO 考虑不全, 需要做退款or人工处理
        if (Objects.nonNull(payOrder.getExpiredTime())
                && DateTimeUtil.ge(LocalDateTime.now(), payOrder.getExpiredTime())) {
            callbackInfo.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg("回调时间超出了支付单支付有效时间");
            return;
        }
        // 支付单已经被支付,不需要重复处理
        if (Objects.equals(payOrder.getStatus(), PayStatusEnum.SUCCESS.getCode())) {
            callbackInfo.setCallbackStatus(CallbackStatusEnum.IGNORE).setCallbackErrorMsg("支付单已经是支付成功状态,不进行处理");
            return;
        }
        // 支付单已被取消,记录回调记录 TODO 考虑不全, 需要做退款or人工处理
        if (!Objects.equals(payOrder.getStatus(), PayStatusEnum.PROGRESS.getCode())) {
            callbackInfo.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg("支付单不是待支付状态,记录回调记录");
            return;
        }
        // 修改订单支付状态为成功
        payOrder.setStatus(PayStatusEnum.SUCCESS.getCode())
                .setPayTime(callbackInfo.getFinishTime())
                .setOutOrderNo(callbackInfo.getOutTradeNo())
                .setCloseTime(null);
        payOrderManager.updateById(payOrder);
        tradeFlowRecordService.savePay(payOrder);
        merchantNoticeService.registerPayNotice(payOrder);
        allocationService.registerAutoAlloc(payOrder);
    }

    /**
     * 失败处理, 使用调整策略将支付订单调整为关闭状态
     */
    private void fail(PayOrder payOrder) {
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        // payment已被取消,记录回调记录,后期处理 TODO 考虑不完善, 后续优化
        if (!Objects.equals(payOrder.getStatus(), PayStatusEnum.PROGRESS.getCode())) {
            callbackInfo.setCallbackStatus(CallbackStatusEnum.IGNORE).setCallbackErrorMsg("支付单已经取消,记录回调记录");
            return;
        }
        // payment支付成功, 状态非法 TODO 考虑不完善, 后续优化
        if (!Objects.equals(payOrder.getStatus(), PayStatusEnum.SUCCESS.getCode())) {
            callbackInfo.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg("支付单状态非法,支付网关状态为失败,但支付单状态为已完成");
            return;
        }
        // 执行支付关闭的调整逻辑
        // 执行策略的关闭方法
        payOrder.setStatus(PayStatusEnum.CLOSE.getCode())
                .setErrorMsg(callbackInfo.getTradeErrorMsg())
                .setCloseTime(LocalDateTime.now());
        payOrderManager.updateById(payOrder);
        merchantNoticeService.registerPayNotice(payOrder);
    }

}
