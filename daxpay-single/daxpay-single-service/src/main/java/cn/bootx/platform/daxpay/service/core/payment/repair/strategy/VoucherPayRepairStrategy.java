package cn.bootx.platform.daxpay.service.core.payment.repair.strategy;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.service.core.channel.voucher.service.VoucherPayService;
import cn.bootx.platform.daxpay.service.func.AbsPayRepairStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 *
 * @author xxm
 * @since 2023/12/29
 */
@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Service
@RequiredArgsConstructor
public class VoucherPayRepairStrategy extends AbsPayRepairStrategy {
    private final VoucherPayService voucherPayService;


    /**
     * 策略标识
     */
    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.VOUCHER;
    }

    /**
     * 取消支付
     */
    @Override
    public void doCloseLocalHandler() {
        voucherPayService.close(this.getOrder().getId());
        this.getChannelOrder().setStatus(PayStatusEnum.CLOSE.getCode());
    }
}
