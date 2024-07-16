package cn.daxpay.single.service.core.payment.sync.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.exception.RepetitiveOperationException;
import cn.daxpay.single.core.code.AllocOrderStatusEnum;
import cn.daxpay.single.core.param.payment.allocation.AllocSyncParam;
import cn.daxpay.single.core.result.sync.AllocSyncResult;
import cn.daxpay.single.service.code.TradeAdjustSourceEnum;
import cn.daxpay.single.service.code.TradeTypeEnum;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.order.allocation.dao.AllocOrderDetailManager;
import cn.daxpay.single.service.core.order.allocation.dao.AllocOrderManager;
import cn.daxpay.single.service.core.order.allocation.entity.AllocOrder;
import cn.daxpay.single.service.core.order.allocation.entity.AllocOrderDetail;
import cn.daxpay.single.service.core.payment.adjust.param.AllocAdjustParam;
import cn.daxpay.single.service.core.payment.adjust.service.AllocAdjustService;
import cn.daxpay.single.service.core.payment.notice.service.ClientNoticeService;
import cn.daxpay.single.service.core.payment.sync.result.AllocRemoteSyncResult;
import cn.daxpay.single.service.core.record.sync.entity.TradeSyncRecord;
import cn.daxpay.single.service.core.record.sync.service.TradeSyncRecordService;
import cn.daxpay.single.service.func.AbsAllocSyncStrategy;
import cn.daxpay.single.service.util.PayStrategyFactory;
import cn.hutool.core.util.StrUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    private final AllocAdjustService allocAdjustService;

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
            AbsAllocSyncStrategy allocSyncStrategy =  PayStrategyFactory.create(allocOrder.getChannel(), AbsAllocSyncStrategy.class);
            allocSyncStrategy.initParam(allocOrder);
            // 执行同步操作, 返账通用对账比对结果列表
            AllocRemoteSyncResult allocRemoteSyncResult = allocSyncStrategy.doSync();
            // 分账调整处理
            AllocAdjustParam allocAdjustParam = new AllocAdjustParam()
                    .setSource(TradeAdjustSourceEnum.SYNC)
                    .setOrder(allocOrder)
                    .setDetails(detailList)
                    .setResultItems(allocRemoteSyncResult.getResultItems());
            String adjustNo = allocAdjustService.adjust(allocAdjustParam);
            // 保存分账同步记录
            this.saveRecord(allocOrder, allocRemoteSyncResult, adjustNo);
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }


    /**
     * 保存同步记录
     */
    private void saveRecord(AllocOrder order, AllocRemoteSyncResult syncResult, String adjustNo){
        TradeSyncRecord tradeSyncRecord = new TradeSyncRecord()
                .setBizTradeNo(order.getBizAllocNo())
                .setTradeNo(order.getAllocNo())
                .setOutTradeNo(order.getOutAllocNo())
                .setType(TradeTypeEnum.ALLOCATION.getCode())
                .setAdjust(StrUtil.isNotBlank(adjustNo))
                .setAdjustNo(adjustNo)
                .setChannel(order.getChannel())
                .setSyncInfo(syncResult.getSyncInfo())
                .setErrorCode(syncResult.getErrorCode())
                .setErrorMsg(syncResult.getErrorMsg())
                .setClientIp(PaymentContextLocal.get().getClientInfo().getClientIp());
        tradeSyncRecordService.saveRecord(tradeSyncRecord);
    }
}
