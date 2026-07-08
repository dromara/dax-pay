package cn.daxpay.open.channel.hmpay.service.payment;

import cn.daxpay.open.channel.hmpay.client.HmpayChannelClient;
import cn.daxpay.open.channel.hmpay.client.credential.HmpaySdkCredential;
import cn.daxpay.open.channel.hmpay.client.req.HmpayRefundSyncReq;
import cn.daxpay.open.channel.hmpay.client.resp.HmpayRefundSyncResp;
import cn.daxpay.open.payment.common.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.core.trade.bo.RefundResultBo;
import cn.daxpay.open.payment.core.trade.entity.PayRefundOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 河马付服务商退款同步业务服务
///
/// 通过 [HmpayChannelClient] 调用子应用查询河马付(杉德)退款状态。
@Slf4j
@Service
@RequiredArgsConstructor
public class HmpayRefundSyncService {

    private final HmpayChannelClient hmpayChannelClient;

    /// 查询退款状态
    public RefundResultBo sync(PayRefundOrder refundOrder, HmpaySdkCredential credential) {
        HmpayRefundSyncReq req = new HmpayRefundSyncReq();
        req.setCredential(credential);
        req.setOutTradeNo(refundOrder.getOrderNo());
        req.setOutRefundNo(refundOrder.getRefundNo());

        DaxResult<HmpayRefundSyncResp> result = hmpayChannelClient.refundSync(req);
        RefundResultBo bo = new RefundResultBo();
        if (result.getCode() != 0) {
            bo.setComplete(false);
            bo.setStatus(RefundOrderStatusEnum.PROGRESS);
            return bo;
        }

        HmpayRefundSyncResp resp = result.getData();
        bo.setOutRefundNo(resp.getTradeNo())
                .setFinishTime(resp.getFinishTime());
        // 退款状态: SUCCESS→SUCCESS, FAIL→FAIL, 其他→PROGRESS
        boolean success = Objects.equals(resp.getRefundState(), "SUCCESS");
        bo.setStatus(success ? RefundOrderStatusEnum.SUCCESS : RefundOrderStatusEnum.PROGRESS);
        bo.setComplete(success);
        return bo;
    }
}
