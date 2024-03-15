package cn.bootx.platform.daxpay.service.core.payment.refund.strategy;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.code.RefundStatusEnum;
import cn.bootx.platform.daxpay.param.channel.WalletPayParam;
import cn.bootx.platform.daxpay.service.core.channel.wallet.entity.Wallet;
import cn.bootx.platform.daxpay.service.core.channel.wallet.service.WalletPayService;
import cn.bootx.platform.daxpay.service.core.channel.wallet.service.WalletQueryService;
import cn.bootx.platform.daxpay.service.core.channel.wallet.service.WalletRecordService;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayChannelOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.func.AbsRefundStrategy;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 钱包支付退款
 * @author xxm
 * @since 2023/7/5
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class WalletRefundStrategy extends AbsRefundStrategy {

    private final WalletPayService walletPayService;

    private final WalletRecordService walletRecordService;

    private final WalletQueryService walletQueryService;

    private WalletPayParam walletPayParam;

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
    public void initRefundParam(PayOrder payOrder, PayChannelOrder payChannelOrder) {
        // 先设置参数
        super.initRefundParam(payOrder, payChannelOrder);
        // 从通道扩展参数中取出钱包参数
        String channelExtra = this.getPayChannelOrder().getChannelExtra();
        this.walletPayParam = JSONUtil.toBean(channelExtra, WalletPayParam.class);
    }

    /**
     * 退款前对处理
     */
    @Override
    public void doBeforeRefundHandler() {
        // 如果任务执行完成, 则跳过
        if (Objects.equals(this.getRefundChannelOrder().getStatus(), RefundStatusEnum.SUCCESS.getCode())){
            return;
        }
        //
        if (!this.getPayOrder().isAsyncPay()) {
            this.wallet = walletQueryService.getWallet(this.walletPayParam);
        }
    }

    /**
     * 退款操作
     */
    @Override
    public void doRefundHandler() {
        // 如果任务执行完成, 则跳过
        if (Objects.equals(this.getRefundChannelOrder().getStatus(), RefundStatusEnum.SUCCESS.getCode())){
            return;
        }
        // 不包含异步支付, 则只在支付订单中进行扣减, 等待异步退款完成, 再进行退款
        if (!this.getPayOrder().isAsyncPay()){
            walletPayService.refund(this.wallet, this.getRefundChannelParam().getAmount());
            walletRecordService.refund(this.getRefundChannelOrder(), this.getPayOrder().getTitle(), this.wallet);
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
