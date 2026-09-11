package cn.daxpay.open.payment.trade.notice.service;

import cn.daxpay.open.payment.common.json.UnipayJson;
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
import cn.daxpay.open.payment.trade.transfer.convert.TransferTradeConvert;
import cn.daxpay.open.payment.trade.transfer.entity.TransferTrade;
import cn.daxpay.open.payment.trade.alloc.convert.AllocOrderConvert;
import cn.daxpay.open.payment.trade.alloc.dao.AllocDetailManager;
import cn.daxpay.open.payment.trade.alloc.entity.AllocOrder;
import cn.daxpay.open.payment.trade.alloc.result.AllocOrderResult;
import cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum;
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
    private final AllocDetailManager allocDetailManager;

    /// unipay 契约报文序列化器: 通知内容快照的时间字段需与契约一致(北京时间),
    /// 而内部订单 DTO 不能加字段注解(会连带影响运营端/商户端接口)
    private final UnipayJson unipayJson;

    /// 支付终态通知
    public void dispatchPay(PayTrade trade, NoticeEventEnum event) {
        if (Objects.isNull(trade) || Objects.isNull(event)) {
            return;
        }
        if (isGateway(trade)) {
            GatewayPayOrder order = gatewayPayOrderManager.findById(trade.getContainerId()).orElse(null);
            if (Objects.isNull(order)) {
                log.warn("网关订单不存在, 跳过通知: containerId={}", trade.getContainerId());
                return;
            }
            String content = unipayJson.toJson(GatewayPayOrderConvert.CONVERT.toResult(order));
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
        if (Objects.isNull(order)) {
            log.warn("普通支付订单不存在, 跳过通知: containerId={}", trade.getContainerId());
            return;
        }
        String content = unipayJson.toJson(NormalPayOrderConvert.CONVERT.toResult(order));
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
        if (Objects.isNull(refundOrder) || Objects.isNull(event)) {
            return;
        }
        String content = unipayJson.toJson(RefundOrderConvert.CONVERT.toResult(refundOrder));
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

    /// 转账终态通知(通知快照取公共资金凭证, 通知地址取容器)
    public void dispatchTransfer(TransferTrade trade, String notifyUrl, NoticeEventEnum event) {
        if (Objects.isNull(trade) || Objects.isNull(event)) {
            return;
        }
        String content = unipayJson.toJson(TransferTradeConvert.CONVERT.toResult(trade));
        noticeDispatcher.dispatch(new NoticeDispatchCommand()
                .setMchNo(trade.getMchNo())
                .setEvent(event.getCode())
                .setBizId(trade.getId())
                .setBizNo(trade.getTradeNo())
                .setOrderNotifyUrl(notifyUrl)
                .setTransport(NoticeTransportEnum.HTTP).setFormat(NoticeFormatEnum.SYSTEM)
                .setContentMode(NoticeContentModeEnum.SNAPSHOT)
                .setContentOrRef(content));
    }

    /// 判断资金凭证是否为网关支付类型
    private boolean isGateway(PayTrade trade) {
        return Objects.equals(trade.getTradeType(), PayTradeTypeEnum.GATEWAY.getCode());
    }

    /// 分账终态通知(通知快照含明细列表, 通知地址取分账单的 notifyUrl)
    public void dispatchAlloc(AllocOrder allocOrder, NoticeEventEnum event) {
        if (Objects.isNull(allocOrder) || Objects.isNull(event)) {
            return;
        }
        AllocOrderResult result = AllocOrderConvert.CONVERT.toResult(allocOrder);
        // 明细列表单独装配(主单 Convert 不自动带明细)
        result.setDetails(AllocOrderConvert.CONVERT.toDetailResults(
                allocDetailManager.findAllByAllocNo(allocOrder.getAllocNo())));
        String content = unipayJson.toJson(result);
        noticeDispatcher.dispatch(new NoticeDispatchCommand()
                .setMchNo(allocOrder.getMchNo())
                .setAppId(allocOrder.getAppId())
                .setEvent(event.getCode())
                .setBizId(allocOrder.getId())
                .setBizNo(allocOrder.getAllocNo())
                .setOrderNotifyUrl(allocOrder.getNotifyUrl())
                .setTransport(NoticeTransportEnum.HTTP).setFormat(NoticeFormatEnum.SYSTEM)
                .setContentMode(NoticeContentModeEnum.SNAPSHOT)
                .setContentOrRef(content));
    }
}
