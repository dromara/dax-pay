package cn.daxpay.open.channel.jlpay.service.payment.refund;

import cn.daxpay.open.channel.jlpay.client.JlpayChannelClient;
import cn.daxpay.open.channel.jlpay.client.credential.JlpaySdkCredential;
import cn.daxpay.open.channel.jlpay.client.req.JlpayRefundReq;
import cn.daxpay.open.channel.jlpay.client.resp.JlpayRefundResp;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 嘉联退款业务服务
///
/// 通过 [JlpayChannelClient] 调用子应用发起嘉联 /open/trans/refund 退款。
/// 嘉联退款为同步终态: 受理成功即退款成功, 无退款异步通知与退款查询接口(对齐参考实现能力)。
///
/// 退款需原嘉联交易号(ori_transaction_id = 原支付订单的通道订单号), 未知时退款失败由人工跟进。
@Slf4j
@Service
@RequiredArgsConstructor
public class JlpayRefundService {

    private final JlpayChannelClient jlpayChannelClient;

    /// 执行嘉联退款
    public RefundResultBo refund(RefundOrder refundOrder, JlpaySdkCredential credential) {
        // 原嘉联交易号未知时无法发起退款(嘉联退款仅支持按 ori_transaction_id), 记失败由人工跟进
        if (StrUtil.isBlank(refundOrder.getOutOrderNo())) {
            log.error("嘉联退款缺少原交易号: refundNo={}", refundOrder.getRefundNo());
            return new RefundResultBo()
                    .setComplete(false)
                    .setStatus(RefundOrderStatusEnum.FAIL)
                    .setSyncSuccess(false)
                    .setSyncErrorMsg("原支付嘉联交易号未知, 无法发起退款");
        }
        JlpayRefundReq req = new JlpayRefundReq();
        req.setOutRefundNo(refundOrder.getRelationOrderNo());
        // 原支付订单的嘉联交易号(transaction_id)
        req.setOriginOutOrderNo(refundOrder.getOutOrderNo());
        req.setAmount(refundOrder.getAmount());
        req.setClientIp(refundOrder.getClientIp());
        req.setCredential(credential);

        DaxResult<JlpayRefundResp> result = jlpayChannelClient.refund(req);
        if (result.getCode() != 0) {
            log.error("嘉联通道退款失败: refundNo={}, msg={}", refundOrder.getRefundNo(), result.getMsg());
            return new RefundResultBo()
                    .setComplete(false)
                    .setStatus(RefundOrderStatusEnum.FAIL)
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }

        // 嘉联退款同步终态, 受理成功即完成
        return new RefundResultBo()
                .setComplete(true)
                .setStatus(RefundOrderStatusEnum.SUCCESS);
    }
}
