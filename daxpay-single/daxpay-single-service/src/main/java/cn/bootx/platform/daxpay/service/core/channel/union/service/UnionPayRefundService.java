package cn.bootx.platform.daxpay.service.core.channel.union.service;

import cn.bootx.platform.daxpay.code.RefundStatusEnum;
import cn.bootx.platform.daxpay.service.code.UnionPayCode;
import cn.bootx.platform.daxpay.service.common.context.RefundLocal;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayChannelOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.RefundOrder;
import cn.bootx.platform.daxpay.service.sdk.union.api.UnionPayKit;
import cn.bootx.platform.daxpay.service.sdk.union.bean.UnionRefundOrder;
import com.egzosn.pay.union.bean.UnionRefundResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 云闪付退款操作
 * @author xxm
 * @since 2024/3/7
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UnionPayRefundService {

    /**
     * 退款方法
     */
    public void refund(RefundOrder refundOrder, PayOrder payOrder, int amount, PayChannelOrder channelOrder, UnionPayKit unionPayKit) {

        // 金额转换
        BigDecimal refundAmount = BigDecimal.valueOf(amount * 0.01);
        BigDecimal orderAmount = BigDecimal.valueOf(channelOrder.getAmount() * 0.01);

        UnionRefundOrder unionRefundOrder = new UnionRefundOrder();
        unionRefundOrder.setRefundNo(String.valueOf(refundOrder.getId()));
        unionRefundOrder.setTradeNo(String.valueOf(payOrder.getGatewayOrderNo()));
        unionRefundOrder.setRefundAmount(refundAmount);
        unionRefundOrder.setTotalAmount(orderAmount);
        UnionRefundResult refund = unionPayKit.refund(unionRefundOrder);

        String gatewayNo = (String) refund.getAttr(UnionPayCode.QUERY_ID);
        // 云闪付退款是否成功需要查询状态, 所以设置为退款中状态
        RefundLocal refundInfo = PaymentContextLocal.get().getRefundInfo();
        refundInfo.setStatus(RefundStatusEnum.PROGRESS).setGatewayOrderNo(gatewayNo);
    }
}
