package cn.daxpay.open.channel.vbill.service.payment;

import cn.daxpay.open.channel.vbill.client.VbillChannelClient;
import cn.daxpay.open.channel.vbill.client.credential.VbillSdkCredential;
import cn.daxpay.open.channel.vbill.client.req.VbillRefundReq;
import cn.daxpay.open.channel.vbill.client.resp.VbillRefundResp;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.ChannelResultUnknownException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 随行付服务商退款执行业务服务
///
/// 通过 [VbillChannelClient] 调用子应用完成随行付退款(`/order/refund`)。
@Slf4j
@Service
@RequiredArgsConstructor
public class VbillRefundService {

    private final VbillChannelClient vbillChannelClient;

    /// 执行退款
    public RefundResultBo refund(RefundOrder refundOrder, VbillSdkCredential credential) {
        VbillRefundReq req = new VbillRefundReq();
        req.setCredential(credential);
        // 退款单号(作为随行付 ordNo)
        req.setOutRefundNo(refundOrder.getRelationOrderNo());
        // 原支付订单的网关订单号(随行付 uuid)
        req.setOutOrderNo(refundOrder.getOutOrderNo());
        req.setAmount(refundOrder.getAmount());
        req.setReason(refundOrder.getReason());

        DaxResult<VbillRefundResp> result = vbillChannelClient.refund(req);
        // 子应用返回"结果未知"(上游通道连接中断/读超时): 保持 PROGRESS 交由退款同步查单纠正,
        // 避免误判 FAIL 导致资金悬挂
        if (result.getCode() == ChannelResultUnknownException.RESULT_UNKNOWN_CODE) {
            throw new ChannelResultUnknownException("pay.error.channelResultUnknown",
                    new RuntimeException(result.getMsg()));
        }
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.vbill.refundFailed", result.getMsg());
        }

        VbillRefundResp resp = result.getData();
        RefundResultBo bo = new RefundResultBo()
                .setOutRefundNo(resp.getOutRefundOrderNo())
                .setFinishTime(resp.getFinishTime())
                .setComplete(Boolean.TRUE.equals(resp.getComplete()));
        // complete=true 表示已终态(REFUNDSUC/REFUNDFAIL), complete=false 表示处理中(REFUNDING)
        bo.setStatus(Boolean.TRUE.equals(resp.getComplete())
                ? RefundOrderStatusEnum.SUCCESS
                : RefundOrderStatusEnum.PROGRESS);
        return bo;
    }
}
