package cn.daxpay.open.channel.fuyou.service.payment;

import cn.daxpay.open.channel.fuyou.client.FuyouDirectChannelClient;
import cn.daxpay.open.channel.fuyou.client.credential.FuyouDirectSdkCredential;
import cn.daxpay.open.channel.fuyou.client.req.FuyouDirectRefundSyncReq;
import cn.daxpay.open.channel.fuyou.client.resp.FuyouRefundSyncResp;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 富友直连退款同步业务服务
///
/// 通过 [FuyouDirectChannelClient] 调用子应用查询富友直连退款最终状态(`/aggregatePay/refundQuery`)。
/// trans_stat: SUCCESS / PAYERROR / 其他(处理中)。
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouDirectRefundSyncService {

    private final FuyouDirectChannelClient fuyouDirectChannelClient;

    /// 同步退款状态
    public RefundResultBo sync(RefundOrder refundOrder, FuyouDirectSdkCredential credential) {
        FuyouDirectRefundSyncReq req = new FuyouDirectRefundSyncReq();
        req.setCredential(credential);
        // 富友凭 refund_order_no(平台 refundNo) 查询
        req.setOutRefundNo(refundOrder.getRelationOrderNo());

        DaxResult<FuyouRefundSyncResp> result = fuyouDirectChannelClient.refundSync(req);
        RefundResultBo bo = new RefundResultBo();
        if (result.getCode() != 0) {
            bo.setSyncSuccess(false);
            bo.setSyncErrorMsg(result.getMsg());
            return bo;
        }

        FuyouRefundSyncResp resp = result.getData();
        bo.setSyncSuccess(Boolean.TRUE.equals(resp.getSyncSuccess()));
        bo.setSyncErrorMsg(resp.getSyncErrorMsg());
        bo.setOutRefundNo(refundOrder.getOutRefundNo());
        bo.setStatus(mapRefundStatus(resp.getRefundStatus()));
        bo.setComplete(Objects.equals(resp.getRefundStatus(), "SUCCESS"));
        bo.setRefundAmount(resp.getAmount());
        return bo;
    }

    /// 富友退款状态 → 平台退款状态
    private RefundOrderStatusEnum mapRefundStatus(String refundStatus) {
        if (Objects.equals(refundStatus, "SUCCESS")) {
            return RefundOrderStatusEnum.SUCCESS;
        }
        if (Objects.equals(refundStatus, "FAIL")) {
            return RefundOrderStatusEnum.FAIL;
        }
        // PROGRESS 处理中
        return RefundOrderStatusEnum.PROGRESS;
    }
}
