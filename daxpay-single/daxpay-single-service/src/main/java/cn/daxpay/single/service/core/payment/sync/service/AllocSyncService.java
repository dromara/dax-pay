package cn.daxpay.single.service.core.payment.sync.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.exception.RepetitiveOperationException;
import cn.daxpay.single.core.code.AllocDetailResultEnum;
import cn.daxpay.single.core.code.AllocOrderResultEnum;
import cn.daxpay.single.core.code.AllocOrderStatusEnum;
import cn.daxpay.single.core.param.payment.allocation.AllocSyncParam;
import cn.daxpay.single.core.result.sync.AllocSyncResult;
import cn.daxpay.single.service.code.TradeTypeEnum;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.order.allocation.dao.AllocOrderDetailManager;
import cn.daxpay.single.service.core.order.allocation.dao.AllocOrderManager;
import cn.daxpay.single.service.core.order.allocation.entity.AllocOrder;
import cn.daxpay.single.service.core.order.allocation.entity.AllocOrderDetail;
import cn.daxpay.single.service.core.payment.notice.service.ClientNoticeService;
import cn.daxpay.single.service.core.payment.sync.result.AllocRemoteSyncResult;
import cn.daxpay.single.service.core.record.sync.entity.TradeSyncRecord;
import cn.daxpay.single.service.core.record.sync.service.TradeSyncRecordService;
import cn.daxpay.single.service.func.AbsAllocStrategy;
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
public class AllocSyncService {

    private final ClientNoticeService clientNoticeService;

    private final AllocOrderManager allocOrderManager;

    private final AllocOrderDetailManager allocOrderDetailManager;

    private final TradeSyncRecordService tradeSyncRecordService;

    private final LockTemplate lockTemplate;

    /**
     * 分账同步, 开启一个新的事务, 不受外部抛出异常的影响
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public AllocSyncResult sync(AllocSyncParam param) {
        // 获取分账订单
        AllocOrder allocOrder = null;
        if (Objects.nonNull(param.getAllocNo())){
            allocOrder = allocOrderManager.findByAllocNo(param.getAllocNo())
                    .orElseThrow(() -> new DataNotExistException("分账单不存在"));
        }
        if (Objects.isNull(allocOrder)){
            allocOrder = allocOrderManager.findByAllocNo(param.getBizAllocNo())
                    .orElseThrow(() -> new DataNotExistException("分账单不存在"));
        }
        // 如果类型为忽略, 不进行同步处理
        if (Objects.equals(allocOrder.getStatus(), AllocOrderStatusEnum.IGNORE.getCode())){
            return new AllocSyncResult();
        }

        // 调用同步逻辑
        this.sync(allocOrder);
        return new AllocSyncResult();
    }

    /**
     * 分账同步
     */
    public void sync(AllocOrder allocOrder){
        LockInfo lock = lockTemplate.lock("payment:alloc:" + allocOrder.getOrderId(),10000,200);
        if (Objects.isNull(lock)){
            throw new RepetitiveOperationException("分账同步中，请勿重复操作");
        }
        try {
            List<AllocOrderDetail> detailList = allocOrderDetailManager.findAllByOrderId(allocOrder.getId());
            // 获取分账策略
            AbsAllocStrategy allocStrategy =  PayStrategyFactory.create(allocOrder.getChannel(),AbsAllocStrategy.class);
            allocStrategy.initParam(allocOrder, detailList);
            // 分账完结预处理
            allocStrategy.doBeforeHandler();
            // 执行同步操作, 分账明细的状态变更会在这个里面
            AllocRemoteSyncResult allocRemoteSyncResult = allocStrategy.doSync();
            // 保存分账同步记录
            this.saveRecord(allocOrder, allocRemoteSyncResult,null,null);
            // 根据订单明细更新订单的状态和处理结果
            this.updateOrderStatus(allocOrder, detailList);
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 根据订单明细更新订单的状态和处理结果, 如果订单是分账结束或失败, 不更新状态
     * TODO 是否多次同步会产生多次变动, 注意处理多次推送通知的问题, 目前是
     */
    private void updateOrderStatus(AllocOrder allocOrder, List<AllocOrderDetail> details){
        // 如果是分账结束或失败, 不更新状态
        String status = allocOrder.getStatus();

        // 如果是分账结束或失败, 不进行对订单进行处理
        List<String> list = Arrays.asList(AllocOrderStatusEnum.FINISH.getCode(), AllocOrderStatusEnum.FINISH_FAILED.getCode());
        if (!list.contains(status)){
            // 判断明细状态. 获取成功和失败的
            long successCount = details.stream()
                    .map(AllocOrderDetail::getResult)
                    .filter(AllocDetailResultEnum.SUCCESS.getCode()::equals)
                    .count();
            long failCount = details.stream()
                    .map(AllocOrderDetail::getResult)
                    .filter(AllocDetailResultEnum.FAIL.getCode()::equals)
                    .count();

            // 成功和失败都为0 表示进行中
            if (successCount == 0 && failCount == 0){
                allocOrder.setStatus(AllocOrderStatusEnum.ALLOCATION_PROCESSING.getCode())
                        .setResult(AllocOrderResultEnum.ALL_PENDING.getCode());
            } else {
                if (failCount == details.size()){
                    // 全部失败
                    allocOrder.setStatus(AllocOrderStatusEnum.ALLOCATION_END.getCode())
                            .setResult(AllocOrderResultEnum.ALL_FAILED.getCode());
                } else if (successCount == details.size()){
                    // 全部成功
                    allocOrder.setStatus(AllocOrderStatusEnum.ALLOCATION_END.getCode())
                            .setResult(AllocOrderResultEnum.ALL_SUCCESS.getCode());
                } else {
                    // 部分成功
                    allocOrder.setStatus(AllocOrderStatusEnum.ALLOCATION_END.getCode())
                            .setResult(AllocOrderResultEnum.PART_SUCCESS.getCode());
                }
            }
        }
        allocOrderDetailManager.updateAllById(details);
        allocOrderManager.updateById(allocOrder);

        // 如果状态为完成, 发送通知
        if (Objects.equals(AllocOrderStatusEnum.ALLOCATION_END.getCode(), allocOrder.getStatus())){
            // 发送通知
            clientNoticeService.registerAllocNotice(allocOrder, details);
        }
    }


    /**
     * 保存同步记录
     */
    private void saveRecord(AllocOrder order, AllocRemoteSyncResult syncResult, String errorCode, String errorMsg){
        TradeSyncRecord tradeSyncRecord = new TradeSyncRecord()
                .setBizTradeNo(order.getBizAllocNo())
                .setTradeNo(order.getAllocNo())
                .setOutTradeNo(order.getOutAllocNo())
                .setType(TradeTypeEnum.ALLOCATION.getCode())
                .setChannel(order.getChannel())
                .setSyncInfo(syncResult.getSyncInfo())
                .setErrorCode(errorCode)
                .setErrorMsg(errorMsg)
                .setClientIp(PaymentContextLocal.get().getClientInfo().getClientIp());
        tradeSyncRecordService.saveRecord(tradeSyncRecord);
    }
}
