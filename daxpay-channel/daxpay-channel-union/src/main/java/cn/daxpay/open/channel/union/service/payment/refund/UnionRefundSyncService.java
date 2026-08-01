package cn.daxpay.open.channel.union.service.payment.refund;

import cn.daxpay.open.channel.union.client.UnionChannelClient;
import cn.daxpay.open.channel.union.client.credential.UnionSdkCredential;
import cn.daxpay.open.channel.union.client.enums.UnionPayMethod;
import cn.daxpay.open.channel.union.client.req.UnionRefundSyncReq;
import cn.daxpay.open.channel.union.client.resp.UnionRefundSyncResp;
import cn.daxpay.open.channel.union.code.UnionCode;
import cn.daxpay.open.channel.union.util.UnionDateUtil;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 云闪付退款同步业务服务
///
/// 通过 [UnionChannelClient] 调用子应用查询银联退款状态。
@Slf4j
@Service
@RequiredArgsConstructor
public class UnionRefundSyncService {

    private final UnionChannelClient unionChannelClient;

    /// 执行云闪付退款同步
    public RefundResultBo sync(RefundOrder refundOrder, UnionSdkCredential credential, UnionPayMethod method) {
        UnionRefundSyncReq req = new UnionRefundSyncReq();
        req.setOutRefundNo(refundOrder.getRelationOrderNo());
        req.setMethod(method);
        req.setCredential(credential);

        DaxResult<UnionRefundSyncResp> result = unionChannelClient.refundSync(req);
        if (result.getCode() != 0) {
            log.error("云闪付退款同步失败: refundNo={}, msg={}", refundOrder.getRefundNo(), result.getMsg());
            return new RefundResultBo()
                    .setComplete(false)
                    .setStatus(RefundOrderStatusEnum.PROGRESS)
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }
        return toRefundSyncResult(result.getData());
    }

    /// 解析子应用响应
    private RefundResultBo toRefundSyncResult(UnionRefundSyncResp resp) {
        RefundResultBo bo = new RefundResultBo();
        bo.setFinishTime(UnionDateUtil.parseCst(resp.getFinishTime()));
        String refundStatus = resp.getRefundStatus();
        if (StrUtil.isBlank(refundStatus)) {
            return bo.setComplete(false)
                    .setStatus(RefundOrderStatusEnum.PROGRESS)
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(StrUtil.blankToDefault(resp.getErrorMsg(), "云闪付退款同步查询失败"));
        }
        return switch (refundStatus) {
            case UnionCode.REFUND_STATUS_SUCCESS -> bo.setComplete(true).setStatus(RefundOrderStatusEnum.SUCCESS);
            case UnionCode.TRADE_STATUS_CLOSED ->
                    bo.setComplete(true).setStatus(RefundOrderStatusEnum.FAIL);
            default -> bo.setComplete(false).setStatus(RefundOrderStatusEnum.PROGRESS);
        };
    }
}
