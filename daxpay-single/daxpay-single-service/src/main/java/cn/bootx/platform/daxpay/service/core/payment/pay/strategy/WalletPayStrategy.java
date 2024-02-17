package cn.bootx.platform.daxpay.service.core.payment.pay.strategy;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.exception.waller.WalletBannedException;
import cn.bootx.platform.daxpay.exception.waller.WalletLackOfBalanceException;
import cn.bootx.platform.daxpay.param.channel.WalletPayParam;
import cn.bootx.platform.daxpay.service.code.WalletCode;
import cn.bootx.platform.daxpay.service.core.channel.wallet.entity.Wallet;
import cn.bootx.platform.daxpay.service.core.channel.wallet.service.WalletPayService;
import cn.bootx.platform.daxpay.service.core.channel.wallet.service.WalletQueryService;
import cn.bootx.platform.daxpay.service.func.AbsPayStrategy;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;
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


    private final WalletPayService walletPayService;

    private final WalletQueryService walletQueryService;

    private Wallet wallet;

    @Override
    public PayChannelEnum getChannel() {
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
            Map<String, Object> channelParam = this.getPayChannelParam().getChannelParam();
            if (CollUtil.isNotEmpty(channelParam)) {
                walletPayParam = BeanUtil.toBean(channelParam, WalletPayParam.class);
            }
        } catch (JSONException e) {
            throw new PayFailureException("支付参数错误");
        }
        // 获取钱包
        this.wallet = walletQueryService.getWallet(walletPayParam);
        if (Objects.isNull(this.wallet)){
            throw new PayFailureException("钱包不存在");
        }
        // 是否被禁用
        if (Objects.equals(WalletCode.STATUS_FORBIDDEN, this.wallet.getStatus())) {
            throw new WalletBannedException();
        }
        // 判断余额
        if (this.wallet.getBalance() < getPayChannelParam().getAmount()) {
            throw new WalletLackOfBalanceException();
        }
    }

    /**
     * 支付操作
     */
    @Override
    public void doPayHandler() {
        // 异步支付方式时使用冻结方式
        walletPayService.pay(getPayChannelParam().getAmount(), this.wallet);
    }
}
