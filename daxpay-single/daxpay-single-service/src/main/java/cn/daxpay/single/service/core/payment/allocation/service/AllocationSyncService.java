package cn.daxpay.single.service.core.payment.allocation.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.exception.RepetitiveOperationException;
import cn.daxpay.single.code.AllocDetailResultEnum;
import cn.daxpay.single.code.AllocOrderResultEnum;
import cn.daxpay.single.code.AllocOrderStatusEnum;
import cn.daxpay.single.param.payment.allocation.AllocSyncParam;
import cn.daxpay.single.result.sync.AllocSyncResult;
import cn.daxpay.single.service.code.PaymentTypeEnum;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.order.allocation.dao.AllocationOrderDetailManager;
import cn.daxpay.single.service.core.order.allocation.dao.AllocationOrderManager;
import cn.daxpay.single.service.core.order.allocation.entity.AllocationOrder;
import cn.daxpay.single.service.core.order.allocation.entity.AllocationOrderDetail;
import cn.daxpay.single.service.core.payment.notice.service.ClientNoticeService;
import cn.daxpay.single.service.core.payment.sync.result.AllocRemoteSyncResult;
import cn.daxpay.single.service.core.record.sync.entity.PaySyncRecord;
import cn.daxpay.single.service.core.record.sync.service.PaySyncRecordService;
import cn.daxpay.single.service.func.AbsAllocationStrategy;
import cn.daxpay.single.service.util.PayStrategyFactory;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 对账同步
 * @author xxm
 * @since 2024/5/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AllocationSyncService {

    private final ClientNoticeService clientNoticeService;

    private final AllocationOrderManager allocationOrderManager;

    private final AllocationOrderDetailManager allocationOrderDetailManager;

    private final PaySyncRecordService paySyncRecordService;

    private final LockTemplate lockTemplate;

    /**
     * 分账同步, 开启一个新的事务, 不受外部抛出异常的影响
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public AllocSyncResult sync(AllocSyncParam param) {
        // 获取分账订单
        AllocationOrder allocationOrder = null;
        if (Objects.nonNull(param.getAllocNo())){
            allocationOrder = allocationOrderManager.findByAllocationNo(param.getAllocNo())
                    .orElseThrow(() -> new DataNotExistException("分账单不存在"));
        }
        if (Objects.isNull(allocationOrder)){
            allocationOrder = allocationOrderManager.findByAllocationNo(param.getBizAllocNo())
                    .orElseThrow(() -> new DataNotExistException("分账单不存在"));
        }
        // 如果类型为忽略, 不进行同步处理
        if (Objects.equals(allocationOrder.getStatus(), AllocOrderStatusEnum.IGNORE.getCode())){
            return new AllocSyncResult();
        }

        // 调用同步逻辑
        this.sync(allocationOrder);
        return new AllocSyncResult();
    }

    /**
     * 分账同步
     */
    public void sync(AllocationOrder allocationOrder){
        LockInfo lock = lockTemplate.lock("payment:allocation:" + allocationOrder.getOrderId(),10000,200);
        if (Objects.isNull(lock)){
            throw new RepetitiveOperationException("分账同步中，请勿重复操作");
        }
        try {
            List<AllocationOrderDetail> detailList = allocationOrderDetailManager.findAllByOrderId(allocationOrder.getId());
            // 获取分账策略
            AbsAllocationStrategy allocationStrategy =  PayStrategyFactory.create(allocationOrder.getChannel(),AbsAllocationStrategy.class);
            allocationStrategy.initParam(allocationOrder, detailList);
            // 分账完结预处理
            allocationStrategy.doBeforeHandler();
            // 执行同步操作, 分账明细的状态变更会在这个里面
            AllocRemoteSyncResult allocRemoteSyncResult = allocationStrategy.doSync();
            // 保存分账同步记录
            this.saveRecord(allocationOrder, allocRemoteSyncResult,null,null);
            // 根据订单明细更新订单的状态和处理结果
            this.updateOrderStatus(allocationOrder, detailList);
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 根据订单明细更新订单的状态和处理结果, 如果订单是分账结束或失败, 不更新状态
     * TODO 是否多次同步会产生多次变动, 注意处理多次推送通知的问题, 目前是
     */
    private void updateOrderStatus(AllocationOrder allocationOrder, List<AllocationOrderDetail> details){
        // 如果是分账结束或失败, 不更新状态
        String status = allocationOrder.getStatus();

        // 如果是分账结束或失败, 不进行对订单进行处理
        List<String> list = Arrays.asList(AllocOrderStatusEnum.FINISH.getCode(), AllocOrderStatusEnum.FINISH_FAILED.getCode());
        if (!list.contains(status)){
            // 判断明细状态. 获取成功和失败的
            long successCount = details.stream()
                    .map(AllocationOrderDetail::getResult)
                    .filter(AllocDetailResultEnum.SUCCESS.getCode()::equals)
                    .count();
            long failCount = details.stream()
                    .map(AllocationOrderDetail::getResult)
                    .filter(AllocDetailResultEnum.FAIL.getCode()::equals)
                    .count();

            // 成功和失败都为0 表示进行中
            if (successCount == 0 && failCount == 0){
                allocationOrder.setStatus(AllocOrderStatusEnum.ALLOCATION_PROCESSING.getCode())
                        .setResult(AllocOrderResultEnum.ALL_PENDING.getCode());
            } else {
                if (failCount == details.size()){
                    // 全部失败
                    allocationOrder.setStatus(AllocOrderStatusEnum.ALLOCATION_END.getCode())
                            .setResult(AllocOrderResultEnum.ALL_FAILED.getCode());
                } else if (successCount == details.size()){
                    // 全部成功
                    allocationOrder.setStatus(AllocOrderStatusEnum.ALLOCATION_END.getCode())
                            .setResult(AllocOrderResultEnum.ALL_SUCCESS.getCode());
                } else {
                    // 部分成功
                    allocationOrder.setStatus(AllocOrderStatusEnum.ALLOCATION_END.getCode())
                            .setResult(AllocOrderResultEnum.PART_SUCCESS.getCode());
                }
            }
        }
        allocationOrderDetailManager.updateAllById(details);
        allocationOrderManager.updateById(allocationOrder);

        // 如果状态为完成, 发送通知
        if (Objects.equals(AllocOrderStatusEnum.ALLOCATION_END.getCode(), allocationOrder.getStatus())){
            // 发送通知
            clientNoticeService.registerAllocNotice(allocationOrder, details);
        }
    }


    /**
     * 保存同步记录
     */
    private void saveRecord(AllocationOrder order, AllocRemoteSyncResult syncResult, String errorCode, String errorMsg){
        PaySyncRecord paySyncRecord = new PaySyncRecord()
                .setBizTradeNo(order.getBizAllocNo())
                .setTradeNo(order.getAllocNo())
                .setOutTradeNo(order.getOutAllocNo())
                .setSyncType(PaymentTypeEnum.ALLOCATION.getCode())
                .setChannel(order.getChannel())
                .setSyncInfo(syncResult.getSyncInfo())
                .setErrorCode(errorCode)
                .setErrorMsg(errorMsg)
                .setClientIp(PaymentContextLocal.get().getRequestInfo().getClientIp());
        paySyncRecordService.saveRecord(paySyncRecord);
    }
}
