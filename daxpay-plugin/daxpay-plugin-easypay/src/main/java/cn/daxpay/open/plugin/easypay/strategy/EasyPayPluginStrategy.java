package cn.daxpay.open.plugin.easypay.strategy;

import cn.daxpay.open.payment.strategy.plugin.AbsPayPluginStrategy;
import cn.daxpay.open.payment.trade.notice.command.NoticeDispatchCommand;
import cn.daxpay.open.payment.trade.notice.service.NoticeDispatcher;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeContentModeEnum;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeEventEnum;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeProtocolEnum;
import cn.daxpay.open.platform.core.enums.pay.trade.TradeSourceEnum;
import cn.daxpay.open.plugin.easypay.entity.EasyPayOrder;
import cn.daxpay.open.plugin.easypay.service.order.EasyPayOrderService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

/// # 易支付插件生命周期策略
///
/// 仅处理来源为易支付协议（TradeSourceEnum.EASY_PAY）的交易钩子
@Slf4j
@Service
@RequiredArgsConstructor
public class EasyPayPluginStrategy implements AbsPayPluginStrategy {

    private final EasyPayOrderService easyPayOrderService;
    private final NoticeDispatcher noticeDispatcher;

    /// 支付成功：回写协议单状态并注册易支付出站通知
    @Override
    public void paySuccess(PayTrade trade) {
        if (!Objects.equals(TradeSourceEnum.EASY_PAY.getCode(), trade.getSource())) {
            return;
        }
        EasyPayOrder easyPayOrder = easyPayOrderService.paySuccess(trade);
        registerEasyPayNotice(easyPayOrder);
    }

    /// 关单：一期仅日志
    @Override
    public void payClose(PayTrade trade) {
        if (!Objects.equals(TradeSourceEnum.EASY_PAY.getCode(), trade.getSource())) {
            return;
        }
        easyPayOrderService.payClose(trade);
    }

    /// 退款成功：累加协议单已退金额
    @Override
    public void refundSuccess(PayTrade trade, RefundOrder refundOrder) {
        if (!Objects.equals(TradeSourceEnum.EASY_PAY.getCode(), trade.getSource())) {
            return;
        }
        long amount = refundOrder.getAmount() == null ? 0L : refundOrder.getAmount();
        easyPayOrderService.refundSuccess(trade, amount);
    }

    /// 注册易支付协议出站（content 仅存 id 指针）
    private void registerEasyPayNotice(EasyPayOrder easyPayOrder) {
        if (easyPayOrder == null || StrUtil.isBlank(easyPayOrder.getNotifyUrl())) {
            log.info("易支付订单无需回调, outTradeNo={}",
                    easyPayOrder == null ? null : easyPayOrder.getOutTradeNo());
            return;
        }
        String content = JacksonUtil.toJson(Map.of(
                "id", easyPayOrder.getId(),
                "pid", easyPayOrder.getPid() == null ? 0 : easyPayOrder.getPid(),
                "remark", "ref-only; payload assembled at send time"
        ));
        noticeDispatcher.dispatch(new NoticeDispatchCommand()
                .setMchNo(easyPayOrder.getMchNo())
                .setAppId(easyPayOrder.getAppId())
                .setEvent(NoticeEventEnum.PAY_SUCCESS.getCode())
                .setBizId(easyPayOrder.getId())
                .setBizNo(easyPayOrder.getTradeNo() != null ? easyPayOrder.getTradeNo() : easyPayOrder.getOutTradeNo())
                .setProtocol(NoticeProtocolEnum.EASY_PAY)
                .setContentMode(NoticeContentModeEnum.REF)
                .setContentOrRef(content)
                .setProtocolNotifyUrl(easyPayOrder.getNotifyUrl()));
    }
}
