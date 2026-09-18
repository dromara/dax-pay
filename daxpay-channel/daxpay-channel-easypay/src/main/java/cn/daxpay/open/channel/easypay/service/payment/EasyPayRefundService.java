package cn.daxpay.open.channel.easypay.service.payment;

import cn.daxpay.open.channel.easypay.client.EasyPayChannelClient;
import cn.daxpay.open.channel.easypay.client.credential.EasyPaySdkCredential;
import cn.daxpay.open.channel.easypay.client.req.EasyPayRefundReq;
import cn.daxpay.open.channel.easypay.client.resp.EasyPayRefundResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 易支付退款执行业务服务
///
/// 通过 [EasyPayChannelClient] 调用子应用完成易支付退款。
/// 易支付退款为同步受理即成功模式(无退款异步通知), 受理即置终态成功。
@Slf4j
@Service
@RequiredArgsConstructor
public class EasyPayRefundService {

    private final EasyPayChannelClient easyPayChannelClient;

    /// 执行退款
    public RefundResultBo refund(RefundOrder refundOrder, EasyPaySdkCredential credential) {
        EasyPayRefundReq req = new EasyPayRefundReq();
        req.setCredential(credential);
        // 退款单号(作为易支付商户退款单号 out_refund_no)
        req.setOutRefundNo(refundOrder.getRelationOrderNo());
        // 原支付订单号(平台 tradeNo 作为原商户订单号)
        req.setOriginOutTradeNo(refundOrder.getTradeNo());
        // 原通道交易号(首次退款时可能为空)
        req.setOriginTradeNo(refundOrder.getOutOrderNo());
        req.setAmount(refundOrder.getAmount());

        DaxResult<EasyPayRefundResp> result = easyPayChannelClient.refund(req);
        if (result.getCode() != 0) {
            // 易支付退款异常
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.easypay.refundFailed", result.getMsg());
        }

        EasyPayRefundResp resp = result.getData();
        RefundResultBo bo = new RefundResultBo()
                .setOutRefundNo(resp.getTradeNo())
                .setComplete(Boolean.TRUE.equals(resp.getComplete()));
        // 易支付退款同步受理即成功
        bo.setStatus(Boolean.TRUE.equals(resp.getComplete())
                ? RefundOrderStatusEnum.SUCCESS
                : RefundOrderStatusEnum.PROGRESS);
        return bo;
    }
}
