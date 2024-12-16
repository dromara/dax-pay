package org.dromara.daxpay.channel.wechat.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.service.bo.allocation.AllocStartResultBo;
import org.dromara.daxpay.service.bo.allocation.AllocSyncResultBo;
import org.dromara.daxpay.service.strategy.AbsAllocationStrategy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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
public class WechatAllocationStrategy extends AbsAllocationStrategy {


    /**
     * 分账通道
     */
    @Override
    public String getChannel() {
        return ChannelEnum.WECHAT.getCode();
    }

    /**
     * 开始分账
     */
    @Override
    public AllocStartResultBo start() {
        return null;
    }

    /**
     *
     */
    @Override
    public void finish() {

    }

    /**
     * 同步状态
     */
    @Override
    public AllocSyncResultBo doSync() {
        return null;
    }

}
