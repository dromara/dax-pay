package cn.bootx.platform.daxpay.service.core.payment.refund.strategy;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.code.RefundStatusEnum;
import cn.bootx.platform.daxpay.param.channel.VoucherPayParam;
import cn.bootx.platform.daxpay.service.core.channel.voucher.entity.Voucher;
import cn.bootx.platform.daxpay.service.core.channel.voucher.service.VoucherPayService;
import cn.bootx.platform.daxpay.service.core.channel.voucher.service.VoucherQueryService;
import cn.bootx.platform.daxpay.service.core.channel.voucher.service.VoucherRecordService;
import cn.bootx.platform.daxpay.service.func.AbsRefundStrategy;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 储值卡支付退款
 * @author xxm
 * @since 2023/7/5
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class VoucherRefundStrategy extends AbsRefundStrategy {
    private final VoucherPayService voucherPayService;

    private final VoucherQueryService voucherQueryService;

    private final VoucherRecordService voucherRecordService;

    private Voucher voucher;

    /**
     * 策略标识
     *
     * @see PayChannelEnum
     */
    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.VOUCHER;
    }


    /**
     * 退款前对处理
     */
    @Override
    public void doBeforeRefundHandler() {
        // 从通道扩展参数中取出钱包参数
        if (!this.getPayOrder().isAsyncPay()) {
            String channelExtra = this.getPayChannelOrder().getChannelExtra();
            VoucherPayParam voucherPayParam = JSONUtil.toBean(channelExtra, VoucherPayParam.class);
            this.voucher = voucherQueryService.getVoucherByCardNo(voucherPayParam.getCardNo());
        }
    }


    /**
     * 退款
     */
    @Override
    public void doRefundHandler() {
        // 不包含异步支付
        if (!this.getPayOrder().isAsyncPay()){
            voucherPayService.refund(this.getRefundChannelParam().getAmount(), this.voucher);
            voucherRecordService.refund(this.getRefundChannelOrder(), this.getPayOrder().getTitle(), this.voucher);
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
