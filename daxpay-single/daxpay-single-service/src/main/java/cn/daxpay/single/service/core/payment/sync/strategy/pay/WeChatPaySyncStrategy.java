package cn.daxpay.single.service.core.payment.sync.strategy.pay;

import cn.daxpay.single.core.code.PayChannelEnum;
import cn.daxpay.single.service.core.channel.wechat.entity.WeChatPayConfig;
import cn.daxpay.single.service.core.channel.wechat.service.WeChatPayConfigService;
import cn.daxpay.single.service.core.channel.wechat.service.WeChatPaySyncService;
import cn.daxpay.single.service.core.payment.sync.result.PayRemoteSyncResult;
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

    /**
     * 策略标识
     */
    @Override
    public String getChannel() {
        return PayChannelEnum.WECHAT.getCode();
    }

    /**
     * 异步支付单与支付网关进行状态比对
     */
    @Override
    public PayRemoteSyncResult doSyncStatus() {
        WeChatPayConfig config = payConfigService.getConfig();
        return weChatPaySyncService.syncPayStatus(this.getOrder(), config);
    }
}
