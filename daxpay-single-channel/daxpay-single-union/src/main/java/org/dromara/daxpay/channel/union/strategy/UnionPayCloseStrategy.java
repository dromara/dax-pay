package org.dromara.daxpay.channel.union.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.enums.CloseTypeEnum;
import org.dromara.daxpay.core.exception.UnsupportedAbilityException;
import org.dromara.daxpay.service.strategy.AbsPayCloseStrategy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 云闪付, 云闪付的关闭使用冲正
 * @author xxm
 * @since 2023/12/30
 */
@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Service
@RequiredArgsConstructor
public class UnionPayCloseStrategy extends AbsPayCloseStrategy {


    @Override
    public String getChannel() {
        return ChannelEnum.UNION_PAY.getCode();
    }

    /**
     * 关闭前的处理方式
     */
    @Override
    public void doBeforeCloseHandler() {
        throw new UnsupportedAbilityException("云闪付没有关闭订单功能!");
    }

    /**
     * 关闭操作
     */
    @Override
    public CloseTypeEnum doCloseHandler() {
        return null;
    }
}
