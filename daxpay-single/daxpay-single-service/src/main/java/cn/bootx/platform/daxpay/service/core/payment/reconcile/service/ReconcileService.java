package cn.bootx.platform.daxpay.service.core.payment.reconcile.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.function.CollectorsFunction;
import cn.bootx.platform.common.core.util.CollUtil;
import cn.bootx.platform.daxpay.code.ReconcileTradeEnum;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.service.code.PaymentTypeEnum;
import cn.bootx.platform.daxpay.service.code.ReconcileDiffTypeEnum;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.order.reconcile.dao.ReconcileDetailManager;
import cn.bootx.platform.daxpay.service.core.order.reconcile.dao.ReconcileOrderManager;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.ReconcileDetail;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.ReconcileDiffRecord;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.ReconcileOrder;
import cn.bootx.platform.daxpay.service.core.order.reconcile.service.ReconcileDiffService;
import cn.bootx.platform.daxpay.service.core.order.reconcile.service.ReconcileOrderService;
import cn.bootx.platform.daxpay.service.core.payment.reconcile.domain.GeneralTradeInfo;
import cn.bootx.platform.daxpay.service.core.payment.reconcile.domain.ReconcileDiff;
import cn.bootx.platform.daxpay.service.core.payment.reconcile.factory.ReconcileStrategyFactory;
import cn.bootx.platform.daxpay.service.func.AbsReconcileStrategy;
import cn.bootx.platform.daxpay.util.OrderNoGenerateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
        ReconcileOrder order = new ReconcileOrder()
                .setReconcileNo(OrderNoGenerateUtil.reconciliation())
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
     * 手动传输对账单
     * @param id 对账单ID
     * @param file 文件
     */
    public void upload(Long id, MultipartFile file) {
        ReconcileOrder reconcileOrder = reconcileOrderService.findById(id)
                .orElseThrow(() -> new DataNotExistException("未找到对账订单"));
        // 将对账订单写入到上下文中
        PaymentContextLocal.get().getReconcileInfo().setReconcileOrder(reconcileOrder);
        AbsReconcileStrategy reconcileStrategy = ReconcileStrategyFactory.create(reconcileOrder.getChannel());
        reconcileStrategy.setRecordOrder(reconcileOrder);
        reconcileStrategy.doBeforeHandler();
        try {
            reconcileStrategy.upload(file);
            reconcileOrder.setDownOrUpload(true)
                    .setErrorMsg(null);
            reconcileOrderService.update(reconcileOrder);
        } catch (Exception e) {
            log.error("上传对账单异常", e);
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
     * 下载对账单并进行保存
     */
    public void downAndSave(ReconcileOrder reconcileOrder) {
        // 如果对账单已经存在
        if (reconcileOrder.isDownOrUpload()){
            throw new PayFailureException("对账单文件已经下载或上传");
        }
        // 将对账订单写入到上下文中
        PaymentContextLocal.get().getReconcileInfo().setReconcileOrder(reconcileOrder);
        // 构建对账策略
        AbsReconcileStrategy reconcileStrategy = ReconcileStrategyFactory.create(reconcileOrder.getChannel());
        reconcileStrategy.setRecordOrder(reconcileOrder);
        reconcileStrategy.doBeforeHandler();
        try {
            reconcileStrategy.downAndSave();
            reconcileOrder.setDownOrUpload(true)
                    .setErrorMsg(null);
            reconcileOrderService.update(reconcileOrder);
        } catch (Exception e) {
            log.error("下载对账单异常", e);
            reconcileOrder.setErrorMsg("原因: " + e.getMessage());
            // 本方法无事务, 更新信息不会被回滚
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
        if (!reconcileOrder.isDownOrUpload()){
            throw new PayFailureException("请先下载对账单");
        }
        // 是否对比完成
        if (reconcileOrder.isCompare()){
            throw new PayFailureException("对账单比对已经完成");
        }

        // 查询对账单
        List<ReconcileDetail> reconcileDetails = reconcileDetailManager.findAllByReconcileId(reconcileOrder.getId());
        // 构建对账策略
        AbsReconcileStrategy reconcileStrategy = ReconcileStrategyFactory.create(reconcileOrder.getChannel());
        // 初始化参数
        reconcileStrategy.setRecordOrder(reconcileOrder);
        reconcileStrategy.setReconcileDetails(reconcileDetails);
        try {
            // 执行比对任务, 获取对账差异记录并保存
            List<GeneralTradeInfo> generalTradeInfo = reconcileStrategy.getGeneralReconcileRecord();
            List<ReconcileDiffRecord> diffRecords = this.generateDiffRecord(reconcileOrder, generalTradeInfo,reconcileDetails);
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
     * @param reconcileOrder 对账单
     * @param localTrades 本地交易订单
     * @param outDetails 远程对账明细
     */
    private List<ReconcileDiffRecord> generateDiffRecord(ReconcileOrder reconcileOrder,
                                                         List<GeneralTradeInfo> localTrades,
                                                         List<ReconcileDetail> outDetails){
        if (CollUtil.isEmpty(outDetails) || CollUtil.isEmpty(localTrades)){
            return new ArrayList<>();
        }
        // 差异内容
        List<ReconcileDiffRecord> diffRecords = new ArrayList<>();

        // 三方对账订单
        Map<String, ReconcileDetail> outDetailMap = outDetails.stream()
                .collect(Collectors.toMap(ReconcileDetail::getTradeNo, Function.identity(), CollectorsFunction::retainLatest));
        // 本地订单记录
        Map<String, GeneralTradeInfo> localTradeMap = localTrades.stream()
                .collect(Collectors.toMap(GeneralTradeInfo::getTradeNo, Function.identity(), CollectorsFunction::retainLatest));
        // 对账与比对
        for (ReconcileDetail outDetail : outDetails) {
            // 判断本地有没有记录, 流水没有记录查询本地订单
            GeneralTradeInfo localTrade = localTradeMap.get(outDetail.getTradeNo());
            if (Objects.isNull(localTrade)){
                log.info("本地订单不存在: {}", outDetail.getTradeNo());
                ReconcileDiffRecord diffRecord = new ReconcileDiffRecord()
                        .setDiffType(ReconcileDiffTypeEnum.LOCAL_NOT_EXISTS.getCode())
                        .setTradeNo(outDetail.getTradeNo())
                        .setDetailId(outDetail.getId())
                        .setReconcileId(reconcileOrder.getId())
                        .setTitle(outDetail.getTitle())
                        .setOrderType(outDetail.getType())
                        .setOutOrderNo(outDetail.getOutTradeNo())
                        .setAmount(outDetail.getAmount())
                        .setTradeTime(outDetail.getTradeTime());
                diffRecords.add(diffRecord);
                continue;
            }
            // 如果远程和本地都存在, 比对差异
            List<ReconcileDiff> reconcileDiffs = this.reconcileDiff(outDetail, localTrade);
            if (CollUtil.isNotEmpty(reconcileDiffs)) {
                ReconcileDiffRecord diffRecord = new ReconcileDiffRecord()
                        .setReconcileId(reconcileOrder.getId())
                        .setDetailId(outDetail.getId())
                        .setDiffType(ReconcileDiffTypeEnum.NOT_MATCH.getCode())
                        .setTradeNo(outDetail.getTradeNo())
                        .setTitle(outDetail.getTitle())
                        .setOrderType(outDetail.getType())
                        .setOutOrderNo(outDetail.getOutTradeNo())
                        .setAmount(outDetail.getAmount())
                        .setDiffs(reconcileDiffs)
                        .setTradeTime(outDetail.getTradeTime());
                diffRecords.add(diffRecord);
            }
        }
        // 本地与对账单比对, 找出本地有, 远程没有的记录
        for (GeneralTradeInfo localTrade : localTrades) {
            ReconcileDetail outDetail = outDetailMap.get(localTrade.getTradeNo());
            if (Objects.isNull(outDetail)){
                ReconcileDiffRecord diffRecord = new ReconcileDiffRecord()
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
    public List<ReconcileDiff> reconcileDiff(ReconcileDetail outDetail, GeneralTradeInfo localTrade){
        List<ReconcileDiff> diffs = new ArrayList<>();

        // 判断类型是否相同
        if (Objects.equals(outDetail.getType(), ReconcileTradeEnum.PAY.getCode())
                && !Objects.equals(localTrade.getType(), PaymentTypeEnum.PAY.getCode())){
            log.warn("订单类型不一致: {},{}", outDetail.getType(), localTrade.getType());
            diffs.add(new ReconcileDiff().setFieldName("订单类型").setLocalValue(outDetail.getType()).setOutValue(localTrade.getType()));
        }
        if (Objects.equals(outDetail.getType(), ReconcileTradeEnum.REFUND.getCode())
                && !Objects.equals(localTrade.getType(), PaymentTypeEnum.REFUND.getCode())){
            log.warn("订单类型不一致: {},{}", outDetail.getType(), localTrade.getType());
            diffs.add(new ReconcileDiff().setFieldName("订单类型").setLocalValue(outDetail.getType()).setOutValue(localTrade.getType()));
        }

        // 判断名称是否一致
        if (!Objects.equals(outDetail.getTitle(), localTrade.getTitle())){
            log.warn("订单名称不一致: {},{}", outDetail.getTitle(), localTrade.getTitle());
            diffs.add(new ReconcileDiff().setFieldName("订单名称").setLocalValue(outDetail.getTitle()).setOutValue(localTrade.getTitle()));
        }

        // 判断金额是否一致
        if (!Objects.equals(outDetail.getAmount(), localTrade.getAmount())){
            log.warn("订单金额不一致: {},{}", outDetail.getAmount(), localTrade.getAmount());
            diffs.add(new ReconcileDiff().setFieldName("订单金额")
                    .setLocalValue(String.valueOf(outDetail.getAmount()))
                    .setOutValue(String.valueOf(localTrade.getAmount())));
        }
        return diffs;
    }

}
