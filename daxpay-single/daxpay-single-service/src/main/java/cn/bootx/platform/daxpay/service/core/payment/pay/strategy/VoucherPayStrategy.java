package cn.bootx.platform.daxpay.service.core.payment.pay.strategy;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.param.channel.VoucherPayParam;
import cn.bootx.platform.daxpay.service.core.channel.voucher.entity.Voucher;
import cn.bootx.platform.daxpay.service.core.channel.voucher.service.VoucherPayService;
import cn.bootx.platform.daxpay.service.func.AbsPayStrategy;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Map;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 储值卡支付
 *
 * @author xxm
 * @since 2022/3/13
 */
@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Service
@RequiredArgsConstructor
public class VoucherPayStrategy extends AbsPayStrategy {

    private final VoucherPayService voucherPayService;

    private Voucher voucher;

    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.VOUCHER;
    }

    /**
     * 支付前处理
     */
    @Override
    public void doBeforePayHandler() {
        VoucherPayParam voucherPayParam;
        try {
            // 储值卡参数验证
            Map<String, Object> channelParam = this.getPayChannelParam().getChannelParam();
            if (CollUtil.isNotEmpty(channelParam)) {
                voucherPayParam = BeanUtil.toBean(channelParam, VoucherPayParam.class);
            } else {
                throw new PayFailureException("储值卡支付参数错误");
            }
        }
        catch (JSONException e) {
            throw new PayFailureException("储值卡支付参数错误");
        }
        this.voucher = voucherPayService.getAndCheckVoucher(voucherPayParam);
        // 金额是否满足
        if (this.getPayChannelParam().getAmount() > this.voucher.getBalance()) {
            throw new PayFailureException("储值卡余额不足");
        }
    }

    /**
     * 支付操作
     */
    @Override
    public void doPayHandler() {
        voucherPayService.pay(this.getPayChannelParam().getAmount(), this.voucher);
    }

}
