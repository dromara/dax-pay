package cn.bootx.platform.daxpay.service.core.payment.refund.strategy;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.RefundStatusEnum;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.channel.wechat.entity.WeChatPayConfig;
import cn.bootx.platform.daxpay.service.core.channel.wechat.service.WeChatPayConfigService;
import cn.bootx.platform.daxpay.service.core.channel.wechat.service.WeChatPayRecordService;
import cn.bootx.platform.daxpay.service.core.channel.wechat.service.WechatRefundService;
import cn.bootx.platform.daxpay.service.core.order.pay.service.PayChannelOrderService;
import cn.bootx.platform.daxpay.service.func.AbsRefundStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 微信支付退款
 * @author xxm
 * @since 2023/7/5
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class WeChatPayRefundStrategy extends AbsRefundStrategy {

    private final WeChatPayConfigService weChatPayConfigService;

    private final WechatRefundService wechatRefundService;

    private final WeChatPayRecordService weChatPayRecordService;

    private final PayChannelOrderService payChannelOrderService;

    private WeChatPayConfig weChatPayConfig;

    /**
     * 策略标识
     *
     * @see PayChannelEnum
     */
    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.WECHAT;
    }

    /**
     * 退款前对处理, 初始化微信支付配置
     */
    @Override
    public void doBeforeRefundHandler() {
        this.weChatPayConfig = weChatPayConfigService.getAndCheckConfig();
    }


    /**
     * 退款操作
     */
    @Override
    public void doRefundHandler() {
        wechatRefundService.refund(this.getRefundOrder(), this.getRefundChannelParam().getAmount(), this.getPayChannelOrder(), this.weChatPayConfig);
    }

    /**
     * 退款发起成功操作
     */
    @Override
    public void doSuccessHandler() {
        // 更新退款订单数据状态
        RefundStatusEnum refundStatusEnum = PaymentContextLocal.get()
                .getRefundInfo()
                .getStatus();
        this.getRefundChannelOrder().setStatus(refundStatusEnum.getCode());

        // 更新支付通道订单中的属性
        payChannelOrderService.updateAsyncPayRefund(this.getPayChannelOrder(), this.getRefundChannelOrder());
        // 如果退款完成, 保存流水记录
        if (Objects.equals(RefundStatusEnum.SUCCESS.getCode(), refundStatusEnum.getCode())) {
            weChatPayRecordService.refund(this.getRefundOrder(), this.getRefundChannelOrder());
        }
    }
}
