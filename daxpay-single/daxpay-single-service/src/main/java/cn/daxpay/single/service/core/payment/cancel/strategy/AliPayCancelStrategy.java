package cn.daxpay.single.service.core.payment.cancel.strategy;

import cn.daxpay.single.code.PayChannelEnum;
import cn.daxpay.single.service.core.channel.alipay.entity.AliPayConfig;
import cn.daxpay.single.service.core.channel.alipay.service.AliPayCloseService;
import cn.daxpay.single.service.core.channel.alipay.service.AliPayConfigService;
import cn.daxpay.single.service.func.AbsPayCancelStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 支付宝撤销策略
 * @author xxm
 * @since 2024/6/5
 */
@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Service
@RequiredArgsConstructor
public class AliPayCancelStrategy extends AbsPayCancelStrategy {

    private final AliPayConfigService alipayConfigService;

    private final AliPayCloseService aliPayCloseService;

    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.ALI;
    }

    /**
     * 关闭前的处理方式
     */
    @Override
    public void doBeforeCancelHandler() {
        AliPayConfig config = alipayConfigService.getConfig();
        alipayConfigService.initConfig(config);
    }

    /**
     * 关闭操作
     */
    @Override
    public void doCancelHandler() {
        aliPayCloseService.cancel(this.getOrder());
    }
}
