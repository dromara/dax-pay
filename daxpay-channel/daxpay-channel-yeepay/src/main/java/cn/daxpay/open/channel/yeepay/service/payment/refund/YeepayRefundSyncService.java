package cn.daxpay.open.channel.yeepay.service.payment.refund;

import cn.daxpay.open.channel.yeepay.client.YeepayChannelClient;
import cn.daxpay.open.channel.yeepay.client.credential.YeepaySdkCredential;
import cn.daxpay.open.channel.yeepay.client.req.YeepayRefundSyncReq;
import cn.daxpay.open.channel.yeepay.client.resp.YeepayRefundSyncResp;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 易宝退款同步业务服务
///
/// 通过 [YeepayChannelClient] 调用子应用查询易宝退款状态。
@Slf4j
@Service
@RequiredArgsConstructor
public class YeepayRefundSyncService {

    private final YeepayChannelClient yeepayChannelClient;

    /// 执行易宝退款同步查询
    public RefundResultBo sync(RefundOrder refundOrder, YeepaySdkCredential credential) {
        YeepayRefundSyncReq req = new YeepayRefundSyncReq();
        req.setOriginOutTradeNo(refundOrder.getTradeNo());
        req.setOutRefundNo(refundOrder.getRelationOrderNo());
        req.setCredential(credential);

        DaxResult<YeepayRefundSyncResp> result = yeepayChannelClient.refundSync(req);
        if (result.getCode() != 0) {
            log.error("易宝通道退款同步失败: refundNo={}, msg={}", refundOrder.getRefundNo(), result.getMsg());
            return new RefundResultBo()
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }

        return toSyncResult(result.getData());
    }

    /// 解析子应用响应
    private RefundResultBo toSyncResult(YeepayRefundSyncResp resp) {
        RefundResultBo bo = new RefundResultBo();
        bo.setOutRefundNo(resp.getTradeNo());
        bo.setFinishTime(resp.getFinishTime());
        bo.setRefundAmount(resp.getAmount());

        // 统一状态码映射
        return switch (resp.getTradeStatus()) {
            case "SUCCESS" -> bo.setComplete(true).setStatus(RefundOrderStatusEnum.SUCCESS);
            case "FAIL" -> bo.setComplete(true).setStatus(RefundOrderStatusEnum.FAIL);
            default -> bo.setComplete(false).setStatus(RefundOrderStatusEnum.PROGRESS);
        };
    }
}
