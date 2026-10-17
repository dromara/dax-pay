package cn.daxpay.open.channel.epaylinks.service.payment.refund;

import cn.daxpay.open.channel.epaylinks.client.EpaylinksChannelClient;
import cn.daxpay.open.channel.epaylinks.client.credential.EpaylinksSdkCredential;
import cn.daxpay.open.channel.epaylinks.client.req.EpaylinksRefundReq;
import cn.daxpay.open.channel.epaylinks.client.resp.EpaylinksRefundResp;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 易票联退款业务服务
///
/// 通过 [EpaylinksChannelClient] 调用子应用发起易票联 Refund/V2 退款。
/// 对齐参考实现: 退款为同步受理即终态语义(returnCode=0000 即退款成功),
/// 受理成功直接置退款单成功(无退款查询/退款回调)。
///
/// 注意: 退款需原支付的易票联平台交易号(transactionNo), 未收到支付回调/查单回执前无法发起退款。
@Slf4j
@Service
@RequiredArgsConstructor
public class EpaylinksRefundService {

    private final EpaylinksChannelClient epaylinksChannelClient;

    /// 执行易票联退款
    public RefundResultBo refund(RefundOrder refundOrder, EpaylinksSdkCredential credential) {
        EpaylinksRefundReq req = new EpaylinksRefundReq();
        req.setOutRefundNo(refundOrder.getRelationOrderNo());
        // 原支付订单的易票联平台交易号(transactionNo, 来自支付回调/查单)
        req.setOutTransNo(refundOrder.getOutOrderNo());
        req.setAmount(refundOrder.getAmount());
        req.setCredential(credential);

        DaxResult<EpaylinksRefundResp> result = epaylinksChannelClient.refund(req);
        if (result.getCode() != 0) {
            log.error("易票联通道退款失败: refundNo={}, msg={}", refundOrder.getRefundNo(), result.getMsg());
            return new RefundResultBo()
                    .setComplete(false)
                    .setStatus(RefundOrderStatusEnum.FAIL)
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }

        // Refund/V2 为同步受理即终态, 受理成功即退款成功
        return new RefundResultBo()
                .setComplete(true)
                .setStatus(RefundOrderStatusEnum.SUCCESS);
    }
}
