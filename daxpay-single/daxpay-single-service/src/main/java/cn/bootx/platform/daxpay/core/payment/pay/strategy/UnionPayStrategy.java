package cn.bootx.platform.daxpay.core.payment.pay.strategy;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.common.exception.ExceptionInfo;
import cn.bootx.platform.daxpay.func.AbsPayStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 云闪付
 *
 * @author xxm
 * @since 2022/3/8
 */
@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Service
@RequiredArgsConstructor
public class UnionPayStrategy extends AbsPayStrategy {

    @Override
    public PayChannelEnum getType() {
        return PayChannelEnum.UNION_PAY;
    }

    @Override
    public void doPayHandler() {

    }

    @Override
    public void doErrorHandler(ExceptionInfo exceptionInfo) {

    }

    @Override
    public void doCloseHandler() {

    }

}
