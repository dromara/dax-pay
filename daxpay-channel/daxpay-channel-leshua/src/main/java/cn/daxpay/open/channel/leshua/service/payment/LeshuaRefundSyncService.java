package cn.daxpay.open.channel.leshua.service.payment;

import cn.daxpay.open.channel.leshua.client.LeshuaChannelClient;
import cn.daxpay.open.channel.leshua.client.credential.LeshuaSdkCredential;
import cn.daxpay.open.channel.leshua.client.req.LeshuaRefundSyncReq;
import cn.daxpay.open.channel.leshua.client.resp.LeshuaRefundSyncResp;
import cn.daxpay.open.channel.leshua.code.LeshuaCode;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayRefundOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 乐刷服务商退款同步业务服务
///
/// 通过 [LeshuaChannelClient] 调用子应用查询乐刷退款最终状态。
@Slf4j
@Service
@RequiredArgsConstructor
public class LeshuaRefundSyncService {

    private final LeshuaChannelClient leshuaChannelClient;

    /// 查询退款状态
    public RefundResultBo sync(PayRefundOrder refundOrder, LeshuaSdkCredential credential) {
        LeshuaRefundSyncReq req = new LeshuaRefundSyncReq();
        req.setCredential(credential);
        req.setLeshuaOrderId(refundOrder.getOutOrderNo());
        req.setLeshuaRefundId(refundOrder.getOutRefundNo());
        req.setOutRefundNo(refundOrder.getRefundNo());

        DaxResult<LeshuaRefundSyncResp> result = leshuaChannelClient.refundSync(req);
        RefundResultBo bo = new RefundResultBo();
        if (result.getCode() != 0) {
            bo.setComplete(false);
            return bo;
        }

        LeshuaRefundSyncResp resp = result.getData();
        bo.setOutRefundNo(resp.getLeshuaRefundId())
                .setFinishTime(resp.getFinishTime())
                .setComplete(Objects.equals(resp.getRefundStatus(), LeshuaCode.REFUND_STATUS_SUCCESS));
        bo.setStatus(mapRefundStatus(resp.getRefundStatus()));
        return bo;
    }

    /// 乐刷退款 status → 平台退款状态
    private RefundOrderStatusEnum mapRefundStatus(String status) {
        if (Objects.equals(status, LeshuaCode.REFUND_STATUS_SUCCESS)) {
            return RefundOrderStatusEnum.SUCCESS;
        }
        if (Objects.equals(status, LeshuaCode.REFUND_STATUS_FAIL)) {
            return RefundOrderStatusEnum.FAIL;
        }
        return RefundOrderStatusEnum.PROGRESS;
    }
}
