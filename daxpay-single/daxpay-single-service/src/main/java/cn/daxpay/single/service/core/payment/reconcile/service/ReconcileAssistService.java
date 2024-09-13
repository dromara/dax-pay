package cn.daxpay.single.service.core.payment.reconcile.service;

import cn.bootx.platform.common.core.function.CollectorsFunction;
import cn.bootx.platform.common.core.util.CollUtil;
import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.daxpay.single.core.code.ReconcileTradeEnum;
import cn.daxpay.single.service.code.TradeTypeEnum;
import cn.daxpay.single.service.code.ReconcileDiffTypeEnum;
import cn.daxpay.single.service.core.order.pay.dao.PayOrderManager;
import cn.daxpay.single.service.core.order.pay.entity.PayOrder;
import cn.daxpay.single.service.core.order.reconcile.entity.ReconcileOutTrade;
import cn.daxpay.single.service.core.order.reconcile.entity.ReconcileDiff;
import cn.daxpay.single.service.core.order.reconcile.entity.ReconcileOrder;
import cn.daxpay.single.service.core.order.refund.dao.RefundOrderManager;
import cn.daxpay.single.service.core.order.refund.entity.RefundOrder;
import cn.daxpay.single.service.core.payment.reconcile.domain.GeneralTradeInfo;
import cn.daxpay.single.service.core.payment.reconcile.domain.ReconcileDiffDetail;
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
    public List<GeneralTradeInfo> getGeneralTradeInfoList(ReconcileOrder reconcileOrder){
        List<GeneralTradeInfo> generalTradeInfoList = new ArrayList<>();
        // 查询流水
        LocalDateTime localDateTime = LocalDateTimeUtil.date2DateTime(reconcileOrder.getDate());
        LocalDateTime start = LocalDateTimeUtil.beginOfDay(localDateTime);
        LocalDateTime end = LocalDateTimeUtil.endOfDay(localDateTime);

        // 下载支付订单
        List<PayOrder> payOrders = payOrderManager.findReconcile(reconcileOrder.getChannel(), start, end);
        List<RefundOrder> refundOrders = refundOrderManager.findReconcile(reconcileOrder.getChannel(), start, end);
        for (PayOrder payOrder : payOrders) {
            generalTradeInfoList.add(new GeneralTradeInfo()
                    .setTitle(payOrder.getTitle())
                    .setTradeNo(payOrder.getOrderNo())
                    .setOutTradeNo(payOrder.getOutOrderNo())
                    .setFinishTime(payOrder.getPayTime())
                    .setType(TradeTypeEnum.PAY.getCode())
                    .setAmount(payOrder.getAmount())
            );
        }
        for (RefundOrder refundOrder : refundOrders) {
            generalTradeInfoList.add(new GeneralTradeInfo()
                    .setTitle(refundOrder.getTitle())
                    .setTradeNo(refundOrder.getRefundNo())
                    .setOutTradeNo(refundOrder.getOutRefundNo())
                    .setFinishTime(refundOrder.getFinishTime())
                    .setType(TradeTypeEnum.REFUND.getCode())
                    .setAmount(refundOrder.getAmount()));
        }
        return generalTradeInfoList;
    }

    /**
     * 比对生成对账差异单, 通道对账单
     * 1. 远程有, 本地无
     * 2. 远程无, 本地有
     * 3. 远程有, 本地有, 但状态不一致
     *
     * @param reconcileOrder 对账单
     * @param localTrades 本地交易订单
     * @param outDetails 远程对账明细
     */
    public List<ReconcileDiff> generateDiffRecord(ReconcileOrder reconcileOrder,
                                                  List<GeneralTradeInfo> localTrades,
                                                  List<ReconcileOutTrade> outDetails){
        // 差异内容
        List<ReconcileDiff> diffRecords = new ArrayList<>();

        // 三方对账订单
        Map<String, ReconcileOutTrade> outDetailMap = outDetails.stream()
                .collect(Collectors.toMap(ReconcileOutTrade::getTradeNo, Function.identity(), CollectorsFunction::retainLatest));
        // 本地订单记录
        Map<String, GeneralTradeInfo> localTradeMap = localTrades.stream()
                .collect(Collectors.toMap(GeneralTradeInfo::getTradeNo, Function.identity(), CollectorsFunction::retainLatest));
        // 对账与比对
        for (ReconcileOutTrade outDetail : outDetails) {
            // 判断本地有没有记录, 流水没有记录查询本地订单
            GeneralTradeInfo localTrade = localTradeMap.get(outDetail.getTradeNo());
            if (Objects.isNull(localTrade)){
                log.info("本地订单不存在: {}", outDetail.getTradeNo());
                ReconcileDiff diffRecord = new ReconcileDiff()
                        .setDiffType(ReconcileDiffTypeEnum.LOCAL_NOT_EXISTS.getCode())
                        .setTradeNo(outDetail.getTradeNo())
                        .setDetailId(outDetail.getId())
                        .setReconcileId(reconcileOrder.getId())
                        .setReconcileNo(reconcileOrder.getReconcileNo())
                        .setReconcileDate(reconcileOrder.getDate())
                        .setTitle(outDetail.getTitle())
                        .setTradeType(outDetail.getType())
                        .setOutTradeNo(outDetail.getOutTradeNo())
                        .setOutAmount(outDetail.getAmount())
                        .setChannel(reconcileOrder.getChannel())
                        .setTradeTime(outDetail.getTradeTime());
                diffRecords.add(diffRecord);
                continue;
            }
            // 如果远程和本地都存在, 比对差异
            List<ReconcileDiffDetail> reconcileDiffDetails = this.reconcileDiff(outDetail, localTrade);
            if (CollUtil.isNotEmpty(reconcileDiffDetails)) {
                ReconcileDiff diffRecord = new ReconcileDiff()
                        .setReconcileId(reconcileOrder.getId())
                        .setReconcileNo(reconcileOrder.getReconcileNo())
                        .setReconcileDate(reconcileOrder.getDate())
                        .setDetailId(outDetail.getId())
                        .setDiffType(ReconcileDiffTypeEnum.NOT_MATCH.getCode())
                        .setTradeNo(outDetail.getTradeNo())
                        .setTitle(outDetail.getTitle())
                        .setTradeType(outDetail.getType())
                        .setOutTradeNo(outDetail.getOutTradeNo())
                        .setOutAmount(outDetail.getAmount())
                        .setAmount(localTrade.getAmount())
                        .setChannel(reconcileOrder.getChannel())
                        .setDiffs(reconcileDiffDetails)
                        .setTradeTime(outDetail.getTradeTime());
                diffRecords.add(diffRecord);
            }
        }
        // 本地与对账单比对, 找出本地有, 远程没有的记录
        for (GeneralTradeInfo localTrade : localTrades) {
            ReconcileOutTrade outDetail = outDetailMap.get(localTrade.getTradeNo());
            if (Objects.isNull(outDetail)){
                ReconcileDiff diffRecord = new ReconcileDiff()
                        .setDiffType(ReconcileDiffTypeEnum.REMOTE_NOT_EXISTS.getCode())
                        .setTradeNo(localTrade.getTradeNo())
                        .setReconcileId(reconcileOrder.getId())
                        .setReconcileNo(reconcileOrder.getReconcileNo())
                        .setReconcileDate(reconcileOrder.getDate())
                        .setTitle(localTrade.getTitle())
                        .setTradeType(localTrade.getType())
                        .setOutTradeNo(localTrade.getOutTradeNo())
                        .setAmount(localTrade.getAmount())
                        .setChannel(reconcileOrder.getChannel())
                        .setTradeTime(localTrade.getFinishTime());
                diffRecords.add(diffRecord);
                log.info("远程订单不存在: {}", localTrade.getTradeNo());
                continue;
            }
        }
        return diffRecords;
    }

    /**
     * 判断订单之间存在哪些差异
     * @param outDetail 下载的对账订单
     * @param localTrade 本地交易订单
     */
    private List<ReconcileDiffDetail> reconcileDiff(ReconcileOutTrade outDetail, GeneralTradeInfo localTrade){
        List<ReconcileDiffDetail> diffs = new ArrayList<>();

        // 判断类型是否相同
        if (Objects.equals(outDetail.getType(), ReconcileTradeEnum.PAY.getCode())
                && !Objects.equals(localTrade.getType(), TradeTypeEnum.PAY.getCode())){
            log.warn("订单类型不一致: {},{}", outDetail.getType(), localTrade.getType());
            diffs.add(new ReconcileDiffDetail().setFieldName("订单类型").setLocalValue(outDetail.getType()).setOutValue(localTrade.getType()));
        }
        if (Objects.equals(outDetail.getType(), ReconcileTradeEnum.REFUND.getCode())
                && !Objects.equals(localTrade.getType(), TradeTypeEnum.REFUND.getCode())){
            log.warn("订单类型不一致: {},{}", outDetail.getType(), localTrade.getType());
            diffs.add(new ReconcileDiffDetail().setFieldName("订单类型").setLocalValue(outDetail.getType()).setOutValue(localTrade.getType()));
        }

        // 判断名称是否一致
        if (!Objects.equals(outDetail.getTitle(), localTrade.getTitle())){
            log.warn("订单名称不一致: {},{}", outDetail.getTitle(), localTrade.getTitle());
            diffs.add(new ReconcileDiffDetail().setFieldName("订单名称").setLocalValue(outDetail.getTitle()).setOutValue(localTrade.getTitle()));
        }

        // 判断金额是否一致
        if (!Objects.equals(outDetail.getAmount(), localTrade.getAmount())){
            log.warn("订单金额不一致: {},{}", outDetail.getAmount(), localTrade.getAmount());
            diffs.add(new ReconcileDiffDetail().setFieldName("订单金额")
                    .setLocalValue(String.valueOf(outDetail.getAmount()))
                    .setOutValue(String.valueOf(localTrade.getAmount())));
        }
        return diffs;
    }
}
