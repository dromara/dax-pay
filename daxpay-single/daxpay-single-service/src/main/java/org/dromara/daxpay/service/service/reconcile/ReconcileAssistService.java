package org.dromara.daxpay.service.service.reconcile;

import cn.bootx.platform.core.util.DateTimeUtil;
import org.dromara.daxpay.core.enums.PayStatusEnum;
import org.dromara.daxpay.core.enums.RefundStatusEnum;
import org.dromara.daxpay.core.enums.TradeStatusEnum;
import org.dromara.daxpay.core.enums.TradeTypeEnum;
import org.dromara.daxpay.service.bo.reconcile.PlatformReconcileTradeBo;
import org.dromara.daxpay.service.dao.order.pay.PayOrderManager;
import org.dromara.daxpay.service.dao.order.refund.RefundOrderManager;
import org.dromara.daxpay.service.entity.order.pay.PayOrder;
import org.dromara.daxpay.service.entity.order.refund.RefundOrder;
import org.dromara.daxpay.service.entity.reconcile.ChannelReconcileTrade;
import org.dromara.daxpay.service.entity.reconcile.ReconcileDiscrepancy;
import org.dromara.daxpay.service.entity.reconcile.ReconcileStatement;
import org.dromara.daxpay.service.enums.ReconcileDiscrepancyTypeEnum;
import cn.hutool.core.date.LocalDateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 对账支撑服务
 * @author xxm
 * @since 2024/5/4
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReconcileAssistService {
    private final PayOrderManager payOrderManager;

    private final RefundOrderManager refundOrderManager;


    /**
     * 获取通用对账对象, 将支付/退款订单转换为对账对象
     */
    public List<PlatformReconcileTradeBo> getPlatformTrades(ReconcileStatement statement){
        List<PlatformReconcileTradeBo> reconcileTradeBos = new ArrayList<>();
        // 查询流水
        LocalDateTime localDateTime = DateTimeUtil.date2DateTime(statement.getDate());
        LocalDateTime start = LocalDateTimeUtil.beginOfDay(localDateTime);
        LocalDateTime end = LocalDateTimeUtil.endOfDay(localDateTime);

        // 支付订单
        List<PayOrder> payOrders = payOrderManager.findReconcile(statement.getChannel(), start, end);
        List<RefundOrder> refundOrders = refundOrderManager.findSuccessReconcile(statement.getChannel(), start, end);
        for (PayOrder payOrder : payOrders) {
            reconcileTradeBos.add(new PlatformReconcileTradeBo()
                    .setTradeNo(payOrder.getOrderNo())
                    .setBizTradeNo(payOrder.getBizOrderNo())
                    .setOutTradeNo(payOrder.getOutOrderNo())
                    .setTradeTime(payOrder.getPayTime())
                    .setTradeType(TradeTypeEnum.PAY.getCode())
                    .setTradeStatus(TradeStatusEnum.SUCCESS.getCode())
                    .setAmount(payOrder.getAmount())
            );
        }
        // 退款订单
        for (RefundOrder refundOrder : refundOrders) {
            reconcileTradeBos.add(new PlatformReconcileTradeBo()
                    .setTradeNo(refundOrder.getRefundNo())
                    .setBizTradeNo(refundOrder.getBizRefundNo())
                    .setOutTradeNo(refundOrder.getOutRefundNo())
                    .setTradeTime(refundOrder.getFinishTime())
                    .setTradeType(TradeTypeEnum.REFUND.getCode())
                    .setTradeStatus(TradeStatusEnum.SUCCESS.getCode())
                    .setAmount(refundOrder.getAmount()));
        }
        return reconcileTradeBos;
    }

    /**
     * 获取通用对账对象, 将支付/退款订单转换为对账对象, 针对本地短单的交易进行二次查询
     */
    public List<PlatformReconcileTradeBo> getPlatformTradesByTradeNo(List<String> tradeNos){
        List<PlatformReconcileTradeBo> reconcileTradeBos = new ArrayList<>();
        // 支付订单
        List<PayOrder> payOrders = payOrderManager.findAllByFields(PayOrder::getOrderNo, tradeNos);
        List<RefundOrder> refundOrders = refundOrderManager.findAllByFields(RefundOrder::getOrderNo, tradeNos);
        for (PayOrder payOrder : payOrders) {
            var tradeBo = new PlatformReconcileTradeBo()
                    .setTradeNo(payOrder.getOrderNo())
                    .setBizTradeNo(payOrder.getBizOrderNo())
                    .setOutTradeNo(payOrder.getOutOrderNo())
                    .setTradeTime(payOrder.getPayTime())
                    .setTradeType(TradeTypeEnum.PAY.getCode())
                    .setAmount(payOrder.getAmount());
            switch (PayStatusEnum.findByCode(payOrder.getStatus())){
                case PROGRESS, TIMEOUT -> tradeBo.setTradeStatus(TradeStatusEnum.PROGRESS.getCode());
                case SUCCESS -> tradeBo.setTradeStatus(TradeStatusEnum.SUCCESS.getCode());
                case CLOSE -> tradeBo.setTradeStatus(TradeStatusEnum.CLOSED.getCode());
                case CANCEL -> tradeBo.setTradeStatus(TradeStatusEnum.REVOKED.getCode());
                case FAIL -> tradeBo.setTradeStatus(TradeStatusEnum.FAIL.getCode());
            }
            reconcileTradeBos.add(tradeBo);
        }
        // 退款订单
        for (RefundOrder refundOrder : refundOrders) {
            var tradeBo = new PlatformReconcileTradeBo()
                    .setTradeNo(refundOrder.getRefundNo())
                    .setBizTradeNo(refundOrder.getBizRefundNo())
                    .setOutTradeNo(refundOrder.getOutRefundNo())
                    .setTradeTime(refundOrder.getFinishTime())
                    .setTradeType(TradeTypeEnum.REFUND.getCode())
                    .setAmount(refundOrder.getAmount());
            switch (RefundStatusEnum.findByCode(refundOrder.getStatus())){
                case PROGRESS -> tradeBo.setTradeStatus(TradeStatusEnum.PROGRESS.getCode());
                case SUCCESS -> tradeBo.setTradeStatus(TradeStatusEnum.SUCCESS.getCode());
                case CLOSE -> tradeBo.setTradeStatus(TradeStatusEnum.CLOSED.getCode());
                case FAIL -> tradeBo.setTradeStatus(TradeStatusEnum.FAIL.getCode());
            }
            reconcileTradeBos.add(tradeBo);
        }
        return reconcileTradeBos;
    }

    /**
     * 构建本地短单差异记录
     */
    public ReconcileDiscrepancy buildDiscrepancy(ReconcileStatement statement, ChannelReconcileTrade channelDetail) {
        return new ReconcileDiscrepancy()
                .setReconcileId(statement.getId())
                .setReconcileNo(statement.getReconcileNo())
                .setReconcileDate(statement.getDate())
                .setDiscrepancyType(ReconcileDiscrepancyTypeEnum.LOCAL_NOT_EXISTS.getCode())
                .setChannel(statement.getChannel())
                .setChannelTradeType(channelDetail.getTradeType())
                .setChannelTradeNo(channelDetail.getTradeNo())
                .setChannelOutTradeNo(channelDetail.getOutTradeNo())
                .setChannelTradeStatus(channelDetail.getTradeStatus())
                .setChannelTradeAmount(channelDetail.getAmount())
                .setChannelTradeTime(channelDetail.getTradeTime());
    }

    /**
     * 构建远程短单差异记录
     */
    public ReconcileDiscrepancy buildDiscrepancy(ReconcileStatement statement, PlatformReconcileTradeBo localTrade) {
        return new ReconcileDiscrepancy()
                .setReconcileId(statement.getId())
                .setReconcileNo(statement.getReconcileNo())
                .setReconcileDate(statement.getDate())
                .setChannel(statement.getChannel())
                .setDiscrepancyType(ReconcileDiscrepancyTypeEnum.REMOTE_NOT_EXISTS.getCode())
                .setTradeNo(localTrade.getTradeNo())
                .setBizTradeNo(localTrade.getBizTradeNo())
                .setOutTradeNo(localTrade.getOutTradeNo())
                .setTradeType(localTrade.getTradeType())
                .setTradeStatus(localTrade.getTradeStatus())
                .setTradeAmount(localTrade.getAmount())
                .setTradeTime(localTrade.getTradeTime());
    }

    /**
     * 构建差异记录
     */
    public ReconcileDiscrepancy buildDiscrepancy(ReconcileStatement statement, PlatformReconcileTradeBo localTrade, ChannelReconcileTrade channelDetail){
       return new ReconcileDiscrepancy()
                .setReconcileId(statement.getId())
                .setReconcileNo(statement.getReconcileNo())
                .setReconcileDate(statement.getDate())
                .setChannel(statement.getChannel())
                .setDiscrepancyType(ReconcileDiscrepancyTypeEnum.NOT_MATCH.getCode())
                .setTradeNo(localTrade.getTradeNo())
                .setBizTradeNo(localTrade.getBizTradeNo())
                .setOutTradeNo(localTrade.getOutTradeNo())
                .setTradeType(localTrade.getTradeType())
                .setTradeAmount(localTrade.getAmount())
                .setChannelTradeStatus(localTrade.getTradeStatus())
                .setTradeTime(localTrade.getTradeTime())
                .setChannelTradeNo(channelDetail.getTradeNo())
                .setChannelOutTradeNo(channelDetail.getOutTradeNo())
                .setChannelTradeType(channelDetail.getTradeType())
                .setChannelTradeStatus(channelDetail.getTradeStatus())
                .setChannelTradeAmount(channelDetail.getAmount())
                .setChannelTradeTime(channelDetail.getTradeTime());
    }

}
