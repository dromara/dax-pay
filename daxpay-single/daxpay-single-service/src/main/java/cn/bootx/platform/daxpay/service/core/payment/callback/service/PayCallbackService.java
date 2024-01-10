package cn.bootx.platform.daxpay.service.core.payment.callback.service;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.daxpay.service.code.PayCallbackStatusEnum;
import cn.bootx.platform.daxpay.service.code.PayRepairSourceEnum;
import cn.bootx.platform.daxpay.service.code.PayRepairTypeEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.service.core.payment.callback.result.PayCallbackResult;
import cn.bootx.platform.daxpay.service.core.payment.repair.param.PayRepairParam;
import cn.bootx.platform.daxpay.service.core.payment.repair.service.PayRepairService;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.service.PayOrderService;
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

    private final PayOrderService payOrderService;

    private final PayRepairService payRepairService;

    /**
     * 统一回调处理
     * @param tradeStatus 支付状态
     */
    public PayCallbackResult callback(Long paymentId, String tradeStatus) {

        // 获取支付单
        PayOrder payOrder = payOrderService.findById(paymentId).orElse(null);

        // 支付单不存在,记录回调记录, TODO 取消支付网关的订单支付情况
        if (Objects.isNull(payOrder)) {
            return new PayCallbackResult().setStatus(PayCallbackStatusEnum.NOT_FOUND.getCode()).setMsg("支付单不存在,记录回调记录");
        }

        // 回调时间超出了支付单超时时间, 记录一下, 不做处理 TODO 这块应该把订单给当成正常结束给处理了,
        if (Objects.nonNull(payOrder.getExpiredTime())
                && LocalDateTimeUtil.ge(LocalDateTime.now(), payOrder.getExpiredTime())) {
            return new PayCallbackResult().setStatus(PayCallbackStatusEnum.TIMEOUT.getCode()).setMsg("回调时间超出了支付单支付有效时间");
        }

        // 成功状态
        if (Objects.equals(PayCallbackStatusEnum.SUCCESS.getCode(), tradeStatus)) {
            return this.success(payOrder);
        }
        else {
            // 失败状态
            return this.fail(payOrder);
        }
    }

    /**
     * 成功处理 使用补偿
     */
    private PayCallbackResult success(PayOrder payOrder) {
        PayCallbackResult result = new PayCallbackResult().setStatus(PayCallbackStatusEnum.SUCCESS.getCode());

        // 支付单已经被支付,不需要重复处理
        if (Objects.equals(payOrder.getStatus(), PayStatusEnum.SUCCESS.getCode())) {
            return result.setStatus(PayCallbackStatusEnum.IGNORE.getCode()).setMsg("支付单已经是支付成功状态,不进行处理");
        }

        // 支付单已被取消,记录回调记录 TODO 考虑不全, 需要做支付订单的取消处理
        if (!Objects.equals(payOrder.getStatus(), PayStatusEnum.PROGRESS.getCode())) {
            return result.setStatus(PayCallbackStatusEnum.FAIL.getCode()).setMsg("支付单不是待支付状态,记录回调记录");
        }

        // 执行支付完成修复逻辑
        PayRepairParam payRepairParam = new PayRepairParam()
                .setRepairSource(PayRepairSourceEnum.CALLBACK)
                .setRepairType(PayRepairTypeEnum.SUCCESS);
        payRepairService.repair(payOrder, payRepairParam);
        return result;
    }

    /**
     * 失败处理, 关闭并退款
     */
    private PayCallbackResult fail(PayOrder payOrder) {
        PayCallbackResult result = new PayCallbackResult().setStatus(PayStatusEnum.SUCCESS.getCode());

        // payment已被取消,记录回调记录,后期处理 TODO 考虑不完善, 后续优化
        if (!Objects.equals(payOrder.getStatus(), PayStatusEnum.PROGRESS.getCode())) {
            return result.setStatus(PayCallbackStatusEnum.IGNORE.getCode()).setMsg("支付单已经取消,记录回调记录");
        }

        // payment支付成功, 状态非法 TODO 考虑不完善, 后续优化
        if (!Objects.equals(payOrder.getStatus(), PayStatusEnum.SUCCESS.getCode())) {
            return result.setStatus(PayCallbackStatusEnum.FAIL.getCode()).setMsg("支付单状态非法,支付网关状态为失败,但支付单状态为已完成");
        }

        // 执行支付关闭修复逻辑
        PayRepairParam payRepairParam = new PayRepairParam()
                .setRepairSource(PayRepairSourceEnum.CALLBACK)
                .setRepairType(PayRepairTypeEnum.CLOSE_LOCAL);
        payRepairService.repair(payOrder, payRepairParam);
        return result;
    }

}
