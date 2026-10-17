package cn.daxpay.open.channel.yisheng.service.payment.refund;

import cn.daxpay.open.channel.yisheng.client.YishengChannelClient;
import cn.daxpay.open.channel.yisheng.client.credential.YishengSdkCredential;
import cn.daxpay.open.channel.yisheng.client.req.YishengRefundReq;
import cn.daxpay.open.channel.yisheng.client.resp.YishengRefundResp;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 易生退款业务服务
///
/// 通过 [YishengChannelClient] 调用子应用发起易生 easypay.merchant.refund 退款。
/// 退款为同步终态: 受理成功(code=00)即退款成功(直接置 SUCCESS), 无退款异步通知与退款查询接口(对齐参考实现能力)。
@Slf4j
@Service
@RequiredArgsConstructor
public class YishengRefundService {

    private final YishengChannelClient yishengChannelClient;

    /// 执行易生退款
    public RefundResultBo refund(RefundOrder refundOrder, YishengSdkCredential credential) {
        YishengRefundReq req = new YishengRefundReq();
        req.setOutRefundNo(refundOrder.getRelationOrderNo());
        // 原支付时上送的商户订单号(平台支付交易号)
        req.setOriginOutTradeNo(refundOrder.getTradeNo());
        req.setAmount(refundOrder.getAmount());
        req.setCredential(credential);

        DaxResult<YishengRefundResp> result = yishengChannelClient.refund(req);
        if (result.getCode() != 0) {
            log.error("易生通道退款失败: refundNo={}, msg={}", refundOrder.getRefundNo(), result.getMsg());
            return new RefundResultBo()
                    .setComplete(false)
                    .setStatus(RefundOrderStatusEnum.FAIL)
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }

        // 退款为同步终态: 受理成功即成功
        return new RefundResultBo()
                .setComplete(Boolean.TRUE.equals(result.getData().getComplete()))
                .setStatus(RefundOrderStatusEnum.SUCCESS);
    }
}
