package cn.bootx.platform.daxpay.core.payment.close.strategy;

import cn.bootx.platform.daxpay.core.channel.alipay.entity.AliPayConfig;
import cn.bootx.platform.daxpay.core.channel.alipay.service.AliPayCloseService;
import cn.bootx.platform.daxpay.core.channel.alipay.service.AliPayOrderService;
import cn.bootx.platform.daxpay.core.channel.alipay.service.AliPayConfigService;
import cn.bootx.platform.daxpay.func.AbsPayCloseStrategy;
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

    private final AliPayOrderService aliPayOrderService;

    private final AliPayCloseService aliPayCloseService;

    /**
     * 关闭前的处理方式
     */
    @Override
    public void doBeforeCloseHandler() {
        AliPayConfig config = alipayConfigService.getConfig();
        alipayConfigService.initConfig(config);
    }

    /**
     * 关闭操作
     */
    @Override
    public void doCloseHandler() {
        aliPayCloseService.close(this.getOrder());
        aliPayOrderService.updateClose(this.getOrder().getId());
    }
}
