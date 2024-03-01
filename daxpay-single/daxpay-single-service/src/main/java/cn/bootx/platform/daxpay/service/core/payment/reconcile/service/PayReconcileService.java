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
import cn.bootx.platform.daxpay.service.core.order.reconcile.dao.PayReconcileDetailManager;
import cn.bootx.platform.daxpay.service.core.order.reconcile.dao.PayReconcileOrderManager;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.PayReconcileDetail;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.PayReconcileDiffRecord;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.PayReconcileOrder;
import cn.bootx.platform.daxpay.service.core.order.reconcile.service.PayReconcileDiffRecordService;
import cn.bootx.platform.daxpay.service.core.order.reconcile.service.PayReconcileOrderService;
import cn.bootx.platform.daxpay.service.core.payment.reconcile.domain.GeneralReconcileRecord;
import cn.bootx.platform.daxpay.service.core.payment.reconcile.factory.PayReconcileStrategyFactory;
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
 * 支付对账单下载服务
 * @author xxm
 * @since 2024/1/17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayReconcileService {
    private final PayReconcileOrderService reconcileOrderService;

    private final PayReconcileDiffRecordService reconcileDiffRecordService;

    private final PayReconcileOrderManager reconcileOrderManager;
    private final PayReconcileDetailManager reconcileDetailManager;

    /**
     * 创建对账订单
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public PayReconcileOrder create(LocalDate date, String channel) {

        // 获取通道枚举
        PayChannelEnum channelEnum = PayChannelEnum.findByCode(channel);
        // 判断是否为为异步通道
        if (!PayChannelEnum.ASYNC_TYPE.contains(channelEnum)){
            throw new PayFailureException("不支持对账的通道, 请检查");
        }
        // 构建对账策略
        AbsReconcileStrategy reconcileStrategy = PayReconcileStrategyFactory.create(channel);

        // 生成批次号
        String seqNo = reconcileStrategy.generateSequence(date);

        PayReconcileOrder order = new PayReconcileOrder()
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
        PayReconcileOrder payReconcileOrder = reconcileOrderService.findById(reconcileOrderId)
                .orElseThrow(() -> new DataNotExistException("未找到对账订单"));
        this.downAndSave(payReconcileOrder);
    }

    /**
     * 下载对账单并进行保存
     */
    public void downAndSave(PayReconcileOrder reconcileOrder) {
        // 构建对账策略
        AbsReconcileStrategy reconcileStrategy = PayReconcileStrategyFactory.create(reconcileOrder.getChannel());
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
        List<PayReconcileDetail> reconcileDetails = PaymentContextLocal.get()
                .getReconcileInfo()
                .getReconcileDetails();
        reconcileDetailManager.saveAll(reconcileDetails);
    }

    /**
     * 对账单比对
     */
    public void compare(Long reconcileOrderId){
        PayReconcileOrder payReconcileOrder = reconcileOrderService.findById(reconcileOrderId)
                .orElseThrow(() -> new DataNotExistException("未找到对账订单"));
        this.compare(payReconcileOrder);
    }

    /**
     * 对账单比对
     */
    public void compare(PayReconcileOrder reconcileOrder){
        // 判断是否已经下载了对账单明细
        if (!reconcileOrder.isDown()){
            throw new PayFailureException("请先下载对账单");
        }

        // 查询对账单
        List<PayReconcileDetail> reconcileDetails = reconcileDetailManager.findAllByOrderId(reconcileOrder.getId());
        // 获取通道枚举
        if (!PayChannelEnum.ASYNC_TYPE_CODE.contains(reconcileOrder.getChannel())){
            log.error("不支持对账的通道, 请检查, 对账订单ID: {}", reconcileOrder.getId());
            throw new PayFailureException("不支持对账的通道, 请检查");
        }
        // 判断是否为为异步通道
        // 构建对账策略
        AbsReconcileStrategy reconcileStrategy = PayReconcileStrategyFactory.create(reconcileOrder.getChannel());
        // 初始化参数
        reconcileStrategy.setRecordOrder(reconcileOrder);
        reconcileStrategy.setReconcileDetails(reconcileDetails);
        try {
            // 执行比对任务, 获取对账差异记录
            List<GeneralReconcileRecord> generalReconcileRecord = reconcileStrategy.getGeneralReconcileRecord();
            List<PayReconcileDiffRecord> diffRecords = this.generateDiffRecord(generalReconcileRecord,reconcileDetails);
//            reconcileOrder.setCompare(true);
//            reconcileOrderService.update(reconcileOrder);
        } catch (Exception e) {
            log.error("比对对账单异常", e);
            reconcileOrder.setErrorMsg("原因: " + e.getMessage());
            reconcileOrderService.update(reconcileOrder);
            throw new RuntimeException(e);
        }
    }

    /**
     * 比对生成对账差异单
     * 1. 远程有, 本地无  补单(追加回订单/记录差异表)
     * 2. 远程无, 本地有  记录差错表
     * 3. 远程有, 本地有, 但状态不一致 记录差错表
     *
     */
    private List<PayReconcileDiffRecord> generateDiffRecord(List<GeneralReconcileRecord> records, List<PayReconcileDetail> details){
        if (CollUtil.isEmpty(details)){
            return new ArrayList<>();
        }
        Map<String, PayReconcileDetail> detailMap = details.stream()
                .collect(Collectors.toMap(PayReconcileDetail::getOrderId, Function.identity(), CollectorsFunction::retainLatest));

        List<PayReconcileDiffRecord> diffRecords = new ArrayList<>();

        Map<Long, GeneralReconcileRecord> recordMap = records.stream()
                .collect(Collectors.toMap(GeneralReconcileRecord::getOrderId, Function.identity(), CollectorsFunction::retainLatest));

        // 对账与流水比对
        for (PayReconcileDetail detail : details) {
            // 判断本地流水有没有记录, 流水没有记录查询本地订单
            GeneralReconcileRecord record = recordMap.get(Long.valueOf(detail.getOrderId()));
            if (Objects.isNull(record)){
                log.info("本地订单不存在: {}", detail.getOrderId());
                PayReconcileDiffRecord diffRecord = new PayReconcileDiffRecord()
                        .setDiffType(ReconcileDiffTypeEnum.LOCAL_NOT_EXISTS.getCode())
                        .setOrderId(Long.valueOf(detail.getOrderId()))
                        .setDetailId(detail.getId())
                        .setRecordId(detail.getRecordOrderId())
                        .setTitle(detail.getTitle())
                        .setDiffType(detail.getType())
                        .setGatewayOrderNo(detail.getGatewayOrderNo())
                        .setAmount(detail.getAmount())
                        .setOrderTime(detail.getOrderTime());
                diffRecords.add(diffRecord);
                continue;
            }
            // 交易类型 支付/退款
            if (Objects.equals(detail.getType(), ReconcileTradeEnum.PAY.getCode())){
                // 判断类型是否存在差异
                if (!Objects.equals(record.getType(), AliPayRecordTypeEnum.PAY.getCode())){
                    PayReconcileDiffRecord diffRecord = new PayReconcileDiffRecord()
                            .setDiffType(ReconcileDiffTypeEnum.NOT_MATCH.getCode())
                            .setOrderId(Long.valueOf(detail.getOrderId()))
                            .setDetailId(detail.getId())
                            .setRecordId(detail.getRecordOrderId())
                            .setTitle(detail.getTitle())
                            .setDiffType(detail.getType())
                            .setGatewayOrderNo(detail.getGatewayOrderNo())
                            .setAmount(detail.getAmount())
                            .setOrderTime(detail.getOrderTime());
                    diffRecords.add(diffRecord);
                    continue;
                }
            } else {
                // 判断类型是否存在差异
                if (!Objects.equals(record.getType(), AliPayRecordTypeEnum.REFUND.getCode())) {
                    log.info("本地订单类型不正常: {}", detail.getOrderId());
                    continue;
                }
            }
            // 判断是否存在差异 金额, 状态
            if (!Objects.equals(record.getAmount(), detail.getAmount())){
                log.info("本地订单金额不正常: {}", detail.getOrderId());
                continue;
            }
        }
        // 流水与对账单比对, 找出本地有, 远程没有的记录
        for (GeneralReconcileRecord record : records) {
            PayReconcileDetail detail = detailMap.get(String.valueOf(record.getOrderId()));
            if (Objects.isNull(detail)){
                log.info("远程订单不存在: {}", record.getOrderId());
                continue;
            }
        }
        return diffRecords;
    }

    /**
     * 判断订单之间是否存在差异
     */
    public void reconcileDiff(GeneralReconcileRecord record, PayReconcileDetail detail){

    }
}
