package cn.daxpay.open.channel.jeepay.service.payment;

import cn.daxpay.open.channel.jeepay.client.JeepayChannelClient;
import cn.daxpay.open.channel.jeepay.client.credential.JeepaySdkCredential;
import cn.daxpay.open.channel.jeepay.client.req.JeepayRefundSyncReq;
import cn.daxpay.open.channel.jeepay.client.resp.JeepayRefundSyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # Jeepay退款同步业务服务
///
/// 通过 [JeepayChannelClient] 调用子应用查询 Jeepay 退款最终状态(`/api/refund/query`)。
/// Jeepay 退款状态: "2"=成功(终态), "3"=失败(终态), "4"=关闭(终态), 其他("0"生成/"1"退款中)=进行中。
@Slf4j
@Service
@RequiredArgsConstructor
public class JeepayRefundSyncService {

    /// Jeepay 退款状态: 成功(终态)
    private static final String REFUND_STATE_SUCCESS = "2";

    /// Jeepay 退款状态: 失败(终态)
    private static final String REFUND_STATE_FAIL = "3";

    /// Jeepay 退款状态: 关闭(终态)
    private static final String REFUND_STATE_CLOSED = "4";

    private final JeepayChannelClient jeepayChannelClient;

    /// 同步退款状态
    public RefundResultBo sync(RefundOrder refundOrder, JeepaySdkCredential credential) {
        JeepayRefundSyncReq req = new JeepayRefundSyncReq();
        req.setCredential(credential);
        req.setOutRefundNo(refundOrder.getRelationOrderNo());
        req.setOriginTradeNo(refundOrder.getOutRefundNo());

        DaxResult<JeepayRefundSyncResp> result = jeepayChannelClient.refundSync(req);
        RefundResultBo bo = new RefundResultBo();
        if (result.getCode() != 0) {
            // 同步失败(不抛异常, 由核心层决定重试)
            bo.setSyncSuccess(false);
            bo.setSyncErrorMsg(result.getMsg());
            return bo;
        }

        JeepayRefundSyncResp resp = result.getData();
        bo.setSyncSuccess(true);
        bo.setOutRefundNo(resp.getTradeNo());
        bo.setStatus(mapRefundStatus(resp.getRefundStatus()));
        // 成功态补充金额与时间
        if (Objects.equals(resp.getRefundStatus(), REFUND_STATE_SUCCESS)) {
            bo.setRefundAmount(resp.getRefundAmount());
            bo.setFinishTime(resp.getFinishTime());
        }
        return bo;
    }

    /// Jeepay 退款状态(原始码) → 平台退款状态
    private RefundOrderStatusEnum mapRefundStatus(String refundStatus) {
        if (Objects.equals(refundStatus, REFUND_STATE_SUCCESS)) {
            return RefundOrderStatusEnum.SUCCESS;
        }
        if (Objects.equals(refundStatus, REFUND_STATE_FAIL) || Objects.equals(refundStatus, REFUND_STATE_CLOSED)) {
            return RefundOrderStatusEnum.FAIL;
        }
        // 其他状态(0=订单生成/1=退款中)视为进行中
        return RefundOrderStatusEnum.PROGRESS;
    }
}
