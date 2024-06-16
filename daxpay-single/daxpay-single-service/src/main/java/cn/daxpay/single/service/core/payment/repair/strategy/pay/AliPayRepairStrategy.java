package cn.daxpay.single.service.core.payment.repair.strategy.pay;

import cn.daxpay.single.code.PayChannelEnum;
import cn.daxpay.single.service.core.channel.alipay.entity.AliPayConfig;
import cn.daxpay.single.service.core.channel.alipay.service.AliPayCloseService;
import cn.daxpay.single.service.core.channel.alipay.service.AliPayConfigService;
import cn.daxpay.single.service.func.AbsPayRepairStrategy;
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
    private final AliPayCloseService closeService;

    private final AliPayConfigService aliPayConfigService;

    private AliPayConfig config;

    /**
     * 策略标识
     */
     @Override
    public String getChannel() {
        return PayChannelEnum.ALI.getCode();
    }

    /**
     * 修复前处理
     */
    @Override
    public void doBeforeHandler() {
        this.config = aliPayConfigService.getConfig();
    }


    /**
     * 关闭本地支付和网关支付
     */
    @Override
    public void doCloseRemoteHandler() {
        closeService.close(this.getOrder(), this.config);
    }
}
