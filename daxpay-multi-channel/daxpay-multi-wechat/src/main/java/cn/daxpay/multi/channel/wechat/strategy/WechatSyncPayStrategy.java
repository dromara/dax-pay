package cn.daxpay.multi.channel.wechat.strategy;

import cn.daxpay.multi.channel.wechat.code.WechatPayCode;
import cn.daxpay.multi.channel.wechat.entity.config.WechatPayConfig;
import cn.daxpay.multi.channel.wechat.service.config.WechatPayConfigService;
import cn.daxpay.multi.channel.wechat.service.sync.pay.WeChatPaySyncV2Service;
import cn.daxpay.multi.channel.wechat.service.sync.pay.WeChatPaySyncV3Service;
import cn.daxpay.multi.core.enums.ChannelEnum;
import cn.daxpay.multi.service.bo.sync.PaySyncResultBo;
import cn.daxpay.multi.service.strategy.AbsSyncPayOrderStrategy;
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
public class WechatSyncPayStrategy extends AbsSyncPayOrderStrategy {

    private final WeChatPaySyncV3Service weChatPaySyncV3Service;

    private final WeChatPaySyncV2Service weChatPaySyncV2Service;
    private final WechatPayConfigService wechatPayConfigService;

    private WechatPayConfig config;


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
    public PaySyncResultBo doSync() {
        var wechatPayConfig = wechatPayConfigService.getWechatPayConfig();
        if (Objects.equals(wechatPayConfig.getApiVersion(), WechatPayCode.API_V2)){
            return weChatPaySyncV2Service.sync(getOrder(), wechatPayConfig);
        } else {
            return weChatPaySyncV3Service.sync(getOrder(), wechatPayConfig);
        }
    }
}
