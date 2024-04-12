package cn.bootx.platform.daxpay.service.core.payment.sync.strategy.allocation;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.service.core.channel.wechat.entity.WeChatPayConfig;
import cn.bootx.platform.daxpay.service.core.channel.wechat.service.WeChatPayAllocationService;
import cn.bootx.platform.daxpay.service.core.channel.wechat.service.WeChatPayConfigService;
import cn.bootx.platform.daxpay.service.func.AbsAllocationSyncStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 微信分账状态同步策略
 * @author xxm
 * @since 2024/4/12
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class WechatPayAllocationSyncStrategy extends AbsAllocationSyncStrategy {

    private final WeChatPayAllocationService weChatPayAllocationService;

    private final WeChatPayConfigService weChatPayConfigService;


    /**
     * 同步状态
     */
    @Override
    public void doSyncStatus() {
        WeChatPayConfig wechatPayConfig =  weChatPayConfigService.getAndCheckConfig();
        weChatPayAllocationService.queryStatus(this.getAllocationOrder(),wechatPayConfig);
    }

    /**
     * 策略标识
     */
    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.WECHAT;
    }
}
