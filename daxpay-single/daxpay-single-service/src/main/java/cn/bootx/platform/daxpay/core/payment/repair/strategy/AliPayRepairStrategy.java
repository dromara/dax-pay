package cn.bootx.platform.daxpay.core.payment.repair.strategy;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayRepairSourceEnum;
import cn.bootx.platform.daxpay.core.channel.alipay.entity.AliPayConfig;
import cn.bootx.platform.daxpay.core.channel.alipay.service.AliPayCloseService;
import cn.bootx.platform.daxpay.core.channel.alipay.service.AliPayOrderService;
import cn.bootx.platform.daxpay.core.channel.alipay.service.AliPayConfigService;
import cn.bootx.platform.daxpay.core.record.pay.dao.PayOrderChannelManager;
import cn.bootx.platform.daxpay.core.record.pay.entity.PayOrderChannel;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.func.AbsPayRepairStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 支付宝订单修复策略
 * @author xxm
 * @since 2023/12/27
 */
@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Service
@RequiredArgsConstructor
public class AliPayRepairStrategy extends AbsPayRepairStrategy {
    private final AliPayOrderService orderService;
    private final AliPayCloseService closeService;
    private final AliPayConfigService aliPayConfigService;

    private final PayOrderChannelManager orderChannelManager;

    /**
     * 修复前处理
     */
    @Override
    public void doBeforeHandler() {
        AliPayConfig config = aliPayConfigService.getConfig();
        aliPayConfigService.initConfig(config);
    }
    /**
     * 支付成功处理
     */
    @Override
    public void doSuccessHandler() {
        PayOrderChannel orderChannel = orderChannelManager.findByPaymentIdAndChannel(this.getOrder().getId(), PayChannelEnum.ALI.getCode())
                .orElseThrow(() -> new PayFailureException("支付宝订单不存在"));
        orderService.updateAsyncSuccess(this.getOrder(), orderChannel.getAmount());
    }

    /**
     * 取消支付
     */
    @Override
    public void doCloseHandler() {
        // 如果非同步出的订单取消状态, 则调用支付宝网关关闭订单
        if (this.getRepairSource() != PayRepairSourceEnum.SYNC){
            closeService.close(this.getOrder());
        }
        orderService.updateClose(this.getOrder().getId());
    }

    /**
     * 退款
     */
    @Override
    public void doRefundHandler() {
        orderService.updateRefund(this.getOrder().getId(), 0);
    }
}
