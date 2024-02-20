package cn.bootx.platform.daxpay.service.core.payment.repair.strategy.pay;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.channel.alipay.entity.AliPayConfig;
import cn.bootx.platform.daxpay.service.core.channel.alipay.service.AliPayCloseService;
import cn.bootx.platform.daxpay.service.core.channel.alipay.service.AliPayConfigService;
import cn.bootx.platform.daxpay.service.core.channel.alipay.service.AliPayRecordService;
import cn.bootx.platform.daxpay.service.core.order.pay.dao.PayChannelOrderManager;
import cn.bootx.platform.daxpay.service.func.AbsPayRepairStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 支付宝订单修复策略
 * @author xxm
 * @since 2023/12/27
 */
@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Service
@RequiredArgsConstructor
public class AliPayRepairStrategy extends AbsPayRepairStrategy {
    private final AliPayCloseService closeService;

    private final AliPayConfigService aliPayConfigService;

    private final AliPayRecordService aliRecordService;

    private final PayChannelOrderManager payChannelOrderManager;

    /**
     * 策略标识
     */
    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.ALI;
    }

    /**
     * 修复前处理
     */
    @Override
    public void doBeforeHandler() {
        AliPayConfig config = aliPayConfigService.getConfig();
        aliPayConfigService.initConfig(config);
    }

    /**
     * 支付成功处理
     */
    @Override
    public void doPaySuccessHandler() {
        LocalDateTime payTime = PaymentContextLocal.get()
                .getRepairInfo()
                .getFinishTime();
        this.getChannelOrder().setStatus(PayStatusEnum.SUCCESS.getCode())
                .setPayTime(payTime);
        payChannelOrderManager.updateById(this.getChannelOrder());
        // 支付完成, 保存记录
        aliRecordService.pay(this.getOrder(), this.getChannelOrder());
    }

    /**
     * 等待支付处理
     */
    @Override
    public void doWaitPayHandler() {
        super.doWaitPayHandler();
    }

    /**
     * 取消支付
     */
    @Override
    public void doCloseLocalHandler() {
        this.getChannelOrder().setStatus(PayStatusEnum.CLOSE.getCode());
    }


    /**
     * 关闭本地支付和网关支付
     */
    @Override
    public void doCloseGatewayHandler() {
        closeService.close(this.getOrder());
        this.doCloseLocalHandler();
    }
}
