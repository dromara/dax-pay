package cn.daxpay.open.channel.lakala.service.payment;

import cn.daxpay.open.channel.lakala.client.LakalaChannelClient;
import cn.daxpay.open.channel.lakala.client.credential.LakalaSdkCredential;
import cn.daxpay.open.channel.lakala.client.req.LakalaRefundSyncReq;
import cn.daxpay.open.channel.lakala.client.resp.LakalaRefundSyncResp;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayRefundOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 拉卡拉服务商退款同步业务服务
///
/// 通过 [LakalaChannelClient] 调用子应用查询拉卡拉退款最终状态。
@Slf4j
@Service
@RequiredArgsConstructor
public class LakalaRefundSyncService {

    private final LakalaChannelClient lakalaChannelClient;

    /// 同步退款状态
    public RefundResultBo sync(PayRefundOrder refundOrder, LakalaSdkCredential credential) {
        LakalaRefundSyncReq req = new LakalaRefundSyncReq();
        req.setCredential(credential);
        req.setOutRefundNo(refundOrder.getRefundNo());
        req.setOriginTradeNo(refundOrder.getOutRefundNo());

        DaxResult<LakalaRefundSyncResp> result = lakalaChannelClient.refundSync(req);
        RefundResultBo bo = new RefundResultBo();
        if (result.getCode() != 0) {
            bo.setSyncSuccess(false);
            bo.setSyncErrorMsg(result.getMsg());
            return bo;
        }

        LakalaRefundSyncResp resp = result.getData();
        bo.setSyncSuccess(true);
        bo.setOutRefundNo(resp.getTradeNo());
        bo.setFinishTime(resp.getFinishTime());
        bo.setStatus(mapRefundStatus(resp.getRefundStatus()));
        bo.setComplete(Objects.equals(resp.getRefundStatus(), "SUCCESS"));
        return bo;
    }

    /// 拉卡拉退款状态 → 平台退款状态
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
