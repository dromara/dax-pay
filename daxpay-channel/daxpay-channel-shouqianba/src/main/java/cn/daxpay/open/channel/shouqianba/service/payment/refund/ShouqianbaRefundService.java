package cn.daxpay.open.channel.shouqianba.service.payment.refund;

import cn.daxpay.open.channel.shouqianba.client.ShouqianbaChannelClient;
import cn.daxpay.open.channel.shouqianba.client.credential.ShouqianbaSdkCredential;
import cn.daxpay.open.channel.shouqianba.client.req.ShouqianbaRefundReq;
import cn.daxpay.open.channel.shouqianba.client.resp.ShouqianbaRefundResp;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 收钱吧退款业务服务
///
/// 通过 [ShouqianbaChannelClient] 调用子应用发起收钱吧 upay/v2/refund 退款。
/// 退款为同步终态: REFUND_SUCCESS 即退款成功(直接置 SUCCESS), 无退款异步通知与退款查询接口。
@Slf4j
@Service
@RequiredArgsConstructor
public class ShouqianbaRefundService {

    private final ShouqianbaChannelClient shouqianbaChannelClient;

    /// 执行收钱吧退款
    public RefundResultBo refund(RefundOrder refundOrder, ShouqianbaSdkCredential credential) {
        ShouqianbaRefundReq req = new ShouqianbaRefundReq();
        req.setOutRefundNo(refundOrder.getRelationOrderNo());
        // 原支付订单的收钱吧流水号 sn(回调/查单获得, 退款必需)
        req.setOriginOutOrderNo(refundOrder.getOutOrderNo());
        req.setAmount(refundOrder.getAmount());
        req.setReason(refundOrder.getReason());
        req.setCredential(credential);

        DaxResult<ShouqianbaRefundResp> result = shouqianbaChannelClient.refund(req);
        if (result.getCode() != 0) {
            log.error("收钱吧通道退款失败: refundNo={}, msg={}", refundOrder.getRefundNo(), result.getMsg());
            return new RefundResultBo()
                    .setComplete(false)
                    .setStatus(RefundOrderStatusEnum.FAIL)
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }

        // 退款为同步终态: REFUND_SUCCESS 即成功
        return new RefundResultBo()
                .setComplete(Boolean.TRUE.equals(result.getData().getComplete()))
                .setStatus(RefundOrderStatusEnum.SUCCESS);
    }
}
