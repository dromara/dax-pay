package org.dromara.daxpay.service.service.allocation;

import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.exception.RepetitiveOperationException;
import cn.bootx.platform.starter.redis.delay.service.DelayJobService;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.core.enums.AllocDetailResultEnum;
import org.dromara.daxpay.core.enums.AllocationResultEnum;
import org.dromara.daxpay.core.enums.AllocationStatusEnum;
import org.dromara.daxpay.core.enums.TradeTypeEnum;
import org.dromara.daxpay.core.param.allocation.order.AllocSyncParam;
import org.dromara.daxpay.core.result.allocation.AllocSyncResult;
import org.dromara.daxpay.service.bo.allocation.AllocSyncResultBo;
import org.dromara.daxpay.service.code.DaxPayCode;
import org.dromara.daxpay.service.common.local.PaymentContextLocal;
import org.dromara.daxpay.service.dao.allocation.order.AllocDetailManager;
import org.dromara.daxpay.service.dao.allocation.order.AllocOrderManager;
import org.dromara.daxpay.service.dao.allocation.AllocConfigManager;
import org.dromara.daxpay.service.entity.allocation.AllocConfig;
import org.dromara.daxpay.service.entity.allocation.order.AllocDetail;
import org.dromara.daxpay.service.entity.allocation.order.AllocOrder;
import org.dromara.daxpay.service.entity.record.sync.TradeSyncRecord;
import org.dromara.daxpay.service.service.assist.PaymentAssistService;
import org.dromara.daxpay.service.service.notice.MerchantNoticeService;
import org.dromara.daxpay.service.service.record.sync.TradeSyncRecordService;
import org.dromara.daxpay.service.strategy.AbsAllocationStrategy;
import org.dromara.daxpay.service.util.PaymentStrategyFactory;
import org.springframework.stereotype.Service;
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

    private final AllocOrderManager allocationOrderManager;

    private final AllocDetailManager allocOrderDetailManager;

    private final TradeSyncRecordService paySyncRecordService;

    private final LockTemplate lockTemplate;
    private final PaymentAssistService paymentAssistService;
    private final MerchantNoticeService merchantNoticeService;
    private final AllocConfigManager allocConfigManager;
    private final DelayJobService delayJobService;

    /**
     * 分账同步
     */
    @Transactional(rollbackFor = Exception.class)
    public void sync(Long id) {
        AllocOrder allocOrder = allocationOrderManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("分账单不存在"));
        // 如果类型为忽略, 不进行同步处理
        if (Objects.equals(allocOrder.getStatus(), AllocationStatusEnum.IGNORE.getCode())){
            return;
        }
        paymentAssistService.initMchApp(allocOrder.getAppId());
        // 调用同步逻辑
        this.sync(allocOrder);
    }

    /**
     * 分账同步
     */
    @Transactional(rollbackFor = Exception.class)
    public AllocSyncResult sync(AllocSyncParam param) {
        // 获取分账订单
        AllocOrder allocOrder = null;
        if (Objects.nonNull(param.getAllocNo())){
            allocOrder = allocationOrderManager.findByAllocNo(param.getAllocNo(),param.getAppId())
                    .orElseThrow(() -> new DataNotExistException("分账单不存在"));
        }
        if (Objects.isNull(allocOrder)){
            allocOrder = allocationOrderManager.findByAllocNo(param.getBizAllocNo(),param.getAppId())
                    .orElseThrow(() -> new DataNotExistException("分账单不存在"));
        }
        // 如果类型为忽略, 不进行同步处理
        if (Objects.equals(allocOrder.getStatus(), AllocationStatusEnum.IGNORE.getCode())){
            return new AllocSyncResult();
        }

        // 调用同步逻辑
        this.sync(allocOrder);
        return new AllocSyncResult().setResult(allocOrder.getResult()).setStatus(allocOrder.getStatus());
    }

    /**
     * 分账同步
     */
    @Transactional(rollbackFor = Exception.class)
    public void sync(AllocOrder allocOrder){
        LockInfo lock = lockTemplate.lock("payment:allocation:" + allocOrder.getOrderId(),10000,200);
        if (Objects.isNull(lock)){
            throw new RepetitiveOperationException("分账同步中，请勿重复操作");
        }
        String beforeStatus = allocOrder.getStatus();
        try {
            List<AllocDetail> detailList = allocOrderDetailManager.findAllByOrderId(allocOrder.getId());
            // 获取分账策略
            var allocationStrategy =  PaymentStrategyFactory.create(allocOrder.getChannel(),AbsAllocationStrategy.class);
            allocationStrategy.initParam(allocOrder, detailList);
            // 分账完结预处理
            allocationStrategy.doBeforeHandler();
            // 执行同步操作, 分账明细的状态变更会在这个里面
            AllocSyncResultBo allocSyncResultBo = allocationStrategy.doSync();
            // 保存分账同步记录
            this.saveRecord(allocOrder, allocSyncResultBo);
            // 根据订单明细更新订单的状态和处理结果
            allocOrder.setErrorMsg(null).setErrorCode(null);
            this.updateOrderStatus(allocOrder, detailList,beforeStatus);
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 根据订单明细更新订单的状态和处理结果, 如果订单是分账结束或失败, 不更新状态
     */
    private void updateOrderStatus(AllocOrder allocOrder, List<AllocDetail> details, String beforeStatus){
        // 如果是分账结束或失败, 不更新状态
        String status = allocOrder.getStatus();
        // 如果是分账结束或失败, 不进行对订单进行处理
        List<String> list = Arrays.asList(AllocationStatusEnum.FINISH.getCode(), AllocationStatusEnum.FINISH_FAILED.getCode());
        if (list.contains(status)) {
            return;
        }
        // 判断明细状态. 获取成功和失败的
        long successCount = details.stream()
                .map(AllocDetail::getResult)
                .filter(AllocDetailResultEnum.SUCCESS.getCode()::equals)
                .count();
        long failCount = details.stream()
                .map(AllocDetail::getResult)
                .filter(AllocDetailResultEnum.FAIL.getCode()::equals)
                .count();

        // 成功和失败都为0 表示进行中
        if (successCount == 0 && failCount == 0){
            allocOrder.setStatus(AllocationStatusEnum.PROCESSING.getCode())
                    .setResult(AllocationResultEnum.ALL_PENDING.getCode());
        } else {
            if (failCount == details.size()){
                // 全部失败
                allocOrder.setStatus(AllocationStatusEnum.ALLOC_END.getCode())
                        .setResult(AllocationResultEnum.ALL_FAILED.getCode());
            } else if (successCount == details.size()){
                // 全部成功
                allocOrder.setStatus(AllocationStatusEnum.ALLOC_END.getCode())
                        .setResult(AllocationResultEnum.ALL_SUCCESS.getCode());
            } else {
                // 部分成功
                allocOrder.setStatus(AllocationStatusEnum.ALLOC_END.getCode())
                        .setResult(AllocationResultEnum.PART_SUCCESS.getCode());
            }
        }
        allocOrderDetailManager.updateAllById(details);
        allocationOrderManager.updateById(allocOrder);

        // 如果状态为分账处理完成, 且与类型发生了变动, 发送通知
        if (Objects.equals(AllocationStatusEnum.ALLOC_END.getCode(), allocOrder.getStatus())
                && !Objects.equals(beforeStatus, AllocationStatusEnum.ALLOC_END.getCode())){
            // 注册通知 多次同步会产生多次变动, 注意处理多次推送通知的问题
            merchantNoticeService.registerAllocNotice(allocOrder, details);

            // 是否开启自动完结
            AllocConfig allocConfig = allocConfigManager.findByAppId(allocOrder.getAppId()).orElse(null);
            if (Objects.nonNull(allocConfig)&& allocConfig.getAutoFinish()){
                // 注册一分钟后的分账完结任务
                delayJobService.registerByTransaction(allocOrder.getId(), DaxPayCode.Event.ORDER_ALLOC_FINISH, 60 * 1000L);
            }
        }
    }


    /**
     * 保存同步记录
     */
    private void saveRecord(AllocOrder order, AllocSyncResultBo syncResult){
        TradeSyncRecord syncRecord = new TradeSyncRecord()
                .setBizTradeNo(order.getBizAllocNo())
                .setTradeNo(order.getAllocNo())
                .setOutTradeNo(order.getOutAllocNo())
                .setTradeType(TradeTypeEnum.ALLOCATION.getCode())
                .setChannel(order.getChannel())
                .setSyncInfo(syncResult.getSyncInfo())
                .setClientIp(PaymentContextLocal.get().getClientInfo().getClientIp());
        paySyncRecordService.saveRecord(syncRecord);
    }
}
