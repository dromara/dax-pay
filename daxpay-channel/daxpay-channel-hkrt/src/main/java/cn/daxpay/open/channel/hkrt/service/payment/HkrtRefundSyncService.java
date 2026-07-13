package cn.daxpay.open.channel.hkrt.service.payment;

import cn.daxpay.open.channel.hkrt.client.HkrtChannelClient;
import cn.daxpay.open.channel.hkrt.client.credential.HkrtSdkCredential;
import cn.daxpay.open.channel.hkrt.client.req.HkrtRefundSyncReq;
import cn.daxpay.open.channel.hkrt.client.resp.HkrtRefundSyncResp;
import cn.daxpay.open.payment.common.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayRefundOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 海科融通服务商退款同步业务服务
///
/// 通过 [HkrtChannelClient] 调用子应用查询海科融通退款最终状态。
@Slf4j
@Service
@RequiredArgsConstructor
public class HkrtRefundSyncService {

    private final HkrtChannelClient hkrtChannelClient;

    /// 同步退款状态
    public RefundResultBo sync(PayRefundOrder refundOrder, HkrtSdkCredential credential) {
        HkrtRefundSyncReq req = new HkrtRefundSyncReq();
        req.setCredential(credential);
        req.setOutRefundNo(refundOrder.getRefundNo());
        req.setOriginTradeNo(refundOrder.getOutRefundNo());

        DaxResult<HkrtRefundSyncResp> result = hkrtChannelClient.refundSync(req);
        RefundResultBo bo = new RefundResultBo();
        if (result.getCode() != 0) {
            bo.setSyncSuccess(false);
            bo.setSyncErrorMsg(result.getMsg());
            return bo;
        }

        HkrtRefundSyncResp resp = result.getData();
        bo.setSyncSuccess(true);
        bo.setOutRefundNo(resp.getTradeNo());
        bo.setFinishTime(resp.getFinishTime());
        bo.setStatus(mapRefundStatus(resp.getRefundStatus()));
        // 退款完成(子应用输出抽象态 SUCCESS)
        bo.setComplete(Objects.equals(resp.getRefundStatus(), "SUCCESS"));
        return bo;
    }

    /// 抽象态(SUCCESS/FAIL/PROCESSING) → 平台退款状态
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
