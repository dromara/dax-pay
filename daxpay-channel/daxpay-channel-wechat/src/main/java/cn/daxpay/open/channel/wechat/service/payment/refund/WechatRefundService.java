package cn.daxpay.open.channel.wechat.service.payment.refund;

import cn.daxpay.open.channel.wechat.client.WechatChannelClient;
import cn.daxpay.open.channel.wechat.client.credential.WechatSdkCredential;
import cn.daxpay.open.channel.wechat.client.req.WechatRefundReq;
import cn.daxpay.open.channel.wechat.client.resp.WechatRefundResp;
import cn.daxpay.open.payment.common.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayRefundOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 微信退款业务服务
///
/// 通过 [WechatChannelClient] 调用子应用 dax-pay-channel-one 发起微信退款。
/// 请求构建、结果判定在本类中完成。
///
/// 退款状态 status 判定:
/// - SUCCESS / CLOSED → 终态(complete=true), 退款成功
/// - PROCESSING / ABNORMAL → 未终态(complete=false), 需退款同步查询确认最终状态
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatRefundService {

    private final WechatChannelClient wechatChannelClient;

    /// 微信退款状态
    private static final String STATUS_SUCCESS = "SUCCESS";
    private static final String STATUS_CLOSED = "CLOSED";

    /// 执行微信退款
    ///
    /// @param refundOrder 退款订单(refundNo 作为 out_refund_no, orderNo 作为 out_trade_no, outOrderNo 作为 transaction_id)
    /// @param credential  通道调用凭证
    /// @return 退款结果(含映射后的退款状态)
    public RefundResultBo refund(PayRefundOrder refundOrder, WechatSdkCredential credential) {
        // 构建请求
        WechatRefundReq req = new WechatRefundReq();
        req.setOutTradeNo(refundOrder.getOrderNo());
        req.setTransactionId(refundOrder.getOutOrderNo());
        req.setOutRefundNo(refundOrder.getRefundNo());
        // 微信退款需原订单总额(orderAmount)与退款金额(amount)
        req.setTotalAmount(refundOrder.getOrderAmount());
        req.setRefundAmount(refundOrder.getAmount());
        req.setReason(refundOrder.getReason());
        req.setNotifyUrl(refundOrder.getNotifyUrl());
        req.setCredential(credential);

        // 调用子应用
        DaxResult<WechatRefundResp> result = wechatChannelClient.refund(req);
        if (result.getCode() != 0) {
            log.error("微信通道退款失败: refundNo={}, msg={}", refundOrder.getRefundNo(), result.getMsg());
            RefundResultBo bo = new RefundResultBo();
            bo.setComplete(false)
                    .setStatus(RefundOrderStatusEnum.FAIL)
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
            return bo;
        }

        return toRefundResult(result.getData());
    }

    /// 解析子应用响应
    private RefundResultBo toRefundResult(WechatRefundResp resp) {
        RefundResultBo bo = new RefundResultBo();
        bo.setOutRefundNo(resp.getOutRefundNo());
        bo.setFinishTime(resp.getFinishTime());
        bo.setRefundAmount(resp.getRefundAmount());
        // SUCCESS / CLOSED → 退款终态成功; 否则退款中, 需同步查询确认
        if (STATUS_SUCCESS.equals(resp.getStatus()) || STATUS_CLOSED.equals(resp.getStatus())) {
            bo.setComplete(true)
                    .setStatus(RefundOrderStatusEnum.SUCCESS);
        } else {
            bo.setComplete(false)
                    .setStatus(RefundOrderStatusEnum.PROGRESS);
        }
        return bo;
    }
}
