package cn.daxpay.open.channel.baofu.service.payment.refund;

import cn.daxpay.open.channel.baofu.client.BaofuChannelClient;
import cn.daxpay.open.channel.baofu.client.credential.BaofuSdkCredential;
import cn.daxpay.open.channel.baofu.client.req.BaofuRefundReq;
import cn.daxpay.open.channel.baofu.client.resp.BaofuRefundResp;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 宝付退款业务服务
///
/// 通过 [BaofuChannelClient] 调用子应用发起宝付 order_refund 退款。
/// 退款为同步终态: 受理成功(resultCode=SUCCESS)即退款成功, 无退款异步通知与退款查询接口(对齐参考实现能力)。
///
/// 原宝付交易号缺省时子应用先按商户订单号查单补齐, 本服务不做前置拦截。
@Slf4j
@Service
@RequiredArgsConstructor
public class BaofuRefundService {

    private final BaofuChannelClient baofuChannelClient;

    /// 执行宝付退款
    public RefundResultBo refund(RefundOrder refundOrder, BaofuSdkCredential credential) {
        BaofuRefundReq req = new BaofuRefundReq();
        req.setOutRefundNo(refundOrder.getRelationOrderNo());
        // 原支付时上送的商户订单号(平台支付交易号)
        req.setOriginOutTradeNo(refundOrder.getTradeNo());
        // 原支付订单的宝付交易号(tradeNo; 可空, 子应用缺省时先查单补齐)
        req.setOriginOutOrderNo(refundOrder.getOutOrderNo());
        req.setAmount(refundOrder.getAmount());
        req.setReason(refundOrder.getReason());
        req.setCredential(credential);

        DaxResult<BaofuRefundResp> result = baofuChannelClient.refund(req);
        if (result.getCode() != 0) {
            log.error("宝付通道退款失败: refundNo={}, msg={}", refundOrder.getRefundNo(), result.getMsg());
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
