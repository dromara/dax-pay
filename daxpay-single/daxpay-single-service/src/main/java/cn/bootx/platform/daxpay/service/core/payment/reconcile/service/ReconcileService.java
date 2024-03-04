package cn.bootx.platform.daxpay.service.core.payment.reconcile.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.function.CollectorsFunction;
import cn.bootx.platform.common.core.util.CollUtil;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.ReconcileTradeEnum;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.service.code.AliPayRecordTypeEnum;
import cn.bootx.platform.daxpay.service.code.ReconcileDiffTypeEnum;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.order.reconcile.dao.ReconcileDetailManager;
import cn.bootx.platform.daxpay.service.core.order.reconcile.dao.ReconcileOrderManager;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.ReconcileDetail;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.ReconcileDiffRecord;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.ReconcileOrder;
import cn.bootx.platform.daxpay.service.core.order.reconcile.service.ReconcileDiffService;
import cn.bootx.platform.daxpay.service.core.order.reconcile.service.ReconcileOrderService;
import cn.bootx.platform.daxpay.service.core.payment.reconcile.domain.GeneralReconcileRecord;
import cn.bootx.platform.daxpay.service.core.payment.reconcile.domain.ReconcileDiff;
import cn.bootx.platform.daxpay.service.core.payment.reconcile.factory.ReconcileStrategyFactory;
import cn.bootx.platform.daxpay.service.func.AbsReconcileStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 对账服务
 * @author xxm
 * @since 2024/1/17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReconcileService {
    private final ReconcileOrderService reconcileOrderService;

    private final ReconcileDiffService reconcileDiffService;

    private final ReconcileOrderManager reconcileOrderManager;
    private final ReconcileDetailManager reconcileDetailManager;

    /**
     * 创建对账订单
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public ReconcileOrder create(LocalDate date, String channel) {

        // 获取通道枚举
        PayChannelEnum channelEnum = PayChannelEnum.findByCode(channel);
        // 判断是否为为异步通道
        if (!PayChannelEnum.ASYNC_TYPE.contains(channelEnum)){
            throw new PayFailureException("不支持对账的通道, 请检查");
        }
        // 构建对账策略
        AbsReconcileStrategy reconcileStrategy = ReconcileStrategyFactory.create(channel);

        // 生成批次号
        String seqNo = reconcileStrategy.generateSequence(date);

        ReconcileOrder order = new ReconcileOrder()
                .setBatchNo(seqNo)
                .setChannel(channel)
                .setDate(date);
        reconcileOrderManager.save(order);
        return order;
    }

    /**
     * 下载对账单并进行保存
     */
    public void downAndSave(Long reconcileOrderId) {
        ReconcileOrder reconcileOrder = reconcileOrderService.findById(reconcileOrderId)
                .orElseThrow(() -> new DataNotExistException("未找到对账订单"));
        this.downAndSave(reconcileOrder);
    }

    /**
     * 下载对账单并进行保存
     */
    public void downAndSave(ReconcileOrder reconcileOrder) {
        // 构建对账策略
        AbsReconcileStrategy reconcileStrategy = ReconcileStrategyFactory.create(reconcileOrder.getChannel());
        reconcileStrategy.setRecordOrder(reconcileOrder);
        reconcileStrategy.doBeforeHandler();
        try {
            reconcileStrategy.downAndSave();
            reconcileOrder.setDown(true);
            reconcileOrderService.update(reconcileOrder);
        } catch (Exception e) {
            log.error("下载对账单异常", e);
            reconcileOrder.setErrorMsg("原因: " + e.getMessage());
            reconcileOrderService.update(reconcileOrder);
            throw new RuntimeException(e);
        }
        // 保存转换后的通用结构对账单
        List<ReconcileDetail> reconcileDetails = PaymentContextLocal.get()
                .getReconcileInfo()
                .getReconcileDetails();
        reconcileDetailManager.saveAll(reconcileDetails);
    }

    /**
     * 对账单比对
     */
    public void compare(Long reconcileOrderId){
        ReconcileOrder reconcileOrder = reconcileOrderService.findById(reconcileOrderId)
                .orElseThrow(() -> new DataNotExistException("未找到对账订单"));
        this.compare(reconcileOrder);
    }

    /**
     * 对账单比对
     */
    public void compare(ReconcileOrder reconcileOrder){
        // 判断是否已经下载了对账单明细
        if (!reconcileOrder.isDown()){
            throw new PayFailureException("请先下载对账单");
        }

        // 查询对账单
        List<ReconcileDetail> reconcileDetails = reconcileDetailManager.findAllByOrderId(reconcileOrder.getId());
        // 获取通道枚举
        if (!PayChannelEnum.ASYNC_TYPE_CODE.contains(reconcileOrder.getChannel())){
            log.error("不支持对账的通道, 请检查, 对账订单ID: {}", reconcileOrder.getId());
            throw new PayFailureException("不支持对账的通道, 请检查");
        }
        // 判断是否为为异步通道
        // 构建对账策略
        AbsReconcileStrategy reconcileStrategy = ReconcileStrategyFactory.create(reconcileOrder.getChannel());
        // 初始化参数
        reconcileStrategy.setRecordOrder(reconcileOrder);
        reconcileStrategy.setReconcileDetails(reconcileDetails);
        try {
            // 执行比对任务, 获取对账差异记录并保存
            List<GeneralReconcileRecord> generalReconcileRecord = reconcileStrategy.getGeneralReconcileRecord();
            List<ReconcileDiffRecord> diffRecords = this.generateDiffRecord(reconcileOrder,generalReconcileRecord,reconcileDetails);
            reconcileOrder.setCompare(true);
            reconcileOrderService.update(reconcileOrder);
            reconcileDiffService.saveAll(diffRecords);
        } catch (Exception e) {
            log.error("比对对账单异常", e);
            reconcileOrder.setErrorMsg("原因: " + e.getMessage());
            reconcileOrderService.update(reconcileOrder);
            throw new RuntimeException(e);
        }
    }

    /**
     * 比对生成对账差异单
     * 1. 远程有, 本地无
     * 2. 远程无, 本地有
     * 3. 远程有, 本地有, 但状态不一致
     *
     */
    private List<ReconcileDiffRecord> generateDiffRecord(ReconcileOrder reconcileOrder, List<GeneralReconcileRecord> gatewayRecords, List<ReconcileDetail> localDetails){
        if (CollUtil.isEmpty(localDetails)){
            return new ArrayList<>();
        }
        Map<String, ReconcileDetail> detailMap = localDetails.stream()
                .collect(Collectors.toMap(ReconcileDetail::getOrderId, Function.identity(), CollectorsFunction::retainLatest));

        // 差异内容
        List<ReconcileDiffRecord> diffRecords = new ArrayList<>();
        Map<Long, GeneralReconcileRecord> recordMap = gatewayRecords.stream()
                .collect(Collectors.toMap(GeneralReconcileRecord::getOrderId, Function.identity(), CollectorsFunction::retainLatest));
        // 对账与流水比对
        for (ReconcileDetail detail : localDetails) {
            // 判断本地流水有没有记录, 流水没有记录查询本地订单
            GeneralReconcileRecord record = recordMap.get(Long.valueOf(detail.getOrderId()));
            if (Objects.isNull(record)){
                log.info("本地订单不存在: {}", detail.getOrderId());
                ReconcileDiffRecord diffRecord = new ReconcileDiffRecord()
                        .setDiffType(ReconcileDiffTypeEnum.LOCAL_NOT_EXISTS.getCode())
                        .setOrderId(Long.valueOf(detail.getOrderId()))
                        .setDetailId(detail.getId())
                        .setRecordId(reconcileOrder.getId())
                        .setTitle(detail.getTitle())
                        .setOrderType(detail.getType())
                        .setGatewayOrderNo(detail.getGatewayOrderNo())
                        .setAmount(detail.getAmount())
                        .setOrderTime(detail.getOrderTime());
                diffRecords.add(diffRecord);
                continue;
            }
            // 如果远程和本地都存在, 比对差异
            List<ReconcileDiff> reconcileDiffs = this.reconcileDiff(detail, record);
            if (CollUtil.isNotEmpty(reconcileDiffs)) {
                ReconcileDiffRecord diffRecord = new ReconcileDiffRecord()
                        .setDiffType(ReconcileDiffTypeEnum.NOT_MATCH.getCode())
                        .setOrderId(Long.valueOf(detail.getOrderId()))
                        .setDetailId(detail.getId())
                        .setRecordId(reconcileOrder.getId())
                        .setTitle(detail.getTitle())
                        .setOrderType(detail.getType())
                        .setGatewayOrderNo(detail.getGatewayOrderNo())
                        .setAmount(detail.getAmount())
                        .setDiffs(reconcileDiffs)
                        .setOrderTime(detail.getOrderTime());
                diffRecords.add(diffRecord);
            }
        }
        // 流水与对账单比对, 找出本地有, 远程没有的记录
        for (GeneralReconcileRecord gateway : gatewayRecords) {
            ReconcileDetail detail = detailMap.get(String.valueOf(gateway.getOrderId()));
            if (Objects.isNull(detail)){
                ReconcileDiffRecord diffRecord = new ReconcileDiffRecord()
                        .setDiffType(ReconcileDiffTypeEnum.LOCAL_NOT_EXISTS.getCode())
                        .setOrderId(gateway.getOrderId())
                        .setRecordId(reconcileOrder.getId())
                        .setDetailId(null)
                        .setTitle(gateway.getTitle())
                        .setOrderType(gateway.getType())
                        .setGatewayOrderNo(gateway.getGatewayOrderNo())
                        .setAmount(gateway.getAmount())
                        .setOrderTime(gateway.getGatewayTime());
                diffRecords.add(diffRecord);
                log.info("远程订单不存在: {}", gateway.getOrderId());
                continue;
            }
        }
        return diffRecords;
    }

    /**
     * 判断订单之间存在哪些差异
     */
    public List<ReconcileDiff> reconcileDiff(ReconcileDetail detail, GeneralReconcileRecord record){
        List<ReconcileDiff> diffs = new ArrayList<>();

        // 判断类型是否相同
        if (Objects.equals(detail.getType(), ReconcileTradeEnum.PAY.getCode())
                && !Objects.equals(record.getType(), AliPayRecordTypeEnum.PAY.getCode())){
            log.warn("订单类型不一致: {},{}", detail.getType(), record.getType());
            diffs.add(new ReconcileDiff().setFieldName("订单类型").setLocalValue(detail.getType()).setGatewayValue(record.getType()));
        }
        if (Objects.equals(detail.getType(), ReconcileTradeEnum.REFUND.getCode())
                && !Objects.equals(record.getType(), AliPayRecordTypeEnum.REFUND.getCode())){
            log.warn("订单类型不一致: {},{}", detail.getType(), record.getType());
            diffs.add(new ReconcileDiff().setFieldName("订单类型").setLocalValue(detail.getType()).setGatewayValue(record.getType()));
        }

        // 判断名称是否一致
        if (!Objects.equals(detail.getTitle(), record.getTitle())){
            log.warn("订单名称不一致: {},{}", detail.getTitle(), record.getTitle());
            diffs.add(new ReconcileDiff().setFieldName("订单名称").setLocalValue(detail.getTitle()).setGatewayValue(record.getTitle()));
        }

        // 判断金额是否一致
        if (!Objects.equals(detail.getAmount(), record.getAmount())){
            log.warn("订单金额不一致: {},{}", detail.getAmount(), record.getAmount());
            diffs.add(new ReconcileDiff().setFieldName("订单金额")
                    .setLocalValue(String.valueOf(detail.getAmount()))
                    .setGatewayValue(String.valueOf(record.getAmount())));
        }
        return diffs;
    }
}
