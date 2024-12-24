package org.dromara.daxpay.channel.wechat.strategy.merchant;

import lombok.RequiredArgsConstructor;
import org.dromara.daxpay.channel.wechat.code.WechatPayCode;
import org.dromara.daxpay.channel.wechat.service.config.WechatPayConfigService;
import org.dromara.daxpay.channel.wechat.service.sync.refund.WechatRefundSyncV2Service;
import org.dromara.daxpay.channel.wechat.service.sync.refund.WechatRefundSyncV3Service;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.service.bo.sync.RefundSyncResultBo;
import org.dromara.daxpay.service.strategy.AbsSyncRefundOrderStrategy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 微信退款订单查询
 * @author xxm
 * @since 2024/7/25
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class WechatSyncRefundStrategy extends AbsSyncRefundOrderStrategy {

    private final WechatRefundSyncV2Service wechatRefundSyncV2Service;

    private final WechatRefundSyncV3Service wechatRefundSyncV3Service;

    private final WechatPayConfigService wechatPayConfigService;

    /**
     * 策略标识, 可以自行进行扩展
     *
     * @see ChannelEnum
     */
    @Override
    public String getChannel() {
        return ChannelEnum.WECHAT.getCode();
    }

    /**
     * 异步支付单与支付网关进行状态比对后的结果
     *
     */
    @Override
    public RefundSyncResultBo doSync() {
        var wechatPayConfig = wechatPayConfigService.getAndCheckConfig(false);
        if (Objects.equals(wechatPayConfig.getApiVersion(), WechatPayCode.API_V2)){
            return wechatRefundSyncV2Service.sync(this.getRefundOrder(), wechatPayConfig);
        } else {
            return wechatRefundSyncV3Service.sync(this.getRefundOrder(), wechatPayConfig);
        }
    }

}
