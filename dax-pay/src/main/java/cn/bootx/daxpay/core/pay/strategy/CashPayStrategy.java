package cn.bootx.daxpay.core.pay.strategy;

import cn.bootx.platform.common.core.util.BigDecimalUtil;
import cn.bootx.daxpay.code.pay.PayChannelCode;
import cn.bootx.daxpay.code.pay.PayChannelEnum;
import cn.bootx.daxpay.core.pay.func.AbsPayStrategy;
import cn.bootx.daxpay.core.payment.service.PaymentService;
import cn.bootx.daxpay.core.paymodel.cash.service.CashService;
import cn.bootx.daxpay.exception.payment.PayAmountAbnormalException;
import cn.bootx.daxpay.param.pay.PayModeParam;
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
 * @date 2021/6/23
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
    public int getType() {
        return PayChannelCode.CASH;
    }

    /**
     * 支付前检查
     */
    @Override
    public void doBeforePayHandler() {
        // 检查金额
        PayModeParam payMode = this.getPayMode();
        if (BigDecimalUtil.compareTo(payMode.getAmount(), BigDecimal.ZERO) < 1) {
            throw new PayAmountAbnormalException();
        }
    }

    /**
     * 支付操作
     */
    @Override
    public void doPayHandler() {
        cashService.pay(this.getPayMode(), this.getPayment(), this.getPayParam());
    }

    /**
     * 关闭本地支付记录
     */
    @Override
    public void doCloseHandler() {
        cashService.close(this.getPayment().getId());
    }

    /**
     * 退款
     */
    @Override
    public void doRefundHandler() {
        cashService.refund(this.getPayment().getId(), this.getPayMode().getAmount());
        paymentService.updateRefundSuccess(this.getPayment(), this.getPayMode().getAmount(), PayChannelEnum.CASH);
    }

}
