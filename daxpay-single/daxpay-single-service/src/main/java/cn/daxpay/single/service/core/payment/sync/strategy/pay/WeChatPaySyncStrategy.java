package cn.daxpay.single.service.core.payment.sync.strategy.pay;

import cn.daxpay.single.code.PayChannelEnum;
import cn.daxpay.single.service.core.channel.wechat.entity.WeChatPayConfig;
import cn.daxpay.single.service.core.channel.wechat.service.WeChatPayConfigService;
import cn.daxpay.single.service.core.channel.wechat.service.WeChatPaySyncService;
import cn.daxpay.single.service.core.payment.sync.result.PaySyncResult;
import cn.daxpay.single.service.func.AbsPaySyncStrategy;
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
     * 策略标识
     */
    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.WECHAT;
    }

    /**
     * 异步支付单与支付网关进行状态比对
     */
    @Override
    public PaySyncResult doSyncStatus() {
        // 检查并获取微信支付配置
        this.initWeChatPayConfig();
        return weChatPaySyncService.syncPayStatus(this.getOrder(), this.weChatPayConfig);
    }


    /**
     * 初始化微信支付
     */
    private void initWeChatPayConfig() {
        this.weChatPayConfig = payConfigService.getConfig();
    }
}
