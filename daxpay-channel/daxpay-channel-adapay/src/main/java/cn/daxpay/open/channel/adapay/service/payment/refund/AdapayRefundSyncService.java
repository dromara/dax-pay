package cn.daxpay.open.channel.adapay.service.payment.refund;

import cn.daxpay.open.channel.adapay.client.AdapayChannelClient;
import cn.daxpay.open.channel.adapay.client.credential.AdapaySdkCredential;
import cn.daxpay.open.channel.adapay.client.req.AdapayRefundSyncReq;
import cn.daxpay.open.channel.adapay.client.resp.AdapayRefundSyncResp;
import cn.daxpay.open.channel.adapay.code.AdapayCode;
import cn.daxpay.open.channel.adapay.util.AdapayDateUtil;
import cn.daxpay.open.payment.common.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.core.trade.bo.RefundResultBo;
import cn.daxpay.open.payment.core.trade.entity.PayRefundOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 汇付天下退款同步业务服务
///
/// 通过 [AdapayChannelClient] 调用子应用查询汇付天下退款状态。
/// 用平台退款单号(refund_order_no)查询。
@Slf4j
@Service
@RequiredArgsConstructor
public class AdapayRefundSyncService {

    private final AdapayChannelClient adapayChannelClient;

    /// 执行汇付天下退款同步查询
    public RefundResultBo sync(PayRefundOrder refundOrder, AdapaySdkCredential credential) {
        AdapayRefundSyncReq req = new AdapayRefundSyncReq();
        req.setOutRefundNo(refundOrder.getRefundNo());
        req.setCredential(credential);

        DaxResult<AdapayRefundSyncResp> result = adapayChannelClient.refundSync(req);
        if (result.getCode() != 0) {
            log.error("汇付天下通道退款同步失败: refundNo={}, msg={}", refundOrder.getRefundNo(), result.getMsg());
            return new RefundResultBo()
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }

        return toSyncResult(result.getData());
    }

    /// 解析子应用响应
    private RefundResultBo toSyncResult(AdapayRefundSyncResp resp) {
        RefundResultBo bo = new RefundResultBo();
        bo.setOutRefundNo(resp.getOutRefundNo());
        bo.setFinishTime(AdapayDateUtil.parse(resp.getFinishTime()));
        bo.setRefundAmount(resp.getRefundAmount());

        // 统一状态码映射
        return switch (resp.getRefundStatus()) {
            case AdapayCode.TRADE_STATUS_SUCCESS -> bo.setComplete(true).setStatus(RefundOrderStatusEnum.SUCCESS);
            case AdapayCode.TRADE_STATUS_CLOSED -> bo.setComplete(true).setStatus(RefundOrderStatusEnum.FAIL);
            default -> bo.setComplete(false).setStatus(RefundOrderStatusEnum.PROGRESS);
        };
    }
}
