package cn.bootx.platform.daxpay.core.pay.strategy;

import cn.bootx.platform.common.core.util.BigDecimalUtil;
import cn.bootx.platform.daxpay.code.pay.PayChannelEnum;
import cn.bootx.platform.daxpay.core.pay.func.AbsPayStrategy;
import cn.bootx.platform.daxpay.core.payment.service.PaymentService;
import cn.bootx.platform.daxpay.core.channel.cash.service.CashService;
import cn.bootx.platform.daxpay.exception.payment.PayAmountAbnormalException;
import cn.bootx.platform.daxpay.param.pay.PayWayParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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

    private final CashService cashService;

    private final PaymentService paymentService;

    /**
     * 现金支付
     */
    @Override
    public PayChannelEnum getType() {
        return PayChannelEnum.CASH;
    }

    /**
     * 支付前检查
     */
    @Override
    public void doBeforePayHandler() {
        // 检查金额
        PayWayParam payMode = this.getPayWayParam();
        if (BigDecimalUtil.compareTo(payMode.getAmount(), BigDecimal.ZERO) < 1) {
            throw new PayAmountAbnormalException();
        }
    }

    /**
     * 支付操作
     */
    @Override
    public void doPayHandler() {
        cashService.pay(this.getPayWayParam(), this.getPayment(), this.getPayParam());
    }

    /**
     * 关闭本地支付记录
     */
    @Override
    public void doCloseHandler() {
        cashService.close(this.getPayment().getId());
    }

}
