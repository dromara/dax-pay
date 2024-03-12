package cn.bootx.platform.daxpay.service.core.payment.callback.service;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.service.code.PayCallbackStatusEnum;
import cn.bootx.platform.daxpay.service.code.PayRepairWayEnum;
import cn.bootx.platform.daxpay.service.common.context.CallbackLocal;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.service.PayOrderQueryService;
import cn.bootx.platform.daxpay.service.core.payment.repair.result.PayRepairResult;
import cn.bootx.platform.daxpay.service.core.payment.repair.service.PayRepairService;
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

    private final PayRepairService payRepairService;

    private final LockTemplate lockTemplate;

    /**
     * 支付统一回调处理
     */
    public void payCallback() {
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        // 加锁
        LockInfo lock = lockTemplate.lock("callback:payment:" + callbackInfo.getOrderId(),10000, 200);
        if (Objects.isNull(lock)){
            callbackInfo.setCallbackStatus(PayCallbackStatusEnum.IGNORE).setMsg("回调正在处理中，忽略本次回调请求");
            log.warn("订单号: {} 回调正在处理中，忽略本次回调请求", callbackInfo.getOrderId());
            return;
        }
        try {
            // 获取支付单
            PayOrder payOrder = payOrderQueryService.findById(callbackInfo.getOrderId()).orElse(null);
            // 本地支付单不存在,记录回调记录, TODO 需要补单或进行退款
            if (Objects.isNull(payOrder)) {
                callbackInfo.setCallbackStatus(PayCallbackStatusEnum.NOT_FOUND).setMsg("支付单不存在,记录回调记录");
                return;
            }
            // 设置订单关联网关订单号
            payOrder.setGatewayOrderNo(callbackInfo.getGatewayOrderNo());

            // 成功状态
            if (Objects.equals(PayCallbackStatusEnum.SUCCESS.getCode(), callbackInfo.getGatewayStatus())) {
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
     * 成功处理 使用修复策略将支付订单调整为支付成功状态
     */
    private void success(PayOrder payOrder) {
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        // 回调时间超出了支付单超时时间, 记录一下, 不做处理 TODO 考虑不全, 需要做退款or人工处理
        if (Objects.nonNull(payOrder.getExpiredTime())
                && LocalDateTimeUtil.ge(LocalDateTime.now(), payOrder.getExpiredTime())) {
            callbackInfo.setCallbackStatus(PayCallbackStatusEnum.EXCEPTION).setMsg("回调时间超出了支付单支付有效时间");
            return;
        }
        // 支付单已经被支付,不需要重复处理
        if (Objects.equals(payOrder.getStatus(), PayStatusEnum.SUCCESS.getCode())) {
            callbackInfo.setCallbackStatus(PayCallbackStatusEnum.IGNORE).setMsg("支付单已经是支付成功状态,不进行处理");
            return;
        }
        // 支付单已被取消,记录回调记录 TODO 考虑不全, 需要做退款or人工处理
        if (!Objects.equals(payOrder.getStatus(), PayStatusEnum.PROGRESS.getCode())) {
            callbackInfo.setCallbackStatus(PayCallbackStatusEnum.EXCEPTION).setMsg("支付单不是待支付状态,记录回调记录");
            return;
        }
        // 设置支付成功时间
        PaymentContextLocal.get().getRepairInfo().setFinishTime(callbackInfo.getFinishTime());
        // 执行支付完成修复逻辑
        PayRepairResult repair = payRepairService.repair(payOrder, PayRepairWayEnum.PAY_SUCCESS);
        callbackInfo.setPayRepairNo(repair.getRepairNo());
    }

    /**
     * 失败处理, 使用修复策略将支付订单调整为关闭状态
     */
    private void fail(PayOrder payOrder) {
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        // payment已被取消,记录回调记录,后期处理 TODO 考虑不完善, 后续优化
        if (!Objects.equals(payOrder.getStatus(), PayStatusEnum.PROGRESS.getCode())) {
            callbackInfo.setCallbackStatus(PayCallbackStatusEnum.IGNORE).setMsg("支付单已经取消,记录回调记录");
            return;
        }
        // payment支付成功, 状态非法 TODO 考虑不完善, 后续优化
        if (!Objects.equals(payOrder.getStatus(), PayStatusEnum.SUCCESS.getCode())) {
            callbackInfo.setCallbackStatus(PayCallbackStatusEnum.EXCEPTION).setMsg("支付单状态非法,支付网关状态为失败,但支付单状态为已完成");
            return;
        }
        // 执行支付关闭修复逻辑
        PayRepairResult repair = payRepairService.repair(payOrder, PayRepairWayEnum.CLOSE_LOCAL);
        callbackInfo.setPayRepairNo(repair.getRepairNo());
    }

}
