package cn.daxpay.open.channel.sheng.service.payment;

import cn.daxpay.open.channel.sheng.client.ShengChannelClient;
import cn.daxpay.open.channel.sheng.client.credential.ShengSdkCredential;
import cn.daxpay.open.channel.sheng.client.req.ShengRefundSyncReq;
import cn.daxpay.open.channel.sheng.client.resp.ShengRefundSyncResp;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 盛付通退款同步业务服务
///
/// 通过 [ShengChannelClient] 调用子应用查询盛付通退款最终状态。
/// 子应用返回标准化退款状态(SUCCESS / FAIL / PROCESSING)。
@Slf4j
@Service
@RequiredArgsConstructor
public class ShengRefundSyncService {

    private final ShengChannelClient shengChannelClient;

    /// 同步退款状态
    public RefundResultBo sync(RefundOrder refundOrder, ShengSdkCredential credential) {
        ShengRefundSyncReq req = new ShengRefundSyncReq();
        req.setCredential(credential);
        req.setOutRefundNo(refundOrder.getRelationOrderNo());
        req.setOriginTradeNo(refundOrder.getOutRefundNo());

        DaxResult<ShengRefundSyncResp> result = shengChannelClient.refundSync(req);
        RefundResultBo bo = new RefundResultBo();
        if (result.getCode() != 0) {
            // 同步失败(不抛异常, 由核心层决定重试)
            bo.setSyncSuccess(false);
            bo.setSyncErrorMsg(result.getMsg());
            return bo;
        }

        ShengRefundSyncResp resp = result.getData();
        bo.setSyncSuccess(true);
        bo.setOutRefundNo(resp.getTradeNo());
        bo.setFinishTime(resp.getFinishTime());
        bo.setStatus(mapRefundStatus(resp.getRefundStatus()));
        bo.setComplete(Objects.equals(resp.getRefundStatus(), "SUCCESS"));
        return bo;
    }

    /// 标准化退款状态 → 平台退款状态
    private RefundOrderStatusEnum mapRefundStatus(String refundStatus) {
        if (Objects.equals(refundStatus, "SUCCESS")) {
            return RefundOrderStatusEnum.SUCCESS;
        }
        if (Objects.equals(refundStatus, "FAIL")) {
            return RefundOrderStatusEnum.FAIL;
        }
        // 处理中(PROCESSING 及其他)
        return RefundOrderStatusEnum.PROGRESS;
    }
}
