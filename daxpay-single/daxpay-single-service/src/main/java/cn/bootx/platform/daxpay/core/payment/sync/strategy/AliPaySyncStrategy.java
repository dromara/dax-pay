package cn.bootx.platform.daxpay.core.payment.sync.strategy;


import cn.bootx.platform.daxpay.core.channel.alipay.entity.AlipayConfig;
import cn.bootx.platform.daxpay.core.channel.alipay.service.AlipayConfigService;
import cn.bootx.platform.daxpay.core.channel.alipay.service.AlipaySyncService;
import cn.bootx.platform.daxpay.func.AbsPaySyncStrategy;
import cn.bootx.platform.daxpay.core.payment.sync.result.GatewaySyncResult;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 支付宝支付同步
 * @author xxm
 * @since 2023/7/14
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AliPaySyncStrategy extends AbsPaySyncStrategy {

    private final AlipayConfigService alipayConfigService;

    private final AlipaySyncService alipaySyncService;

    /**
     * 异步支付单与支付网关进行状态比对
     */
    @Override
    public GatewaySyncResult doSyncStatus() {
        this.initAlipayConfig();
        return alipaySyncService.syncPayStatus(this.getOrder().getId());
    }

    /**
     * 初始化支付宝配置信息
     */
    private void initAlipayConfig() {
        // 检查并获取支付宝支付配置
        AlipayConfig config = alipayConfigService.getConfig();
        alipayConfigService.initConfig(config);
    }
}
