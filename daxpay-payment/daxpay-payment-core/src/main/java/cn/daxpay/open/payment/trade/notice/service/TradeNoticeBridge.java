package cn.daxpay.open.payment.trade.notice.service;

import cn.daxpay.open.payment.trade.notice.command.NoticeDispatchCommand;
import cn.daxpay.open.payment.trade.order.convert.GatewayPayOrderConvert;
import cn.daxpay.open.payment.trade.order.convert.NormalPayOrderConvert;
import cn.daxpay.open.payment.trade.order.convert.RefundOrderConvert;
import cn.daxpay.open.payment.trade.order.dao.GatewayPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.trade.order.entity.GatewayPayOrder;
import cn.daxpay.open.payment.trade.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeContentModeEnum;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeEventEnum;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeFormatEnum;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeTransportEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 支付/退款域 → 出站通知桥接
///
/// 组装快照并调用 [NoticeDispatcher]，避免业务服务直接依赖通知细节
@Slf4j
@Service
@RequiredArgsConstructor
public class TradeNoticeBridge {

    private final NoticeDispatcher noticeDispatcher;
    private final NormalPayOrderManager normalPayOrderManager;
    private final GatewayPayOrderManager gatewayPayOrderManager;

    /// 支付终态通知
    public void dispatchPay(PayTrade trade, NoticeEventEnum event) {
        if (trade == null || event == null) {
            return;
        }
        if (isGateway(trade)) {
            GatewayPayOrder order = gatewayPayOrderManager.findById(trade.getContainerId()).orElse(null);
            if (order == null) {
                log.warn("网关订单不存在, 跳过通知: containerId={}", trade.getContainerId());
                return;
            }
            String content = JacksonUtil.toJson(GatewayPayOrderConvert.CONVERT.toResult(order));
            noticeDispatcher.dispatch(new NoticeDispatchCommand()
                    .setMchNo(order.getMchNo())
                    .setAppId(order.getAppId())
                    .setEvent(event.getCode())
                    .setBizId(order.getId())
                    .setBizNo(order.getOrderNo())
                    .setOrderNotifyUrl(order.getNotifyUrl())
                    .setTransport(NoticeTransportEnum.HTTP).setFormat(NoticeFormatEnum.SYSTEM)
                    .setContentMode(NoticeContentModeEnum.SNAPSHOT)
                    .setContentOrRef(content));
            return;
        }
        NormalPayOrder order = normalPayOrderManager.findById(trade.getContainerId()).orElse(null);
        if (order == null) {
            log.warn("普通支付订单不存在, 跳过通知: containerId={}", trade.getContainerId());
            return;
        }
        String content = JacksonUtil.toJson(NormalPayOrderConvert.CONVERT.toResult(order));
        noticeDispatcher.dispatch(new NoticeDispatchCommand()
                .setMchNo(order.getMchNo())
                .setAppId(order.getAppId())
                .setEvent(event.getCode())
                .setBizId(order.getId())
                .setBizNo(order.getOrderNo())
                .setOrderNotifyUrl(order.getNotifyUrl())
                .setTransport(NoticeTransportEnum.HTTP).setFormat(NoticeFormatEnum.SYSTEM)
                .setContentMode(NoticeContentModeEnum.SNAPSHOT)
                .setContentOrRef(content));
    }

    /// 退款终态通知
    public void dispatchRefund(RefundOrder refundOrder, NoticeEventEnum event) {
        if (refundOrder == null || event == null) {
            return;
        }
        String content = JacksonUtil.toJson(RefundOrderConvert.CONVERT.toResult(refundOrder));
        noticeDispatcher.dispatch(new NoticeDispatchCommand()
                .setMchNo(refundOrder.getMchNo())
                .setAppId(refundOrder.getAppId())
                .setEvent(event.getCode())
                .setBizId(refundOrder.getId())
                .setBizNo(refundOrder.getRefundNo())
                .setOrderNotifyUrl(refundOrder.getNotifyUrl())
                .setTransport(NoticeTransportEnum.HTTP).setFormat(NoticeFormatEnum.SYSTEM)
                .setContentMode(NoticeContentModeEnum.SNAPSHOT)
                .setContentOrRef(content));
    }

    /// 判断资金凭证是否为网关支付类型
    private boolean isGateway(PayTrade trade) {
        return Objects.equals(trade.getTradeType(), PayTradeTypeEnum.GATEWAY.getCode());
    }
}
