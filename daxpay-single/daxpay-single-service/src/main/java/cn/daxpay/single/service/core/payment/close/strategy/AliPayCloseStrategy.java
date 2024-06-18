package cn.daxpay.single.service.core.payment.close.strategy;

import cn.daxpay.single.core.code.PayChannelEnum;
import cn.daxpay.single.service.core.channel.alipay.entity.AliPayConfig;
import cn.daxpay.single.service.core.channel.alipay.service.AliPayCloseService;
import cn.daxpay.single.service.core.channel.alipay.service.AliPayConfigService;
import cn.daxpay.single.service.func.AbsPayCloseStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 *
 * @author xxm
 * @since 2023/12/30
 */
@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Service
@RequiredArgsConstructor
public class AliPayCloseStrategy extends AbsPayCloseStrategy {

    private final AliPayConfigService alipayConfigService;

    private final AliPayCloseService aliPayCloseService;

    private AliPayConfig alipayConfig;

    @Override
    public String getChannel() {
        return PayChannelEnum.ALI.getCode();
    }

    /**
     * 关闭前的处理方式
     */
    @Override
    public void doBeforeCloseHandler() {
        this.alipayConfig = alipayConfigService.getConfig();
    }

    /**
     * 关闭操作
     */
    @Override
    public void doCloseHandler() {
        aliPayCloseService.close(this.getOrder(), this.alipayConfig);
    }
}
