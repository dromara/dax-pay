package cn.daxpay.open.channel.wechat.service.payment.refund;

import cn.daxpay.open.channel.wechat.client.WechatChannelClient;
import cn.daxpay.open.channel.wechat.client.credential.WechatSdkCredential;
import cn.daxpay.open.channel.wechat.client.req.WechatRefundSyncReq;
import cn.daxpay.open.channel.wechat.client.resp.WechatRefundSyncResp;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 微信退款同步业务服务
///
/// 通过 [WechatChannelClient] 调用子应用 dax-pay-channel-one 查询微信退款状态,
/// 将微信退款 status 映射为平台 [RefundOrderStatusEnum]。
///
/// 映射规则(参照商业版 WechatRefundSyncService):
/// - SUCCESS → SUCCESS(退款成功)
/// - CLOSED → CLOSE(退款关闭)
/// - PROCESSING → PROGRESS(退款中)
/// - ABNORMAL → FAIL(退款异常)
/// - 查询失败 → syncSuccess=false
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatRefundSyncService {

    private final WechatChannelClient wechatChannelClient;

    /// 微信退款状态
    private static final String STATUS_SUCCESS = "SUCCESS";
    private static final String STATUS_CLOSED = "CLOSED";
    private static final String STATUS_PROCESSING = "PROCESSING";
    private static final String STATUS_ABNORMAL = "ABNORMAL";

    /// 执行微信退款同步查询
    ///
    /// @param refundOrder 退款订单(refundNo 作为 out_refund_no)
    /// @param credential  通道调用凭证
    /// @return 同步结果(含映射后的退款状态)
    public RefundResultBo sync(RefundOrder refundOrder, WechatSdkCredential credential) {
        // 构建请求
        var req = new WechatRefundSyncReq();
        req.setOutRefundNo(refundOrder.getRelationOrderNo());
        req.setCredential(credential);

        // 调用子应用
        DaxResult<WechatRefundSyncResp> result = wechatChannelClient.refundSync(req);
        if (result.getCode() != 0) {
            log.error("微信通道退款同步失败: refundNo={}, msg={}", refundOrder.getRefundNo(), result.getMsg());
            RefundResultBo bo = new RefundResultBo();
            bo.setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
            return bo;
        }

        return toSyncResult(result.getData());
    }

    /// 解析子应用响应, 映射 status → [RefundOrderStatusEnum]
    private RefundResultBo toSyncResult(WechatRefundSyncResp resp) {
        RefundResultBo bo = new RefundResultBo();
        bo.setOutRefundNo(resp.getOutRefundNo());
        String status = resp.getStatus();

        // 退款成功
        if (STATUS_SUCCESS.equals(status)) {
            return bo.setComplete(true)
                    .setStatus(RefundOrderStatusEnum.SUCCESS)
                    .setFinishTime(resp.getFinishTime())
                    .setRefundAmount(resp.getRefundAmount());
        }

        // 退款关闭
        if (STATUS_CLOSED.equals(status)) {
            return bo.setComplete(true)
                    .setStatus(RefundOrderStatusEnum.CLOSE)
                    .setFinishTime(resp.getFinishTime());
        }

        // 退款异常
        if (STATUS_ABNORMAL.equals(status)) {
            return bo.setComplete(true)
                    .setStatus(RefundOrderStatusEnum.FAIL);
        }

        // 退款中(PROCESSING 或未知)
        return bo.setComplete(false)
                .setStatus(RefundOrderStatusEnum.PROGRESS);
    }
}
