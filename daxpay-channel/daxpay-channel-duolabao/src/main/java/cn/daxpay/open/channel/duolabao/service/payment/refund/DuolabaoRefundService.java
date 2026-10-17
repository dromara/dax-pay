package cn.daxpay.open.channel.duolabao.service.payment.refund;

import cn.daxpay.open.channel.duolabao.client.DuolabaoChannelClient;
import cn.daxpay.open.channel.duolabao.client.credential.DuolabaoSdkCredential;
import cn.daxpay.open.channel.duolabao.client.req.DuolabaoRefundReq;
import cn.daxpay.open.channel.duolabao.client.resp.DuolabaoRefundResp;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 哆啦宝退款业务服务
///
/// 通过 [DuolabaoChannelClient] 调用子应用发起哆啦宝退款(refundByRequestNum)。
/// 哆啦宝退款为同步受理即成功模式(对齐参考插件能力, 无退款异步通知), 受理即置终态成功。
///
/// requestNum 传原支付的商户单号(平台支付交易号), 不依赖通道订单号。
@Slf4j
@Service
@RequiredArgsConstructor
public class DuolabaoRefundService {

    private final DuolabaoChannelClient duolabaoChannelClient;

    /// 执行哆啦宝退款
    public RefundResultBo refund(RefundOrder refundOrder, DuolabaoSdkCredential credential) {
        DuolabaoRefundReq req = new DuolabaoRefundReq();
        req.setCredential(credential);
        // 退款单号(作为 refundRequestNum 上送)
        req.setOutRefundNo(refundOrder.getRelationOrderNo());
        // 原支付商户单号(即平台支付交易号, 下单时的 requestNum)
        req.setOriginOutTradeNo(refundOrder.getTradeNo());
        req.setAmount(refundOrder.getAmount());
        req.setReason(refundOrder.getReason());

        DaxResult<DuolabaoRefundResp> result = duolabaoChannelClient.refund(req);
        if (result.getCode() != 0) {
            // 哆啦宝退款异常
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.duolabao.refundFailed", result.getMsg());
        }

        DuolabaoRefundResp resp = result.getData();
        RefundResultBo bo = new RefundResultBo()
                .setOutRefundNo(resp.getOutRefundOrderNo())
                .setComplete(Boolean.TRUE.equals(resp.getComplete()))
                .setRefundAmount(resp.getRefundAmount());
        // 哆啦宝退款同步受理即成功
        bo.setStatus(Boolean.TRUE.equals(resp.getComplete())
                ? RefundOrderStatusEnum.SUCCESS
                : RefundOrderStatusEnum.PROGRESS);
        return bo;
    }
}
