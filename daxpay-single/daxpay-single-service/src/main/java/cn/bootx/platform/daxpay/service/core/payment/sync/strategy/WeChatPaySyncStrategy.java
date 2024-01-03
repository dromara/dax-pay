package cn.bootx.platform.daxpay.service.core.payment.sync.strategy;

import cn.bootx.platform.daxpay.service.core.channel.wechat.entity.WeChatPayConfig;
import cn.bootx.platform.daxpay.service.core.channel.wechat.service.WeChatPayConfigService;
import cn.bootx.platform.daxpay.service.core.channel.wechat.service.WeChatPaySyncService;
import cn.bootx.platform.daxpay.service.core.payment.sync.result.GatewaySyncResult;
import cn.bootx.platform.daxpay.service.func.AbsPaySyncStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 微信支付同步
 * @author xxm
 * @since 2023/7/14
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class WeChatPaySyncStrategy extends AbsPaySyncStrategy {

    private final WeChatPayConfigService payConfigService;

    private final WeChatPaySyncService weChatPaySyncService;

    private WeChatPayConfig weChatPayConfig;

    /**
     * 异步支付单与支付网关进行状态比对
     */
    @Override
    public GatewaySyncResult doSyncStatus() {
        // 检查并获取微信支付配置
        this.initWeChatPayConfig();
        return weChatPaySyncService.syncPayStatus(this.getOrder().getId(), this.weChatPayConfig);
    }


    /**
     * 初始化微信支付
     */
    private void initWeChatPayConfig() {
        this.weChatPayConfig = payConfigService.getConfig();
    }
}
