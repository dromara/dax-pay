package cn.bootx.platform.daxpay.service.core.payment.transfer.strategy;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.service.core.channel.alipay.entity.AliPayConfig;
import cn.bootx.platform.daxpay.service.core.channel.alipay.service.AliPayConfigService;
import cn.bootx.platform.daxpay.service.core.channel.alipay.service.AliPayTransferService;
import cn.bootx.platform.daxpay.service.func.AbsTransferStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 支付宝转账测策略
 * @author xxm
 * @since 2024/3/21
 */
@Slf4j
@Service
@Scope(SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class AliPayTransferStrategy extends AbsTransferStrategy {

    private final AliPayConfigService payConfigService;

    private final AliPayTransferService aliPayTransferService;

    private AliPayConfig config;

    /**
     * 策略标识
     */
    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.ALI;
    }

    /**
     * 转账前操作
     */
    @Override
    public void doBeforeHandler() {
        this.config = payConfigService.getAndCheckConfig();
        payConfigService.initConfig(this.config);
    }

}
