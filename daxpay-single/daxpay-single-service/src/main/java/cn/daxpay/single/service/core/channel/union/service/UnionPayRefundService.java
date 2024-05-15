package cn.daxpay.single.service.core.channel.union.service;

import cn.daxpay.single.code.RefundStatusEnum;
import cn.daxpay.single.service.code.UnionPayCode;
import cn.daxpay.single.service.common.context.RefundLocal;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.order.refund.entity.RefundOrder;
import cn.daxpay.single.service.sdk.union.api.UnionPayKit;
import cn.daxpay.single.service.sdk.union.bean.UnionRefundOrder;
import com.egzosn.pay.union.bean.UnionRefundResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    public void refund(RefundOrder refundOrder, UnionPayKit unionPayKit) {

        // 金额转换
        BigDecimal refundAmount = BigDecimal.valueOf(refundOrder.getAmount() * 0.01);
        BigDecimal orderAmount = BigDecimal.valueOf(refundOrder.getOrderAmount() * 0.01);

        UnionRefundOrder unionRefundOrder = new UnionRefundOrder();
        unionRefundOrder.setRefundNo(refundOrder.getRefundNo());
        unionRefundOrder.setTradeNo(refundOrder.getOutOrderNo());
        unionRefundOrder.setRefundAmount(refundAmount);
        unionRefundOrder.setTotalAmount(orderAmount);
        UnionRefundResult refund = unionPayKit.refund(unionRefundOrder);

        String outRefundNo = (String) refund.getAttr(UnionPayCode.QUERY_ID);
        // 云闪付退款是否成功需要查询状态, 所以设置为退款中状态
        RefundLocal refundInfo = PaymentContextLocal.get().getRefundInfo();
        refundInfo.setStatus(RefundStatusEnum.PROGRESS).setOutRefundNo(outRefundNo);
        // 设置退款时间
        refundInfo.setFinishTime(LocalDateTime.now());
    }
}
