package cn.bootx.platform.daxpay.service.core.payment.refund.strategy;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.RefundStatusEnum;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.channel.union.entity.UnionPayConfig;
import cn.bootx.platform.daxpay.service.core.channel.union.service.UnionPayConfigService;
import cn.bootx.platform.daxpay.service.core.channel.union.service.UnionPayRefundService;
import cn.bootx.platform.daxpay.service.func.AbsRefundStrategy;
import cn.bootx.platform.daxpay.service.sdk.union.api.UnionPayKit;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 云闪付支付退款
 * @author xxm
 * @since 2023/7/5
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class UnionRefundStrategy extends AbsRefundStrategy {

    private final UnionPayRefundService unionPayRefundService;

    private final UnionPayConfigService unionPayConfigService;

    private final UnionPayRecordService unionPayRecordService;

    private final PayChannelOrderService payChannelOrderService;

    private UnionPayConfig unionPayConfig;

    /**
     * 策略标识
     *
     * @see PayChannelEnum
     */
    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.UNION_PAY;
    }

    /**
     * 退款前对处理, 初始化微信支付配置
     */
    @Override
    public void doBeforeRefundHandler() {
        this.unionPayConfig = unionPayConfigService.getAndCheckConfig();
    }


    /**
     * 退款操作
     */
    @Override
    public void doRefundHandler() {
        UnionPayKit unionPayKit = unionPayConfigService.initPayService(unionPayConfig);
        unionPayRefundService.refund(this.getRefundOrder(), this.getPayOrder(), this.getRefundChannelParam().getAmount(), this.getPayChannelOrder(), unionPayKit);
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
            unionPayRecordService.refund(this.getRefundOrder(), this.getRefundChannelOrder());
        }
    }

}
