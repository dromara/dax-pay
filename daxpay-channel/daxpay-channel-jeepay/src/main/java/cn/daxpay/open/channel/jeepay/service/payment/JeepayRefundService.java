package cn.daxpay.open.channel.jeepay.service.payment;

import cn.daxpay.open.channel.jeepay.client.JeepayChannelClient;
import cn.daxpay.open.channel.jeepay.client.credential.JeepaySdkCredential;
import cn.daxpay.open.channel.jeepay.client.req.JeepayRefundReq;
import cn.daxpay.open.channel.jeepay.client.resp.JeepayRefundResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # Jeepay退款执行业务服务
///
/// 通过 [JeepayChannelClient] 调用子应用完成 Jeepay 退款(`/api/refund/refundOrder`)。
/// Jeepay 退款为异步受理模式, 受理即成功(PHP 参考语义, 最终态由退款查询兜底)。
@Slf4j
@Service
@RequiredArgsConstructor
public class JeepayRefundService {

    private final JeepayChannelClient jeepayChannelClient;

    /// 执行退款
    public RefundResultBo refund(RefundOrder refundOrder, JeepaySdkCredential credential) {
        JeepayRefundReq req = new JeepayRefundReq();
        req.setCredential(credential);
        // 退款单号(作为 Jeepay 商户退款单号 mchRefundNo)
        req.setOutRefundNo(refundOrder.getRelationOrderNo());
        // 原支付订单号(平台 tradeNo 作为原商户订单号)
        req.setOriginOutTradeNo(refundOrder.getTradeNo());
        // 原通道交易号(首次退款时可能为空)
        req.setOriginTradeNo(refundOrder.getOutOrderNo());
        req.setAmount(refundOrder.getAmount());

        DaxResult<JeepayRefundResp> result = jeepayChannelClient.refund(req);
        if (result.getCode() != 0) {
            // Jeepay 退款异常
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.jeepay.refundFailed", result.getMsg());
        }

        JeepayRefundResp resp = result.getData();
        RefundResultBo bo = new RefundResultBo()
                .setOutRefundNo(resp.getTradeNo())
                .setComplete(Boolean.TRUE.equals(resp.getComplete()));
        // Jeepay 退款受理即成功(最终态由退款查询兜底)
        bo.setStatus(Boolean.TRUE.equals(resp.getComplete())
                ? RefundOrderStatusEnum.SUCCESS
                : RefundOrderStatusEnum.PROGRESS);
        return bo;
    }
}
