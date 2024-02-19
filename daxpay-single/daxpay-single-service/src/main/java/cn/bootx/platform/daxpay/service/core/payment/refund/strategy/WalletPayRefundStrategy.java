package cn.bootx.platform.daxpay.service.core.payment.refund.strategy;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.code.RefundStatusEnum;
import cn.bootx.platform.daxpay.param.channel.WalletPayParam;
import cn.bootx.platform.daxpay.service.core.channel.wallet.entity.Wallet;
import cn.bootx.platform.daxpay.service.core.channel.wallet.service.WalletPayService;
import cn.bootx.platform.daxpay.service.core.channel.wallet.service.WalletQueryService;
import cn.bootx.platform.daxpay.service.core.channel.wallet.service.WalletRecordService;
import cn.bootx.platform.daxpay.service.func.AbsRefundStrategy;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 钱包支付退款
 * @author xxm
 * @since 2023/7/5
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class WalletPayRefundStrategy extends AbsRefundStrategy {

    private final WalletPayService walletPayService;

    private final WalletRecordService walletRecordService;

    private final WalletQueryService walletQueryService;

    private Wallet wallet;

    /**
     * 策略标识
     *
     * @see PayChannelEnum
     */
    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.WALLET;
    }

    /**
     * 退款前对处理
     */
    @Override
    public void doBeforeRefundHandler() {
        // 从通道扩展参数中取出钱包参数
        if (!this.getPayOrder().isAsyncPay()) {
            String channelExtra = this.getPayChannelOrder().getChannelExtra();
            WalletPayParam walletPayParam = JSONUtil.toBean(channelExtra, WalletPayParam.class);
            this.wallet = walletQueryService.getWallet(walletPayParam);
        }
    }

    /**
     * 退款操作
     */
    @Override
    public void doRefundHandler() {
        // 不包含异步支付
        if (!this.getPayOrder().isAsyncPay()){
            walletPayService.refund(this.wallet, this.getRefundChannelParam().getAmount());
            walletRecordService.refund(this.getRefundChannelOrder(), this.wallet);
        }
    }

    /**
     * 退款发起成功操作, 异步支付通道需要进行重写
     */
    @Override
    public void doSuccessHandler() {
        // 包含异步支付, 变更状态到退款中
        if (this.getPayOrder().isAsyncPay()) {
            this.getPayChannelOrder().setStatus(PayStatusEnum.REFUNDING.getCode());
            this.getRefundChannelOrder().setStatus(RefundStatusEnum.PROGRESS.getCode());
        } else{
            // 同步支付, 直接标识状态为退款完成
            super.doSuccessHandler();
        }
    }
}
