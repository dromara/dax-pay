package cn.bootx.platform.daxpay.service.core.payment.close.strategy;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.service.core.channel.cash.service.CashRecordService;
import cn.bootx.platform.daxpay.service.func.AbsPayCloseStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 现金支付关闭策略
 * @author xxm
 * @since 2023/12/30
 */
@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Service
@RequiredArgsConstructor
public class CashPayCloseStrategy extends AbsPayCloseStrategy {
    private final CashRecordService cashRecordService;

    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.CASH;
    }

    /**
     * 关闭操作
     */
    @Override
    public void doCloseHandler() {
        cashRecordService.payClose(this.getChannelOrder(),this.getOrder().getTitle());
    }
}
