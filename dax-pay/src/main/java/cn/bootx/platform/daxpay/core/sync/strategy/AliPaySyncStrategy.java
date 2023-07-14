package cn.bootx.platform.daxpay.core.sync.strategy;


import cn.bootx.platform.daxpay.core.channel.alipay.dao.AlipayConfigManager;
import cn.bootx.platform.daxpay.core.channel.alipay.entity.AlipayConfig;
import cn.bootx.platform.daxpay.core.channel.alipay.service.AlipayConfigService;
import cn.bootx.platform.daxpay.core.channel.alipay.service.AlipaySyncService;
import cn.bootx.platform.daxpay.core.sync.func.AbsPaySyncStrategy;
import cn.bootx.platform.daxpay.core.sync.result.PaySyncResult;
import cn.bootx.platform.daxpay.exception.payment.PayFailureException;
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

    private final AlipayConfigManager alipayConfigManager;

    private final AlipaySyncService alipaySyncService;

    private final AlipayConfigService alipayConfigService;

    private AlipayConfig alipayConfig;

    /**
     * 异步支付单与支付网关进行状态比对
     */
    @Override
    public PaySyncResult doSyncPayStatusHandler() {
        this.initAlipayConfig(this.getPayment().getMchAppCode());
        return alipaySyncService.syncPayStatus(this.getPayment());
    }

    /**
     * 初始化支付宝配置信息
     */
    private void initAlipayConfig(String mchAppCode) {
        // 检查并获取支付宝支付配置
        this.alipayConfig = alipayConfigManager.findByMchAppCode(mchAppCode)
                .orElseThrow(() -> new PayFailureException("支付配置不存在"));
        alipayConfigService.initApiConfig(this.alipayConfig);
    }
}
