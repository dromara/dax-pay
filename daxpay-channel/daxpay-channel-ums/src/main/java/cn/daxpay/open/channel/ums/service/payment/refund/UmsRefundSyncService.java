package cn.daxpay.open.channel.ums.service.payment.refund;

import cn.daxpay.open.channel.ums.client.UmsChannelClient;
import cn.daxpay.open.channel.ums.client.credential.UmsSdkCredential;
import cn.daxpay.open.channel.ums.client.enums.UmsPayMethod;
import cn.daxpay.open.channel.ums.client.req.UmsRefundSyncReq;
import cn.daxpay.open.channel.ums.client.resp.UmsRefundSyncResp;
import cn.daxpay.open.channel.ums.code.UmsCode;
import cn.daxpay.open.channel.ums.util.UmsDateUtil;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.PayRefundOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 银联商务退款同步业务服务
///
/// 通过 [UmsChannelClient] 调用子应用查询银联商务退款状态。
@Slf4j
@Service
@RequiredArgsConstructor
public class UmsRefundSyncService {

    private final UmsChannelClient umsChannelClient;
    private final PayTradeManager payTradeManager;

    /// 执行银联商务退款同步查询
    public RefundResultBo sync(PayRefundOrder refundOrder, UmsSdkCredential credential) {
        UmsRefundSyncReq req = new UmsRefundSyncReq();
        req.setOutRefundNo(refundOrder.getRefundNo());
        req.setOutTradeNo(refundOrder.getOrderNo());
        // 首期默认扫码退款查询
        req.setMethod(UmsPayMethod.QRCODE);
        req.setCredential(credential);

        // 银联商务扫码退款查询需要 billDate(原订单创建日, 子应用按通道时区转换)
        payTradeManager.findByTradeNo(refundOrder.getOrderNo())
                .ifPresentOrElse(
                        t -> req.setBillDate(t.getCreateTime()),
                        () -> log.warn("银联商务退款同步未查到原交易({}), billDate 未填充, 银商可能拒绝",
                                refundOrder.getOrderNo()));

        DaxResult<UmsRefundSyncResp> result = umsChannelClient.refundSync(req);
        if (result.getCode() != 0) {
            log.error("银联商务通道退款同步失败: refundNo={}, msg={}", refundOrder.getRefundNo(), result.getMsg());
            return new RefundResultBo()
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }

        return toSyncResult(result.getData());
    }

    /// 解析子应用响应
    private RefundResultBo toSyncResult(UmsRefundSyncResp resp) {
        RefundResultBo bo = new RefundResultBo();
        bo.setOutRefundNo(resp.getOutRefundNo());
        // 退款完成时间(银联商务返回东八区本地时间, 由 UmsDateUtil 解析为带偏移的 OffsetDateTime)
        bo.setFinishTime(UmsDateUtil.parseCst(resp.getFinishTime()));
        // 退款金额
        bo.setRefundAmount(resp.getRefundAmount());

        // 统一状态码映射
        return switch (resp.getRefundStatus()) {
            case UmsCode.TRADE_STATUS_SUCCESS -> bo.setComplete(true).setStatus(RefundOrderStatusEnum.SUCCESS);
            case UmsCode.TRADE_STATUS_CLOSED -> bo.setComplete(true).setStatus(RefundOrderStatusEnum.FAIL);
            default -> bo.setComplete(false).setStatus(RefundOrderStatusEnum.PROGRESS);
        };
    }
}
