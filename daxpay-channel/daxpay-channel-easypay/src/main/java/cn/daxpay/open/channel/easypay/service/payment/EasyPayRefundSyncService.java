package cn.daxpay.open.channel.easypay.service.payment;

import cn.daxpay.open.channel.easypay.client.EasyPayChannelClient;
import cn.daxpay.open.channel.easypay.client.credential.EasyPaySdkCredential;
import cn.daxpay.open.channel.easypay.client.req.EasyPayRefundSyncReq;
import cn.daxpay.open.channel.easypay.client.resp.EasyPayRefundSyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 易支付退款同步业务服务
///
/// 通过 [EasyPayChannelClient] 调用子应用查询易支付退款最终状态。
/// 易支付退款状态: "1"=成功(终态), "0"=失败(终态), 其他=进行中。
@Slf4j
@Service
@RequiredArgsConstructor
public class EasyPayRefundSyncService {

    /// 易支付退款状态: 成功(终态)
    private static final String REFUND_STATUS_SUCCESS = "1";

    /// 易支付退款状态: 失败(终态)
    private static final String REFUND_STATUS_FAIL = "0";

    private final EasyPayChannelClient easyPayChannelClient;

    /// 同步退款状态
    public RefundResultBo sync(RefundOrder refundOrder, EasyPaySdkCredential credential) {
        EasyPayRefundSyncReq req = new EasyPayRefundSyncReq();
        req.setCredential(credential);
        req.setOutRefundNo(refundOrder.getRelationOrderNo());
        req.setOriginTradeNo(refundOrder.getOutRefundNo());

        DaxResult<EasyPayRefundSyncResp> result = easyPayChannelClient.refundSync(req);
        RefundResultBo bo = new RefundResultBo();
        if (result.getCode() != 0) {
            // 同步失败(不抛异常, 由核心层决定重试)
            bo.setSyncSuccess(false);
            bo.setSyncErrorMsg(result.getMsg());
            return bo;
        }

        EasyPayRefundSyncResp resp = result.getData();
        bo.setSyncSuccess(true);
        bo.setOutRefundNo(resp.getTradeNo());
        bo.setStatus(mapRefundStatus(resp.getRefundStatus()));
        // 成功态补充金额与时间
        if (Objects.equals(resp.getRefundStatus(), REFUND_STATUS_SUCCESS)) {
            bo.setRefundAmount(resp.getRefundAmount());
            bo.setFinishTime(resp.getFinishTime());
        }
        return bo;
    }

    /// 易支付退款状态(原始码) → 平台退款状态
    private RefundOrderStatusEnum mapRefundStatus(String refundStatus) {
        if (Objects.equals(refundStatus, REFUND_STATUS_SUCCESS)) {
            return RefundOrderStatusEnum.SUCCESS;
        }
        if (Objects.equals(refundStatus, REFUND_STATUS_FAIL)) {
            return RefundOrderStatusEnum.FAIL;
        }
        // 其他状态视为进行中
        return RefundOrderStatusEnum.PROGRESS;
    }
}
