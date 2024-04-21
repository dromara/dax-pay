package cn.bootx.platform.daxpay.service.core.payment.repair.strategy.refund;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.code.RefundStatusEnum;
import cn.bootx.platform.daxpay.param.channel.WalletPayParam;
import cn.bootx.platform.daxpay.service.core.channel.wallet.entity.Wallet;
import cn.bootx.platform.daxpay.service.core.channel.wallet.service.WalletPayService;
import cn.bootx.platform.daxpay.service.core.channel.wallet.service.WalletQueryService;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayChannelOrder;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.RefundChannelOrder;
import cn.bootx.platform.daxpay.service.func.AbsRefundRepairStrategy;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 钱包退款订单修复策略
 * @author xxm
 * @since 2024/1/30
 */
@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Service
@RequiredArgsConstructor
public class WalletRefundRepairStrategy extends AbsRefundRepairStrategy {

    private final WalletPayService walletPayService;

    private final WalletRecordService walletRecordService;

    private final WalletQueryService walletQueryService;

    private Wallet wallet;

    /**
     * 策略标识
     */
    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.WALLET;
    }

    /**
     * 修复前处理
     */
    @Override
    public void doBeforeHandler() {
        // 从通道扩展参数中取出钱包参数
        String channelExtra = this.getPayChannelOrder().getChannelExtra();
        WalletPayParam walletPayParam = JSONUtil.toBean(channelExtra, WalletPayParam.class);
        this.wallet = walletQueryService.getWallet(walletPayParam);
    }

    /**
     * 退款成功修复, 只有异步支付的退款才会执行到这
     */
    @Override
    public void doSuccessHandler() {
        PayChannelOrder payChannelOrder = this.getPayChannelOrder();
        RefundChannelOrder refundChannelOrder = this.getRefundChannelOrder();
        // 判断是全部退款还是部分退款
        if (Objects.equals(payChannelOrder.getRefundableBalance(), 0)){
            //全部退款
            payChannelOrder.setStatus(PayStatusEnum.REFUNDED.getCode());
        } else {
            // 部分退款
            payChannelOrder.setStatus(PayStatusEnum.PARTIAL_REFUND.getCode());
        }
        refundChannelOrder.setStatus(RefundStatusEnum.SUCCESS.getCode());
        // 退款真正执行和保存
        walletPayService.refund(this.wallet, this.getRefundChannelOrder().getAmount());
        walletRecordService.refund(this.getRefundChannelOrder(), this.getPayOrder().getTitle(), this.wallet);
    }
}
