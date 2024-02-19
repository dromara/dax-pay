package cn.bootx.platform.daxpay.service.core.payment.close.strategy;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.param.channel.VoucherPayParam;
import cn.bootx.platform.daxpay.service.core.channel.voucher.entity.Voucher;
import cn.bootx.platform.daxpay.service.core.channel.voucher.service.VoucherPayService;
import cn.bootx.platform.daxpay.service.core.channel.voucher.service.VoucherQueryService;
import cn.bootx.platform.daxpay.service.core.channel.voucher.service.VoucherRecordService;
import cn.bootx.platform.daxpay.service.func.AbsPayCloseStrategy;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 储值卡
 * @author xxm
 * @since 2023/12/30
 */
@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Service
@RequiredArgsConstructor
public class VoucherPayCloseStrategy extends AbsPayCloseStrategy {

    private final VoucherPayService voucherPayService;

    private final VoucherRecordService voucherRecordService;

    private final VoucherQueryService voucherQueryService;

    private Voucher voucher;

    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.VOUCHER;
    }

    /**
     * 关闭前的处理方式
     */
    @Override
    public void doBeforeCloseHandler() {
        String channelExtra = this.getChannelOrder().getChannelExtra();
        VoucherPayParam params = JSONUtil.toBean(channelExtra, VoucherPayParam.class);
        this.voucher = voucherQueryService.getVoucherByCardNo(params.getCardNo());
    }

    /**
     * 关闭操作
     */
    @Override
    public void doCloseHandler() {
        voucherPayService.close(this.getChannelOrder());
        voucherRecordService.payClose(this.getChannelOrder(),this.voucher);
    }
}
