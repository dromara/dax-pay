package cn.daxpay.open.channel.longpay.service.payment.refund;

import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 建行龙支付退款业务服务
///
/// 依公开资料, 建行龙支付聚合线(B2C 统一下单平台)的退款不经理财网关接口,
/// 仅两种官方途径: ①建行商户服务平台手工退款 ②外联平台退款接口(交易码 5W1004, 需操作员与证书体系)。
/// 两者均未在协议层对接, 本服务不做通道调用, 恒返回退款失败并给出明确原因;
/// 待建行外联平台/商户平台 API 正式文档到手后在此补齐。
@Slf4j
@Service
public class LongpayRefundService {

    /// 发起退款(通道未对接, 明确失败)
    public RefundResultBo refund(RefundOrder refundOrder) {
        log.warn("建行龙支付退款未对接(需经建行商户服务平台或外联平台): refundNo={}", refundOrder.getRefundNo());
        return new RefundResultBo()
                .setComplete(false)
                .setStatus(RefundOrderStatusEnum.FAIL)
                .setSyncSuccess(false)
                .setSyncErrorMsg("建行龙支付退款暂未对接(需经建行商户服务平台或外联平台), 待正式文档补齐");
    }
}
