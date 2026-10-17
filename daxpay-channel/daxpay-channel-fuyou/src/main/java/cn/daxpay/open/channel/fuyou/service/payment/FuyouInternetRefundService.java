package cn.daxpay.open.channel.fuyou.service.payment;

import cn.daxpay.open.channel.fuyou.client.FuyouInternetChannelClient;
import cn.daxpay.open.channel.fuyou.client.credential.FuyouRsaSdkCredential;
import cn.daxpay.open.channel.fuyou.client.req.FuyouInternetRefundReq;
import cn.daxpay.open.channel.fuyou.client.resp.FuyouRefundResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
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

/// # 富友互联网直连退款执行业务服务
///
/// 通过 [FuyouInternetChannelClient] 调用子应用完成富友互联网退款(aggposRefund)。
/// 富友互联网退款受理成功即返回退款金额(参考实现按同步终态处理), 无独立退款查询接口,
/// 本服务将受理成功直接映射为退款成功(complete=true), 无退款同步策略。
///
/// 退款需原支付下单日期(pay_order_date)+ 原商户订单号(pay_order_id),
/// 通过 [PayTradeManager] 凭 refundOrder.orderNo(tradeNo) 反查原支付订单。
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouInternetRefundService {

    private final FuyouInternetChannelClient fuyouInternetChannelClient;
    private final PayTradeManager payTradeManager;
    private final PayTradeContainerFields payTradeContainerFields;

    /// 执行退款
    public RefundResultBo refund(RefundOrder refundOrder, FuyouRsaSdkCredential credential) {
        // 反查原支付订单(富友退款需原订单商户订单号 + 原下单日期)
        PayTrade originOrder = payTradeManager.findByTradeNo(refundOrder.getTradeNo())
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.originOrderNotExist"));

        FuyouInternetRefundReq req = new FuyouInternetRefundReq();
        req.setCredential(credential);
        var fields = payTradeContainerFields.resolve(originOrder);
        // 原支付订单的商户订单号(富友 pay_order_id = 平台交易号)
        req.setRelationOrderNo(fields.relationOrderNo());
        // 退款单号(平台退款关联号, 作为富友 refund_order_id)
        req.setOutRefundNo(refundOrder.getRelationOrderNo());
        // 退款金额(单位: 分)
        req.setRefundAmount(refundOrder.getAmount());
        // 原支付下单时间(富友 pay_order_date)
        req.setOriginOrderTime(originOrder.getCreateTime());

        DaxResult<FuyouRefundResp> result = fuyouInternetChannelClient.refund(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.fuyou.refundFailed", result.getMsg());
        }

        FuyouRefundResp resp = result.getData();
        // 富友互联网退款为同步终态, 受理成功即退款成功
        return new RefundResultBo()
                .setOutRefundNo(resp.getOutRefundId())
                .setStatus(RefundOrderStatusEnum.SUCCESS)
                .setComplete(true)
                .setRefundAmount(refundOrder.getAmount());
    }
}
