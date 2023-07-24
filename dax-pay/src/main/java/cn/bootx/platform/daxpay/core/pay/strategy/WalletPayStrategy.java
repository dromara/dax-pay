package cn.bootx.platform.daxpay.core.pay.strategy;

import cn.bootx.platform.common.core.util.BigDecimalUtil;
import cn.bootx.platform.daxpay.code.pay.PayChannelEnum;
import cn.bootx.platform.daxpay.code.paymodel.WalletCode;
import cn.bootx.platform.daxpay.core.channel.wallet.entity.Wallet;
import cn.bootx.platform.daxpay.core.channel.wallet.service.WalletPayService;
import cn.bootx.platform.daxpay.core.channel.wallet.service.WalletPaymentService;
import cn.bootx.platform.daxpay.core.channel.wallet.service.WalletQueryService;
import cn.bootx.platform.daxpay.core.pay.func.AbsPayStrategy;
import cn.bootx.platform.daxpay.exception.payment.PayFailureException;
import cn.bootx.platform.daxpay.exception.waller.WalletBannedException;
import cn.bootx.platform.daxpay.exception.waller.WalletLackOfBalanceException;
import cn.bootx.platform.daxpay.param.channel.wallet.WalletPayParam;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 钱包支付策略
 *
 * @author xxm
 * @since 2020/12/11
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class WalletPayStrategy extends AbsPayStrategy {

    private final WalletPaymentService walletPaymentService;

    private final WalletPayService walletPayService;

    private final WalletQueryService walletQueryService;

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
        WalletPayParam walletPayParam = new WalletPayParam();
        try {
            // 钱包参数验证
            String extraParamsJson = this.getPayWayParam().getExtraParamsJson();
            if (StrUtil.isNotBlank(extraParamsJson)) {
                walletPayParam = JSONUtil.toBean(extraParamsJson, WalletPayParam.class);
            }
        } catch (JSONException e) {
            throw new PayFailureException("支付参数错误");
        }
        // 获取钱包
        this.wallet = walletQueryService.getWallet(walletPayParam,getPayParam());
        if (Objects.isNull(this.wallet)){
            throw new PayFailureException("钱包不存在");
        }
        // 是否被禁用
        if (Objects.equals(WalletCode.STATUS_FORBIDDEN, this.wallet.getStatus())) {
            throw new WalletBannedException();
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
        // 异步支付方式时使用冻结方式
        if (this.getPayment().isAsyncPayMode()){
            walletPayService.freezeBalance(getPayWayParam().getAmount(), this.getPayment(), this.wallet);
        } else {
            walletPayService.pay(getPayWayParam().getAmount(), this.getPayment(), this.wallet);
        }
        walletPaymentService.savePayment(this.getPayment(), this.getPayParam(), this.getPayWayParam(), this.wallet);
    }

    /**
     * 成功
     */
    @Override
    public void doSuccessHandler() {
        if (this.getPayment().isAsyncPayMode()){
            walletPayService.paySuccess(this.getPayment().getId());
        }
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

}
