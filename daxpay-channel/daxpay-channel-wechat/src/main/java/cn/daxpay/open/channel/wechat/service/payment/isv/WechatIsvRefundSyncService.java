package cn.daxpay.open.channel.wechat.service.payment.isv;

import cn.daxpay.open.channel.wechat.client.WechatChannelClient;
import cn.daxpay.open.channel.wechat.client.credential.WechatSdkCredential;
import cn.daxpay.open.channel.wechat.client.req.WechatRefundSyncReq;
import cn.daxpay.open.channel.wechat.client.resp.WechatRefundSyncResp;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayRefundOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 微信服务商退款同步业务服务
///
/// 通过 [WechatChannelClient] 调用子应用 dax-pay-channel-one 服务商端点查询微信退款状态,
/// 将微信退款 status 映射为平台 [RefundOrderStatusEnum]。映射规则与直连模式一致。
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatIsvRefundSyncService {

    private final WechatChannelClient wechatChannelClient;

    /// 微信退款状态
    private static final String STATUS_SUCCESS = "SUCCESS";
    private static final String STATUS_CLOSED = "CLOSED";
    private static final String STATUS_ABNORMAL = "ABNORMAL";

    /// 执行微信服务商退款同步查询
    public RefundResultBo sync(PayRefundOrder refundOrder, WechatSdkCredential credential) {
        // 构建请求(与直连一致)
        var req = new WechatRefundSyncReq();
        req.setOutRefundNo(refundOrder.getRefundNo());
        req.setCredential(credential);

        // 调用子应用服务商端点
        DaxResult<WechatRefundSyncResp> result = wechatChannelClient.isvRefundSync(req);
        if (result.getCode() != 0) {
            log.error("微信服务商通道退款同步失败: refundNo={}, msg={}", refundOrder.getRefundNo(), result.getMsg());
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
