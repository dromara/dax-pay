package cn.daxpay.open.channel.lakala.strategy;

import cn.daxpay.open.channel.lakala.client.credential.LakalaSdkCredential;
import cn.daxpay.open.channel.lakala.service.isv.LakalaIsvConfigAssembler;
import cn.daxpay.open.channel.lakala.service.payment.LakalaRefundService;
import cn.daxpay.open.payment.strategy.refund.AbsRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 拉卡拉服务商退款策略
///
/// 从退款订单读取通道路由参数(channelMchNo / capability),
/// 组装通道凭证(委托 [LakalaIsvConfigAssembler]), 退款执行委托给 [LakalaRefundService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class LakalaRefundStrategy extends AbsRefundStrategy {

    private final LakalaRefundService lakalaRefundService;
    private final LakalaIsvConfigAssembler lakalaIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.LAKALA_PAY;
    }

    @Override
    public RefundResultBo doRefund(RefundOrder refundOrder) {
        // 组装通道调用凭证
        LakalaSdkCredential credential = lakalaIsvConfigAssembler.buildConfig(
                refundOrder.getMchNo(), refundOrder.getChannelMchNo(), refundOrder.getCapability());
        return lakalaRefundService.refund(refundOrder, credential);
    }
}
