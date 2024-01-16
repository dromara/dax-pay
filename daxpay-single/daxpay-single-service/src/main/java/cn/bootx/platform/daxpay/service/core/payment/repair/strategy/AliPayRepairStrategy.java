package cn.bootx.platform.daxpay.service.core.payment.repair.strategy;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.channel.alipay.entity.AliPayConfig;
import cn.bootx.platform.daxpay.service.core.channel.alipay.service.AliPayCloseService;
import cn.bootx.platform.daxpay.service.core.channel.alipay.service.AliPayConfigService;
import cn.bootx.platform.daxpay.service.core.channel.alipay.service.AliPayOrderService;
import cn.bootx.platform.daxpay.service.core.order.pay.dao.PayOrderChannelManager;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrderChannel;
import cn.bootx.platform.daxpay.service.func.AbsPayRepairStrategy;
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
        // 将支付方式写入上下文
        PaymentContextLocal.get().getAsyncPayInfo().setPayWay(orderChannel.getPayWay());
        orderService.updateAsyncSuccess(this.getOrder(), orderChannel.getAmount());
    }

    /**
     * 取消支付
     */
    @Override
    public void doCloseLocalHandler() {
        orderService.updateClose(this.getOrder().getId());
    }


    /**
     * 关闭本地支付和网关支付
     */
    @Override
    public void doCloseGatewayHandler() {
        closeService.close(this.getOrder());
        this.doCloseLocalHandler();
    }

    /**
     * 退款
     */
    @Override
    public void doRefundHandler() {
        orderService.updateRefund(this.getOrder().getId(), 0);
    }
}
