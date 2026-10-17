package cn.daxpay.open.channel.lakala.service.moss;

import cn.daxpay.open.channel.lakala.client.LakalaChannelClient;
import cn.daxpay.open.channel.lakala.client.credential.MossSdkCredential;
import cn.daxpay.open.channel.lakala.client.req.MossRefundReq;
import cn.daxpay.open.channel.lakala.client.resp.MossRefundResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 拉卡拉 MOSS 退款执行业务服务
///
/// 通过 [LakalaChannelClient] 调用子应用完成 MOSS 退款。
@Slf4j
@Service
@RequiredArgsConstructor
public class LakalaMossRefundService {

    private final LakalaChannelClient lakalaChannelClient;

    /// 执行退款
    public RefundResultBo refund(RefundOrder refundOrder, MossSdkCredential credential) {
        MossRefundReq req = new MossRefundReq();
        req.setCredential(credential);
        // 退款单号(作为 MOSS 退款 order_no)
        req.setOutRefundNo(refundOrder.getRelationOrderNo());
        // 原支付订单号(平台 tradeNo 作为原 order_no)
        req.setOriginOutTradeNo(refundOrder.getTradeNo());
        req.setAmount(refundOrder.getAmount());
        req.setReason(refundOrder.getReason());
        req.setClientIp(refundOrder.getClientIp());

        DaxResult<MossRefundResp> result = lakalaChannelClient.mossRefund(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.lakala.refundFailed", result.getMsg());
        }

        MossRefundResp resp = result.getData();
        RefundResultBo bo = new RefundResultBo()
                .setOutRefundNo(resp.getTradeNo())
                .setFinishTime(resp.getFinishTime())
                .setComplete(Boolean.TRUE.equals(resp.getComplete()));
        // 退款状态: 受理即处理中, 同步返回完成时间视为成功
        bo.setStatus(Boolean.TRUE.equals(resp.getComplete())
                ? RefundOrderStatusEnum.SUCCESS
                : RefundOrderStatusEnum.PROGRESS);
        return bo;
    }
}
