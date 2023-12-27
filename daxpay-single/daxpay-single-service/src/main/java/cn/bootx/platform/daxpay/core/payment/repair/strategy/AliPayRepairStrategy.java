package cn.bootx.platform.daxpay.core.payment.repair.strategy;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.core.channel.alipay.service.AliPayOrderService;
import cn.bootx.platform.daxpay.func.AbsPayRepairStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 支付宝支付单修复策略
 * @author xxm
 * @since 2023/12/27
 */
@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Service
@RequiredArgsConstructor
public class AliPayRepairStrategy extends AbsPayRepairStrategy {
    private final AliPayOrderService orderService;
    @Override
    public PayChannelEnum getType() {
        return PayChannelEnum.ALI;
    }

    /**
     * 支付成功处理
     */
    @Override
    public void successHandler() {
        orderService.updateAsyncSuccess(this.getOrder(), 0);
    }

    /**
     * 取消支付
     */
    @Override
    public void closeHandler() {
        orderService.updateClose(this.getOrder().getId());
    }

    /**
     * 退款
     */
    @Override
    public void refundHandler() {
        orderService.updatePayRefund(this.getOrder().getId(), 0);
    }
}
