package cn.bootx.platform.daxpay.service.core.payment.reconcile.service;

import cn.bootx.platform.common.core.function.CollectorsFunction;
import cn.bootx.platform.common.core.util.CollUtil;
import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.code.ReconcileTradeEnum;
import cn.bootx.platform.daxpay.code.RefundStatusEnum;
import cn.bootx.platform.daxpay.service.code.PaymentTypeEnum;
import cn.bootx.platform.daxpay.service.code.ReconcileDiffTypeEnum;
import cn.bootx.platform.daxpay.service.core.order.pay.dao.PayOrderManager;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.ReconcileTradeDetail;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.ReconcileDiff;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.ReconcileOrder;
import cn.bootx.platform.daxpay.service.core.order.refund.dao.RefundOrderManager;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.RefundOrder;
import cn.bootx.platform.daxpay.service.core.payment.reconcile.domain.GeneralTradeInfo;
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
        List<PayOrder> payOrders = payOrderManager.findReconcile(reconcileOrder.getChannel(), start, end, PayStatusEnum.SUCCESS, PayStatusEnum.PARTIAL_REFUND, PayStatusEnum.REFUNDING, PayStatusEnum.REFUNDED);
        List<RefundOrder> refundOrders = refundOrderManager.findReconcile(reconcileOrder.getChannel(), start, end, RefundStatusEnum.SUCCESS);
        for (PayOrder payOrder : payOrders) {
            generalTradeInfoList.add(new GeneralTradeInfo()
                    .setTitle(payOrder.getTitle())
                    .setTradeNo(payOrder.getOrderNo())
                    .setOutTradeNo(payOrder.getOutOrderNo())
                    .setFinishTime(payOrder.getPayTime())
                    .setType(PaymentTypeEnum.PAY.getCode())
                    .setAmount(payOrder.getAmount())
            );
        }
        for (RefundOrder refundOrder : refundOrders) {
            generalTradeInfoList.add(new GeneralTradeInfo()
                    .setTitle(refundOrder.getTitle())
                    .setTradeNo(refundOrder.getOrderNo())
                    .setOutTradeNo(refundOrder.getOutOrderNo())
                    .setFinishTime(refundOrder.getFinishTime())
                    .setType(PaymentTypeEnum.REFUND.getCode())
                    .setAmount(refundOrder.getAmount()));
        }
        return generalTradeInfoList;
    }

    /**
     * 比对生成对账差异单
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
                                                  List<ReconcileTradeDetail> outDetails){
        if (CollUtil.isEmpty(outDetails) || CollUtil.isEmpty(localTrades)){
            return new ArrayList<>();
        }
        // 差异内容
        List<ReconcileDiff> diffRecords = new ArrayList<>();

        // 三方对账订单
        Map<String, ReconcileTradeDetail> outDetailMap = outDetails.stream()
                .collect(Collectors.toMap(ReconcileTradeDetail::getTradeNo, Function.identity(), CollectorsFunction::retainLatest));
        // 本地订单记录
        Map<String, GeneralTradeInfo> localTradeMap = localTrades.stream()
                .collect(Collectors.toMap(GeneralTradeInfo::getTradeNo, Function.identity(), CollectorsFunction::retainLatest));
        // 对账与比对
        for (ReconcileTradeDetail outDetail : outDetails) {
            // 判断本地有没有记录, 流水没有记录查询本地订单
            GeneralTradeInfo localTrade = localTradeMap.get(outDetail.getTradeNo());
            if (Objects.isNull(localTrade)){
                log.info("本地订单不存在: {}", outDetail.getTradeNo());
                ReconcileDiff diffRecord = new ReconcileDiff()
                        .setDiffType(ReconcileDiffTypeEnum.LOCAL_NOT_EXISTS.getCode())
                        .setTradeNo(outDetail.getTradeNo())
                        .setDetailId(outDetail.getId())
                        .setReconcileId(reconcileOrder.getId())
                        .setTitle(outDetail.getTitle())
                        .setOrderType(outDetail.getType())
                        .setOutOrderNo(outDetail.getOutTradeNo())
                        .setOutAmount(outDetail.getAmount())
                        .setTradeTime(outDetail.getTradeTime());
                diffRecords.add(diffRecord);
                continue;
            }
            // 如果远程和本地都存在, 比对差异
            List<cn.bootx.platform.daxpay.service.core.payment.reconcile.domain.ReconcileDiff> reconcileDiffs = this.reconcileDiff(outDetail, localTrade);
            if (CollUtil.isNotEmpty(reconcileDiffs)) {
                ReconcileDiff diffRecord = new ReconcileDiff()
                        .setReconcileId(reconcileOrder.getId())
                        .setDetailId(outDetail.getId())
                        .setDiffType(ReconcileDiffTypeEnum.NOT_MATCH.getCode())
                        .setTradeNo(outDetail.getTradeNo())
                        .setTitle(outDetail.getTitle())
                        .setOrderType(outDetail.getType())
                        .setOutOrderNo(outDetail.getOutTradeNo())
                        .setOutAmount(outDetail.getAmount())
                        .setAmount(localTrade.getAmount())
                        .setDiffs(reconcileDiffs)
                        .setTradeTime(outDetail.getTradeTime());
                diffRecords.add(diffRecord);
            }
        }
        // 本地与对账单比对, 找出本地有, 远程没有的记录
        for (GeneralTradeInfo localTrade : localTrades) {
            ReconcileTradeDetail outDetail = outDetailMap.get(localTrade.getTradeNo());
            if (Objects.isNull(outDetail)){
                ReconcileDiff diffRecord = new ReconcileDiff()
                        .setDiffType(ReconcileDiffTypeEnum.LOCAL_NOT_EXISTS.getCode())
                        .setTradeNo(localTrade.getTradeNo())
                        .setReconcileId(reconcileOrder.getId())
                        .setDetailId(null)
                        .setTitle(localTrade.getTitle())
                        .setOrderType(localTrade.getType())
                        .setOutOrderNo(localTrade.getOutTradeNo())
                        .setAmount(localTrade.getAmount())
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
    private List<cn.bootx.platform.daxpay.service.core.payment.reconcile.domain.ReconcileDiff> reconcileDiff(ReconcileTradeDetail outDetail, GeneralTradeInfo localTrade){
        List<cn.bootx.platform.daxpay.service.core.payment.reconcile.domain.ReconcileDiff> diffs = new ArrayList<>();

        // 判断类型是否相同
        if (Objects.equals(outDetail.getType(), ReconcileTradeEnum.PAY.getCode())
                && !Objects.equals(localTrade.getType(), PaymentTypeEnum.PAY.getCode())){
            log.warn("订单类型不一致: {},{}", outDetail.getType(), localTrade.getType());
            diffs.add(new cn.bootx.platform.daxpay.service.core.payment.reconcile.domain.ReconcileDiff().setFieldName("订单类型").setLocalValue(outDetail.getType()).setOutValue(localTrade.getType()));
        }
        if (Objects.equals(outDetail.getType(), ReconcileTradeEnum.REFUND.getCode())
                && !Objects.equals(localTrade.getType(), PaymentTypeEnum.REFUND.getCode())){
            log.warn("订单类型不一致: {},{}", outDetail.getType(), localTrade.getType());
            diffs.add(new cn.bootx.platform.daxpay.service.core.payment.reconcile.domain.ReconcileDiff().setFieldName("订单类型").setLocalValue(outDetail.getType()).setOutValue(localTrade.getType()));
        }

        // 判断名称是否一致
        if (!Objects.equals(outDetail.getTitle(), localTrade.getTitle())){
            log.warn("订单名称不一致: {},{}", outDetail.getTitle(), localTrade.getTitle());
            diffs.add(new cn.bootx.platform.daxpay.service.core.payment.reconcile.domain.ReconcileDiff().setFieldName("订单名称").setLocalValue(outDetail.getTitle()).setOutValue(localTrade.getTitle()));
        }

        // 判断金额是否一致
        if (!Objects.equals(outDetail.getAmount(), localTrade.getAmount())){
            log.warn("订单金额不一致: {},{}", outDetail.getAmount(), localTrade.getAmount());
            diffs.add(new cn.bootx.platform.daxpay.service.core.payment.reconcile.domain.ReconcileDiff().setFieldName("订单金额")
                    .setLocalValue(String.valueOf(outDetail.getAmount()))
                    .setOutValue(String.valueOf(localTrade.getAmount())));
        }
        return diffs;
    }

}
