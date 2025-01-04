package org.dromara.daxpay.channel.wechat.strategy.isv;

import org.dromara.daxpay.channel.wechat.code.WechatPayCode;
import org.dromara.daxpay.channel.wechat.entity.config.WechatPayConfig;
import org.dromara.daxpay.channel.wechat.service.allocation.WeChatPaySubAllocationV2Service;
import org.dromara.daxpay.channel.wechat.service.allocation.WeChatPaySubAllocationV3Service;
import org.dromara.daxpay.channel.wechat.service.config.WechatPayConfigService;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.service.bo.allocation.AllocStartResultBo;
import org.dromara.daxpay.service.bo.allocation.AllocSyncResultBo;
import org.dromara.daxpay.service.strategy.AbsAllocationStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 微信分账策略
 * @author xxm
 * @since 2024/12/9
 */
@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class WechatIsvAllocationStrategy extends AbsAllocationStrategy {
    private final WeChatPaySubAllocationV3Service weChatPayAllocationV3Service;
    private final WeChatPaySubAllocationV2Service weChatPayAllocationV2Service;
    private final WechatPayConfigService wechatPayConfigService;

    private WechatPayConfig config;

    /**
     * 分账通道
     */
    @Override
    public String getChannel() {
        return ChannelEnum.WECHAT_ISV.getCode();
    }

    @Override
    public void doBeforeHandler(){
        this.config = wechatPayConfigService.getAndCheckConfig(true);
    }

    /**
     * 开始分账
     */
    @Override
    public AllocStartResultBo start() {
        if (Objects.equals(config.getApiVersion(), WechatPayCode.API_V3)){
           return weChatPayAllocationV3Service.start(getOrder(), getDetails(), this.config);
        } else {
            return weChatPayAllocationV2Service.start(getOrder(), getDetails(), this.config);
        }
    }

    /**
     * 完结
     */
    @Override
    public void finish() {
        if (Objects.equals(config.getApiVersion(), WechatPayCode.API_V3)){
            weChatPayAllocationV3Service.finish(getOrder(), this.config);
        } else {
            weChatPayAllocationV2Service.finish(getOrder(), this.config);
        }
    }

    /**
     * 同步状态
     */
    @Override
    public AllocSyncResultBo doSync() {
        if (Objects.equals(config.getApiVersion(), WechatPayCode.API_V3)){
            return weChatPayAllocationV3Service.sync(getOrder(), this.getDetails(), this.config);
        } else {
            return weChatPayAllocationV2Service.sync(getOrder(), this.getDetails(), this.config);
        }
    }

}
