package cn.daxpay.open.channel.lakala.service.payment;

import cn.daxpay.open.channel.lakala.client.LakalaChannelClient;
import cn.daxpay.open.channel.lakala.client.credential.LakalaSdkCredential;
import cn.daxpay.open.channel.lakala.client.req.LakalaRefundReq;
import cn.daxpay.open.channel.lakala.client.resp.LakalaRefundResp;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 拉卡拉服务商退款执行业务服务
///
/// 通过 [LakalaChannelClient] 调用子应用完成拉卡拉退款。
@Slf4j
@Service
@RequiredArgsConstructor
public class LakalaRefundService {

    private final LakalaChannelClient lakalaChannelClient;

    /// 执行退款
    public RefundResultBo refund(RefundOrder refundOrder, LakalaSdkCredential credential) {
        LakalaRefundReq req = new LakalaRefundReq();
        req.setCredential(credential);
        // 退款单号(作为拉卡拉 out_trade_no)
        req.setOutRefundNo(refundOrder.getRelationOrderNo());
        // 原支付订单号(平台 tradeNo 作为原 out_trade_no)
        req.setOriginOutTradeNo(refundOrder.getTradeNo());
        // 原通道交易号(退款同步查询用, 首次退款时可能为空)
        req.setOriginTradeNo(refundOrder.getOutOrderNo());
        req.setAmount(refundOrder.getAmount());
        req.setReason(refundOrder.getReason());
        req.setClientIp(refundOrder.getClientIp());

        DaxResult<LakalaRefundResp> result = lakalaChannelClient.refund(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.lakala.refundFailed", result.getMsg());
        }

        LakalaRefundResp resp = result.getData();
        RefundResultBo bo = new RefundResultBo()
                .setOutRefundNo(resp.getTradeNo())
                .setFinishTime(resp.getFinishTime())
                .setComplete(Boolean.TRUE.equals(resp.getComplete()));
        // 退款状态: 有完成时间视为成功, 否则处理中
        bo.setStatus(Boolean.TRUE.equals(resp.getComplete())
                ? RefundOrderStatusEnum.SUCCESS
                : RefundOrderStatusEnum.PROGRESS);
        return bo;
    }
}
