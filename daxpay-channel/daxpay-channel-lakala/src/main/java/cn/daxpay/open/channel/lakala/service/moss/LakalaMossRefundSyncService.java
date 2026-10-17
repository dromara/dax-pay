package cn.daxpay.open.channel.lakala.service.moss;

import cn.daxpay.open.channel.lakala.client.LakalaChannelClient;
import cn.daxpay.open.channel.lakala.client.credential.MossSdkCredential;
import cn.daxpay.open.channel.lakala.client.req.MossRefundSyncReq;
import cn.daxpay.open.channel.lakala.client.resp.MossRefundSyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 拉卡拉 MOSS 退款同步业务服务
///
/// 通过 [LakalaChannelClient] 调用子应用查询 MOSS 退款最终状态。
@Slf4j
@Service
@RequiredArgsConstructor
public class LakalaMossRefundSyncService {

    private final LakalaChannelClient lakalaChannelClient;

    /// 同步退款状态
    public RefundResultBo sync(RefundOrder refundOrder, MossSdkCredential credential) {
        MossRefundSyncReq req = new MossRefundSyncReq();
        req.setCredential(credential);
        req.setOutRefundNo(refundOrder.getRelationOrderNo());

        DaxResult<MossRefundSyncResp> result = lakalaChannelClient.mossRefundSync(req);
        RefundResultBo bo = new RefundResultBo();
        if (result.getCode() != 0) {
            bo.setSyncSuccess(false);
            bo.setSyncErrorMsg(result.getMsg());
            return bo;
        }

        MossRefundSyncResp resp = result.getData();
        bo.setSyncSuccess(true);
        bo.setOutRefundNo(resp.getTradeNo());
        bo.setFinishTime(resp.getFinishTime());
        bo.setStatus(mapRefundStatus(resp.getRefundStatus()));
        bo.setComplete(Objects.equals(resp.getRefundStatus(), "SUCCESS"));
        return bo;
    }

    /// MOSS 退款状态 → 平台退款状态
    private RefundOrderStatusEnum mapRefundStatus(String refundStatus) {
        if (Objects.equals(refundStatus, "SUCCESS")) {
            return RefundOrderStatusEnum.SUCCESS;
        }
        if (Objects.equals(refundStatus, "FAIL")) {
            return RefundOrderStatusEnum.FAIL;
        }
        // 处理中
        return RefundOrderStatusEnum.PROGRESS;
    }
}
