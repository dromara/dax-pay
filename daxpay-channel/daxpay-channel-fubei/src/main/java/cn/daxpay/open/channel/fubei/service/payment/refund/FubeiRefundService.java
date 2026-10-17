package cn.daxpay.open.channel.fubei.service.payment.refund;

import cn.daxpay.open.channel.fubei.client.FubeiChannelClient;
import cn.daxpay.open.channel.fubei.client.credential.FubeiSdkCredential;
import cn.daxpay.open.channel.fubei.client.req.FubeiRefundReq;
import cn.daxpay.open.channel.fubei.client.resp.FubeiRefundResp;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 付呗退款业务服务
///
/// 通过 [FubeiChannelClient] 调用子应用发起付呗 fbpay.order.refund 退款。
/// 对齐参考实现: 退款接口同步受理即成功(付呗无退款异步通知/退款查询接口), 受理成功即返回退款终态。
@Slf4j
@Service
@RequiredArgsConstructor
public class FubeiRefundService {

    private final FubeiChannelClient fubeiChannelClient;

    /// 执行付呗退款
    public RefundResultBo refund(RefundOrder refundOrder, FubeiSdkCredential credential) {
        FubeiRefundReq req = new FubeiRefundReq();
        req.setOutRefundNo(refundOrder.getRelationOrderNo());
        req.setOriginOutTradeNo(refundOrder.getTradeNo());
        // 原支付订单的付呗订单号(order_sn)
        req.setOriginOutOrderNo(refundOrder.getOutOrderNo());
        req.setAmount(refundOrder.getAmount());
        req.setReason(refundOrder.getReason());
        req.setCredential(credential);

        DaxResult<FubeiRefundResp> result = fubeiChannelClient.refund(req);
        if (result.getCode() != 0) {
            log.error("付呗通道退款失败: refundNo={}, msg={}", refundOrder.getRefundNo(), result.getMsg());
            return new RefundResultBo()
                    .setComplete(false)
                    .setStatus(RefundOrderStatusEnum.FAIL)
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }

        // 同步受理即成功, 直接返回退款终态
        return new RefundResultBo()
                .setComplete(true)
                .setStatus(RefundOrderStatusEnum.SUCCESS);
    }
}
