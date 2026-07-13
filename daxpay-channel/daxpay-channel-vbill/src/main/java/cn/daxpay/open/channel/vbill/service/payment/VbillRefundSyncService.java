package cn.daxpay.open.channel.vbill.service.payment;

import cn.daxpay.open.channel.vbill.client.VbillChannelClient;
import cn.daxpay.open.channel.vbill.client.credential.VbillSdkCredential;
import cn.daxpay.open.channel.vbill.client.req.VbillRefundSyncReq;
import cn.daxpay.open.channel.vbill.client.resp.VbillRefundSyncResp;
import cn.daxpay.open.payment.common.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayRefundOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 随行付服务商退款同步业务服务
///
/// 通过 [VbillChannelClient] 调用子应用查询随行付退款最终状态(`/query/refundQuery`)。
/// tranSts: REFUNDSUC / REFUNDFAIL / REFUNDING。
@Slf4j
@Service
@RequiredArgsConstructor
public class VbillRefundSyncService {

    private final VbillChannelClient vbillChannelClient;

    /// 同步退款状态
    public RefundResultBo sync(PayRefundOrder refundOrder, VbillSdkCredential credential) {
        VbillRefundSyncReq req = new VbillRefundSyncReq();
        req.setCredential(credential);
        // 优先用网关退款单号 uuid, 兜底用商户退款单号
        req.setOutRefundOrderNo(refundOrder.getOutRefundNo());
        req.setOutRefundNo(refundOrder.getRefundNo());

        DaxResult<VbillRefundSyncResp> result = vbillChannelClient.refundSync(req);
        RefundResultBo bo = new RefundResultBo();
        if (result.getCode() != 0) {
            bo.setSyncSuccess(false);
            bo.setSyncErrorMsg(result.getMsg());
            return bo;
        }

        VbillRefundSyncResp resp = result.getData();
        bo.setSyncSuccess(true);
        bo.setOutRefundNo(resp.getOutRefundOrderNo());
        bo.setFinishTime(resp.getFinishTime());
        bo.setStatus(mapRefundStatus(resp.getRefundStatus()));
        bo.setComplete(Objects.equals(resp.getRefundStatus(), "REFUNDSUC"));
        return bo;
    }

    /// 随行付退款状态 → 平台退款状态
    private RefundOrderStatusEnum mapRefundStatus(String refundStatus) {
        if (Objects.equals(refundStatus, "REFUNDSUC")) {
            return RefundOrderStatusEnum.SUCCESS;
        }
        if (Objects.equals(refundStatus, "REFUNDFAIL")) {
            return RefundOrderStatusEnum.FAIL;
        }
        // REFUNDING 处理中
        return RefundOrderStatusEnum.PROGRESS;
    }
}
