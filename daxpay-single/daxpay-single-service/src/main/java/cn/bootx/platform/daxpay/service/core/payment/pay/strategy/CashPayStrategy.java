package cn.bootx.platform.daxpay.service.core.payment.pay.strategy;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.exception.pay.PayAmountAbnormalException;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.param.pay.PayChannelParam;
import cn.bootx.platform.daxpay.service.core.channel.cash.entity.CashConfig;
import cn.bootx.platform.daxpay.service.core.channel.cash.service.CashPayConfigService;
import cn.bootx.platform.daxpay.service.core.channel.cash.service.CashRecordService;
import cn.bootx.platform.daxpay.service.func.AbsPayStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 现金支付
 *
 * @author xxm
 * @since 2021/6/23
 */
@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Service
@RequiredArgsConstructor
public class CashPayStrategy extends AbsPayStrategy {

    private final CashRecordService cashRecordService;

    private final CashPayConfigService cashPayConfigService;

    /**
     * 现金支付
     */
    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.CASH;
    }

    /**
     * 支付前检查
     */
    @Override
    public void doBeforePayHandler() {
        CashConfig config = cashPayConfigService.getAndCheckConfig();
        // 检查金额
        PayChannelParam payChannelParam = this.getPayChannelParam();
        if (payChannelParam.getAmount() <= 0) {
            throw new PayAmountAbnormalException();
        }
        // 支付金额是否超限
        if (payChannelParam.getAmount() > config.getSingleLimit()) {
            throw new PayFailureException("现金支付金额超过限制");
        }
    }

    /**
     * 支付操作
     */
    @Override
    public void doPayHandler() {
        cashRecordService.pay(this.getChannelOrder(),this.getOrder().getTitle());
    }
}
