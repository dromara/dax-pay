package org.dromara.daxpay.channel.wechat.strategy.sub;

import org.dromara.daxpay.channel.wechat.code.WechatPayCode;
import org.dromara.daxpay.channel.wechat.service.payment.config.WechatPayConfigService;
import org.dromara.daxpay.channel.wechat.service.payment.sync.pay.WechatSubPaySyncV2Service;
import org.dromara.daxpay.channel.wechat.service.payment.sync.pay.WechatSubPaySyncV3Service;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.service.pay.bo.sync.PaySyncResultBo;
import org.dromara.daxpay.service.pay.strategy.AbsSyncPayOrderStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 微信支付订单同步
 * @author xxm
 * @since 2023/7/14
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class WechatSubSyncPayStrategy extends AbsSyncPayOrderStrategy {

    private final WechatSubPaySyncV3Service weChatPaySyncV3Service;

    private final WechatSubPaySyncV2Service weChatPaySyncV2Service;

    private final WechatPayConfigService wechatPayConfigService;


    /**
     * 策略标识, 可以自行进行扩展
     *
     * @see ChannelEnum
     */
    @Override
    public String getChannel() {
        return ChannelEnum.WECHAT_ISV.getCode();
    }



    /**
     * 异步支付单与支付网关进行状态比对后的结果
     *
     */
    @Override
    public PaySyncResultBo doSync() {
        var wechatPayConfig = wechatPayConfigService.getAndCheckConfig(true);
        if (Objects.equals(wechatPayConfig.getApiVersion(), WechatPayCode.API_V2)){
            return weChatPaySyncV2Service.sync(getOrder(), wechatPayConfig);
        } else {
            return weChatPaySyncV3Service.sync(getOrder(), wechatPayConfig);
        }
    }
}
