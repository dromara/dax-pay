package cn.daxpay.open.channel.helipay.service.payment;

import cn.daxpay.open.channel.helipay.client.HelipayChannelClient;
import cn.daxpay.open.channel.helipay.client.credential.HelipaySdkCredential;
import cn.daxpay.open.channel.helipay.client.req.HelipayRefundReq;
import cn.daxpay.open.channel.helipay.client.resp.HelipayRefundResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 合利宝退款执行业务服务
///
/// 通过 [HelipayChannelClient] 调用子应用发起合利宝退款(AppPayRefund)。
/// 合利宝退款为同步结果模式(retCode 0000/0001 即受理完成并同步返回退款流水号/金额,
/// 移植自易支付 PHP 参考语义), 受理即退款终态成功。
@Slf4j
@Service
@RequiredArgsConstructor
public class HelipayRefundService {

    private final HelipayChannelClient helipayChannelClient;

    /// 执行退款
    public RefundResultBo refund(RefundOrder refundOrder, HelipaySdkCredential credential) {
        HelipayRefundReq req = new HelipayRefundReq();
        req.setCredential(credential);
        // 退款单号(作为合利宝 P4_refundOrderId)
        req.setOutRefundNo(refundOrder.getRelationOrderNo());
        // 原支付订单号(平台 tradeNo 作为合利宝 P2_orderId)
        req.setOriginOutTradeNo(refundOrder.getTradeNo());
        req.setAmount(refundOrder.getAmount());

        DaxResult<HelipayRefundResp> result = helipayChannelClient.refund(req);
        if (result.getCode() != 0) {
            // 合利宝退款异常
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.helipay.refundFailed", result.getMsg());
        }

        HelipayRefundResp resp = result.getData();
        // 同步结果模式, 受理即终态(沿用 PHP 参考语义)
        return new RefundResultBo()
                .setOutRefundNo(resp.getOutOrderNo())
                .setComplete(true)
                .setStatus(RefundOrderStatusEnum.SUCCESS);
    }
}
