package cn.daxpay.open.payment.trade.flow.service;

import cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.trade.flow.dao.FundFlowManager;
import cn.daxpay.open.payment.trade.flow.entity.FundFlow;
import cn.daxpay.open.payment.trade.flow.enums.FundFlowTypeEnum;
import cn.daxpay.open.payment.trade.order.dao.GatewayPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.GatewayPayOrder;
import cn.daxpay.open.payment.trade.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

/// # 资金流水服务
///
/// 收款/退款成功(状态 CAS 落库)后落流水, 与主流程同事务。
/// 幂等: 部分唯一索引(支付按 trade_no / 退款按 refund_no)兜底, 并发双落时忽略后者。
@Slf4j
@Service
@RequiredArgsConstructor
public class FundFlowService {

    private final FundFlowManager fundFlowManager;
    private final PayTradeManager payTradeManager;
    private final NormalPayOrderManager payNormalOrderManager;
    private final GatewayPayOrderManager gatewayPayOrderManager;

    /// 落收款流水(支付/人工确认成功后调用, 幂等)
    public void savePayFlow(PayTrade trade) {
        try {
            FundFlow flow = new FundFlow()
                    .setFlowType(FundFlowTypeEnum.PAY.getCode())
                    .setTradeNo(trade.getTradeNo())
                    .setBizOrderNo(this.resolveBizOrderNo(trade))
                    .setTitle(trade.getTitle())
                    // 对账口径取入账金额(postedAmount), 兜底交易金额
                    .setAmount(trade.getPostedAmount() != null ? trade.getPostedAmount() : trade.getAmount())
                    .setCurrency(trade.getCurrency())
                    .setChannel(trade.getChannel())
                    .setProvider(trade.getProvider())
                    .setChannelMchNo(trade.getChannelMchNo())
                    .setOutOrderNo(trade.getOutOrderNo())
                    .setFinishTime(trade.getPayTime())
                    .setAppId(trade.getAppId());
            // 显式写入商户号, 回调/定时等无 PaymentContext 场景避免 Fill 缺失
            flow.setMchNo(trade.getMchNo());
            fundFlowManager.save(flow);
        } catch (DuplicateKeyException e) {
            log.info("收款流水已存在, 幂等跳过: tradeNo={}", trade.getTradeNo());
        }
    }

    /// 落退款流水(退款成功后调用, 幂等)
    public void saveRefundFlow(RefundOrder refundOrder) {
        try {
            FundFlow flow = new FundFlow()
                    .setFlowType(FundFlowTypeEnum.REFUND.getCode())
                    .setTradeNo(refundOrder.getTradeNo())
                    .setRefundNo(refundOrder.getRefundNo())
                    .setBizOrderNo(refundOrder.getBizOrderNo())
                    .setTitle(refundOrder.getTitle())
                    .setAmount(refundOrder.getAmount())
                    .setCurrency(refundOrder.getCurrency())
                    .setChannel(refundOrder.getChannel())
                    .setProvider(resolveProvider(refundOrder))
                    .setChannelMchNo(refundOrder.getChannelMchNo())
                    .setOutOrderNo(refundOrder.getOutRefundNo())
                    .setFinishTime(refundOrder.getFinishTime())
                    .setAppId(refundOrder.getAppId());
            // 显式写入商户号, 避免无上下文场景踩 Fill
            flow.setMchNo(refundOrder.getMchNo());
            fundFlowManager.save(flow);
        } catch (DuplicateKeyException e) {
            log.info("退款流水已存在, 幂等跳过: refundNo={}", refundOrder.getRefundNo());
        }
    }

    /// 从容器(normal/gateway)取商户业务单号
    private String resolveBizOrderNo(PayTrade trade) {
        if (PayTradeTypeEnum.GATEWAY.getCode().equals(trade.getTradeType())) {
            return gatewayPayOrderManager.findById(trade.getContainerId())
                    .map(GatewayPayOrder::getBizOrderNo).orElse(null);
        }
        return payNormalOrderManager.findById(trade.getContainerId())
                .map(NormalPayOrder::getBizOrderNo).orElse(null);
    }

    /// 退款单无 provider 冗余, 回查原支付交易取支付渠道
    private String resolveProvider(RefundOrder refundOrder) {
        return payTradeManager.findByTradeNo(refundOrder.getTradeNo())
                .map(PayTrade::getProvider).orElse(null);
    }
}
