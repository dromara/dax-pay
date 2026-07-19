package cn.daxpay.open.channel.fuyou.service.payment;

import cn.daxpay.open.channel.fuyou.client.FuyouChannelClient;
import cn.daxpay.open.channel.fuyou.client.credential.FuyouSdkCredential;
import cn.daxpay.open.channel.fuyou.client.req.FuyouRefundReq;
import cn.daxpay.open.channel.fuyou.client.resp.FuyouRefundResp;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.runtime.service.PayTradeContainerFields;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 富友服务商退款执行业务服务
///
/// 通过 [FuyouChannelClient] 调用子应用完成富友退款(`/commonRefund`)。
/// 富友退款为异步处理, 同步调用仅返回富友受理成功, 最终状态需通过退款同步查询。
///
/// 富友退款需原支付订单的关联订单号(mchnt_order_no) + 支付厂商(order_type) + 原支付日期(reserved_origi_dt),
/// 通过 [PayTradeManager] 凭 refundOrder.orderNo(tradeNo) 反查原支付订单。
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouRefundService {

    private final FuyouChannelClient fuyouChannelClient;
    private final PayTradeManager payTradeManager;
    private final PayTradeContainerFields payTradeContainerFields;

    /// 执行退款
    public RefundResultBo refund(RefundOrder refundOrder, FuyouSdkCredential credential) {
        // 反查原支付订单(富友退款需原订单关联订单号 + 支付厂商 + 原支付日期)
        PayTrade originOrder = payTradeManager.findByTradeNo(refundOrder.getTradeNo())
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.originOrderNotExist"));

        FuyouRefundReq req = new FuyouRefundReq();
        req.setCredential(credential);
        var fields = payTradeContainerFields.resolve(originOrder);
        // 原支付订单的关联订单号(富友 mchnt_order_no)
        req.setRelationOrderNo(fields.relationOrderNo());
        // 原支付厂商(富友 order_type)
        req.setTradeProduct(fields.tradeProduct());
        // 退款单号(平台 refundNo, 作为富友 refund_order_no)
        req.setOutRefundNo(refundOrder.getRelationOrderNo());
        // 原订单总金额
        req.setTotalAmount(refundOrder.getOrderAmount());
        // 退款金额
        req.setRefundAmount(refundOrder.getAmount());
        req.setReason(refundOrder.getReason());
        // 原支付完成时间(富友 reserved_origi_dt)
        req.setOriginPayTime(originOrder.getPayTime());

        DaxResult<FuyouRefundResp> result = fuyouChannelClient.refund(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.fuyou.refundFailed", result.getMsg());
        }

        FuyouRefundResp resp = result.getData();
        // 富友退款为异步, 同步调用成功表示已受理, 状态为处理中, 需轮询同步
        return new RefundResultBo()
                .setOutRefundNo(resp.getOutRefundId())
                .setStatus(RefundOrderStatusEnum.PROGRESS)
                .setComplete(false);
    }
}
