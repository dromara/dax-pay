package cn.bootx.platform.daxpay.service.core.payment.repair.strategy.pay;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.channel.wechat.entity.WeChatPayConfig;
import cn.bootx.platform.daxpay.service.core.channel.wechat.service.WeChatPayCloseService;
import cn.bootx.platform.daxpay.service.core.channel.wechat.service.WeChatPayConfigService;
import cn.bootx.platform.daxpay.service.func.AbsPayRepairStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 微信支付订单修复策略类
 * @author xxm
 * @since 2023/12/29
 */
@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Service
@RequiredArgsConstructor
public class WeChatPayRepairStrategy extends AbsPayRepairStrategy {
    private final WeChatPayCloseService closeService;

    private final WeChatPayConfigService weChatPayConfigService;

    private final WeChatPayRecordService weChatPayRecordService;

    private WeChatPayConfig weChatPayConfig;

    /**
     * 策略标识
     */
    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.WECHAT;
    }

    /**
     * 等待支付处理
     */
    @Override
    public void doWaitPayHandler(){
        this.getChannelOrder().setPayTime(null).setStatus(PayStatusEnum.PROGRESS.getCode());
    }

    /**
     * 修复前处理
     */
    @Override
    public void doBeforeHandler() {
        this.weChatPayConfig = weChatPayConfigService.getConfig();
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
        // 保存流水记录
        weChatPayRecordService.pay(this.getOrder(), this.getChannelOrder());
    }

    /**
     * 关闭本地支付
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
        closeService.close(this.getOrder(),this.weChatPayConfig);
        this.doCloseLocalHandler();
    }
}
