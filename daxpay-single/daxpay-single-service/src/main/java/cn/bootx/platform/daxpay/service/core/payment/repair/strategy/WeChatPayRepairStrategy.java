package cn.bootx.platform.daxpay.service.core.payment.repair.strategy;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.service.code.PayRepairSourceEnum;
import cn.bootx.platform.daxpay.service.core.channel.wechat.entity.WeChatPayConfig;
import cn.bootx.platform.daxpay.service.core.channel.wechat.service.WeChatPayCloseService;
import cn.bootx.platform.daxpay.service.core.channel.wechat.service.WeChatPayConfigService;
import cn.bootx.platform.daxpay.service.core.channel.wechat.service.WeChatPayOrderService;
import cn.bootx.platform.daxpay.service.core.record.pay.dao.PayOrderChannelManager;
import cn.bootx.platform.daxpay.service.core.record.pay.entity.PayOrderChannel;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
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
public class WeChatPayRepairStrategy extends AbsPayRepairStrategy {
    private final WeChatPayCloseService closeService;
    private final WeChatPayOrderService orderService;
    private final PayOrderChannelManager payOrderChannelManager;

    private final WeChatPayConfigService weChatPayConfigService;

    private WeChatPayConfig weChatPayConfig;

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
    public void doSuccessHandler() {
        PayOrderChannel orderChannel = payOrderChannelManager.findByPaymentIdAndChannel(this.getOrder().getId(), PayChannelEnum.WECHAT.getCode())
                .orElseThrow(() -> new PayFailureException("支付宝订单不存在"));
        orderService.updateAsyncSuccess(this.getOrder(), orderChannel.getAmount());
    }

    /**
     * 退款处理 todo 需要结合退款同步功能进行协同实现
     */
    @Override
    public void doRefundHandler() {
    }

    /**
     * 取消支付
     */
    @Override
    public void doCloseHandler() {
        // 如果非同步出的订单取消状态, 则调用支付网关关闭订单
        if (this.getRepairSource() != PayRepairSourceEnum.SYNC){
            closeService.close(this.getOrder(),this.weChatPayConfig);
        }
        orderService.updateClose(this.getOrder().getId());
    }

    /**
     * 订单支付超时取消支付
     */
    @Override
    public void doTimeoutHandler() {
        closeService.close(this.getOrder(),this.weChatPayConfig);
        orderService.updateClose(this.getOrder().getId());
    }
}
