package cn.bootx.platform.daxpay.core.pay.strategy;

import cn.bootx.platform.common.core.util.BigDecimalUtil;
import cn.bootx.platform.daxpay.code.pay.PayChannelEnum;
import cn.bootx.platform.daxpay.core.channel.wallet.entity.Wallet;
import cn.bootx.platform.daxpay.core.channel.wallet.service.WalletPayService;
import cn.bootx.platform.daxpay.core.channel.wallet.service.WalletPaymentService;
import cn.bootx.platform.daxpay.core.channel.wallet.service.WalletService;
import cn.bootx.platform.daxpay.core.pay.func.AbsPayStrategy;
import cn.bootx.platform.daxpay.core.payment.service.PaymentService;
import cn.bootx.platform.daxpay.exception.payment.PayFailureException;
import cn.bootx.platform.daxpay.exception.waller.WalletLackOfBalanceException;
import cn.bootx.platform.daxpay.param.channel.wallet.WalletPayParam;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 钱包支付策略
 *
 * @author xxm
 * @date 2020/12/11
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class WalletPayStrategy extends AbsPayStrategy {

    private final WalletPaymentService walletPaymentService;

    private final WalletPayService walletPayService;

    private final WalletService walletService;

    private final PaymentService paymentService;

    private Wallet wallet;

    @Override
    public PayChannelEnum getType() {
        return PayChannelEnum.WALLET;
    }

    /**
     * 支付前处理
     */
    @Override
    public void doBeforePayHandler() {
        try {
            // 支付宝参数验证
            String extraParamsJson = this.getPayWayParam().getExtraParamsJson();
            if (StrUtil.isNotBlank(extraParamsJson)) {

                WalletPayParam walletPayParam = JSONUtil.toBean(extraParamsJson, WalletPayParam.class);
                this.wallet = walletService.getNormalWalletById(walletPayParam.getWalletId());
            }
        }
        catch (JSONException e) {
            throw new PayFailureException("支付参数错误");
        }
        // 判断余额
        if (BigDecimalUtil.compareTo(this.wallet.getBalance(), getPayWayParam().getAmount()) < 0) {
            throw new WalletLackOfBalanceException();
        }
    }

    /**
     * 支付操作
     */
    @Override
    public void doPayHandler() {
        walletPayService.pay(getPayWayParam().getAmount(), this.getPayment(), this.wallet);
        walletPaymentService.savePayment(this.getPayment(), this.getPayParam(), this.getPayWayParam(), this.wallet);
    }

    /**
     * 成功
     */
    @Override
    public void doSuccessHandler() {
        walletPaymentService.updateSuccess(this.getPayment().getId());
    }

    /**
     * 取消支付并返还金额
     */
    @Override
    public void doCloseHandler() {
        walletPayService.close(this.getPayment().getId());
        walletPaymentService.updateClose(this.getPayment().getId());
    }

    /**
     * 退款
     */
    @Override
    public void doRefundHandler() {
        walletPayService.refund(this.getPayment().getId(), this.getPayWayParam().getAmount());
        walletPaymentService.updateRefund(this.getPayment().getId(), this.getPayWayParam().getAmount());
        paymentService.updateRefundSuccess(this.getPayment(), this.getPayWayParam().getAmount(), PayChannelEnum.WALLET);
    }

}
