package cn.bootx.platform.daxpay.service.core.payment.repair.strategy.refund;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.param.channel.VoucherPayParam;
import cn.bootx.platform.daxpay.service.core.channel.voucher.entity.Voucher;
import cn.bootx.platform.daxpay.service.core.channel.voucher.service.VoucherPayService;
import cn.bootx.platform.daxpay.service.core.channel.voucher.service.VoucherQueryService;
import cn.bootx.platform.daxpay.service.core.channel.voucher.service.VoucherRecordService;
import cn.bootx.platform.daxpay.service.func.AbsRefundRepairStrategy;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 *
 * @author xxm
 * @since 2024/1/30
 */
@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Service
@RequiredArgsConstructor
public class VoucherRefundRepairStrategy extends AbsRefundRepairStrategy {
    private final VoucherQueryService voucherQueryService;

    private final VoucherRecordService voucherRecordService;

    private final VoucherPayService voucherPayService;

    private Voucher voucher;

    /**
     * 策略标识
     */
    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.VOUCHER;
    }

    /**
     * 修复前处理
     */
    @Override
    public void doBeforeHandler() {
        String channelExtra = this.getPayChannelOrder().getChannelExtra();
        VoucherPayParam params = JSONUtil.toBean(channelExtra, VoucherPayParam.class);
        this.voucher = voucherQueryService.getVoucherByCardNo(params.getCardNo());
    }

    /**
     * 退款成功修复
     */
    @Override
    public void doSuccessHandler() {
        voucherPayService.refund(this.getPayChannelOrder().getAmount(), this.voucher);
        voucherRecordService.refund(this.getRefundChannelOrder(), this.voucher);
    }
}
