package cn.daxpay.multi.service.service.reconcile;

import cn.bootx.platform.common.mybatisplus.function.CollectorsFunction;
import cn.bootx.platform.core.util.DateTimeUtil;
import cn.daxpay.multi.core.enums.TradeStatusEnum;
import cn.daxpay.multi.core.enums.TradeTypeEnum;
import cn.daxpay.multi.service.bo.reconcile.PlatformReconcileTradeBo;
import cn.daxpay.multi.service.dao.order.pay.PayOrderManager;
import cn.daxpay.multi.service.dao.order.refund.RefundOrderManager;
import cn.daxpay.multi.service.entity.order.pay.PayOrder;
import cn.daxpay.multi.service.entity.order.refund.RefundOrder;
import cn.daxpay.multi.service.entity.reconcile.ChannelReconcileTrade;
import cn.daxpay.multi.service.entity.reconcile.ReconcileDiscrepancy;
import cn.daxpay.multi.service.entity.reconcile.ReconcileStatement;
import cn.daxpay.multi.service.enums.ReconcileDiscrepancyTypeEnum;
import cn.hutool.core.date.LocalDateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public List<PlatformReconcileTradeBo> getPlatformReconcileTrades(ReconcileStatement statement){
        List<PlatformReconcileTradeBo> reconcileTradeBos = new ArrayList<>();
        // 查询流水
        LocalDateTime localDateTime = DateTimeUtil.date2DateTime(statement.getDate());
        LocalDateTime start = LocalDateTimeUtil.beginOfDay(localDateTime);
        LocalDateTime end = LocalDateTimeUtil.endOfDay(localDateTime);

        // 支付订单
        List<PayOrder> payOrders = payOrderManager.findReconcile(statement.getChannel(), start, end);
        List<RefundOrder> refundOrders = refundOrderManager.findReconcile(statement.getChannel(), start, end);
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
     * 比对生成对账差异单, 通道对账单
     * 1. 远程有, 本地无
     * 2. 远程无, 本地有
     * 3. 远程有, 本地有, 但信息(金额)不一致
     *
     * @param reconcileOrder 对账单
     * @param localTrades 本地交易明细
     * @param channelTrades 通道交易明细
     */
    public List<ReconcileDiscrepancy> generateDiscrepancy(ReconcileStatement reconcileOrder,
                                                          List<PlatformReconcileTradeBo> localTrades,
                                                          List<ChannelReconcileTrade> channelTrades){
        List<ReconcileDiscrepancy> discrepancies = new ArrayList<>();
        // 三方对账订单
        Map<String, ChannelReconcileTrade> outDetailMap = channelTrades.stream()
                .collect(Collectors.toMap(ChannelReconcileTrade::getTradeNo, Function.identity(), CollectorsFunction::retainLatest));
        // 本地订单记录
        Map<String, PlatformReconcileTradeBo> localTradeMap = localTrades.stream()
                .collect(Collectors.toMap(PlatformReconcileTradeBo::getTradeNo, Function.identity(), CollectorsFunction::retainLatest));
        // 对账与比对
        for (var channelDetail : channelTrades) {
            // 判断本地有没有记录, 流水没有记录查询本地订单
            var localTrade = localTradeMap.get(channelDetail.getTradeNo());
            if (Objects.isNull(localTrade)){
                log.info("本地订单不存在: {}", channelDetail.getTradeNo());
                var discrepancy = new ReconcileDiscrepancy()
                        .setReconcileId(reconcileOrder.getId())
                        .setReconcileNo(reconcileOrder.getReconcileNo())
                        .setDiscrepancyType(ReconcileDiscrepancyTypeEnum.LOCAL_NOT_EXISTS.getCode())
                        .setChannel(reconcileOrder.getChannel())
                        .setChannelTradeType(channelDetail.getTradeType())
                        .setChannelTradeNo(channelDetail.getTradeNo())
                        .setChannelTradeStatus(channelDetail.getTradeStatus())
                        .setChannelTradeAmount(channelDetail.getAmount())
                        .setChannelTradeTime(channelDetail.getTradeTime());
                discrepancies.add(discrepancy);
                continue;
            }
            // 如果远程和本地都存在, 比对差异
            if (this.reconcileDiff(channelDetail, localTrade)) {
                var discrepancy = new ReconcileDiscrepancy()
                        .setReconcileId(reconcileOrder.getId())
                        .setReconcileNo(reconcileOrder.getReconcileNo())
                        .setReconcileDate(reconcileOrder.getDate())
                        .setChannel(reconcileOrder.getChannel())
                        .setDiscrepancyType(ReconcileDiscrepancyTypeEnum.NOT_MATCH.getCode())
                        .setTradeNo(localTrade.getTradeNo())
                        .setBizTradeNo(localTrade.getBizTradeNo())
                        .setTradeType(localTrade.getTradeType())
                        .setTradeAmount(localTrade.getAmount())
                        .setChannelTradeStatus(localTrade.getTradeStatus())
                        .setTradeTime(localTrade.getTradeTime())
                        .setChannelTradeNo(channelDetail.getTradeNo())
                        .setChannelTradeType(channelDetail.getTradeType())
                        .setChannelTradeStatus(channelDetail.getTradeStatus())
                        .setChannelTradeAmount(channelDetail.getAmount())
                        .setChannelTradeTime(channelDetail.getTradeTime());
                discrepancies.add(discrepancy);
            }
        }
        // 本地与对账单比对, 找出本地有, 远程没有的记录
        for (var localTrade : localTrades) {
            var channelTrade = outDetailMap.get(localTrade.getTradeNo());
            if (Objects.isNull(channelTrade)){
                var diffRecord = new ReconcileDiscrepancy()
                        .setReconcileId(reconcileOrder.getId())
                        .setReconcileNo(reconcileOrder.getReconcileNo())
                        .setReconcileDate(reconcileOrder.getDate())
                        .setChannel(reconcileOrder.getChannel())
                        .setDiscrepancyType(ReconcileDiscrepancyTypeEnum.REMOTE_NOT_EXISTS.getCode())
                        .setTradeNo(localTrade.getTradeNo())
                        .setBizTradeNo(localTrade.getBizTradeNo())
                        .setTradeType(localTrade.getTradeType())
                        .setTradeStatus(localTrade.getTradeStatus())
                        .setChannelTradeNo(localTrade.getOutTradeNo())
                        .setTradeAmount(localTrade.getAmount())
                        .setTradeTime(localTrade.getTradeTime());
                discrepancies.add(diffRecord);
                log.info("远程订单不存在: {}", localTrade.getTradeNo());
                continue;
            }
        }
        return discrepancies;
    }

    /**
     * 判断订单之间存在哪些差异
     * @param outDetail 下载的对账订单
     * @param localTrade 本地交易订单
     */
    private boolean reconcileDiff(ChannelReconcileTrade outDetail, PlatformReconcileTradeBo localTrade){

        // 判断类型是否相同
        if (!Objects.equals(outDetail.getTradeType(), localTrade.getTradeType())){
            log.warn("订单类型不一致: {},{}", outDetail.getTradeType(), localTrade.getTradeType());
            return false;
        }
        // 判断金额是否一致
        if (!Objects.equals(outDetail.getAmount(), localTrade.getAmount())){
            log.warn("订单金额不一致: {},{}", outDetail.getAmount(), localTrade.getAmount());
            return false;
        }
        return true;
    }
}
