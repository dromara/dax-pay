package cn.daxpay.single.service.core.payment.pay.strategy;

import cn.daxpay.single.core.code.PayChannelEnum;
import cn.daxpay.single.core.exception.*;
import cn.daxpay.single.core.param.channel.WalletPayParam;
import cn.daxpay.single.service.code.WalletCode;
import cn.daxpay.single.service.core.channel.wallet.entity.Wallet;
import cn.daxpay.single.service.core.channel.wallet.entity.WalletConfig;
import cn.daxpay.single.service.core.channel.wallet.service.WalletConfigService;
import cn.daxpay.single.service.core.channel.wallet.service.WalletPayService;
import cn.daxpay.single.service.core.channel.wallet.service.WalletQueryService;
import cn.daxpay.single.service.func.AbsPayStrategy;
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

    private final WalletConfigService walletConfigService;

    private final WalletPayService walletPayService;

    private final WalletQueryService walletQueryService;

    private Wallet wallet;

    @Override
    public String getChannel() {
        return PayChannelEnum.WALLET.getCode();
    }

    /**
     * 支付前处理
     */
    @Override
    public void doBeforePayHandler() {
        WalletPayParam walletPayParam = new WalletPayParam();
        try {
            // 钱包参数验证
            Map<String, Object> channelParam = this.getPayParam().getExtraParam();
            if (CollUtil.isNotEmpty(channelParam)) {
                walletPayParam = BeanUtil.toBean(channelParam, WalletPayParam.class);
            }
        } catch (JSONException e) {
            throw new ParamValidationFailException("支付参数错误");
        }

        WalletConfig walletConfig = walletConfigService.getAndCheckConfig();

        // 获取钱包
        this.wallet = walletQueryService.getWallet(walletPayParam);
        if (Objects.isNull(this.wallet)){
            throw new TradeFaileException("钱包不存在");
        }
        // 是否被禁用
        if (Objects.equals(WalletCode.STATUS_FORBIDDEN, this.wallet.getStatus())) {
            throw new ConfigNotEnableException();
        }
        // 判断是否超过限额
        if (this.getPayParam().getAmount() > walletConfig.getLimitAmount()){
            throw new AmountExceedLimitException("钱包支付金额超过限额");
        }
        // 判断余额
        if (this.wallet.getBalance() < this.getPayParam().getAmount()) {
            throw new ConfigErrorException();
        }
        // 分账
        if (Objects.equals(this.getPayParam().getAllocation(),true)){
            throw new UnsupportedAbilityException("钱包支付不支持分账");
        }
    }

    /**
     * 支付操作
     */
    @Override
    public void doPayHandler() {
        walletPayService.pay(this.getPayParam().getAmount(), this.wallet);
    }
}
