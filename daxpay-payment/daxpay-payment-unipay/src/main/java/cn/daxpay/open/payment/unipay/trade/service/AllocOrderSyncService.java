package cn.daxpay.open.payment.unipay.trade.service;

import cn.daxpay.open.payment.trade.alloc.dao.AllocOrderManager;
import cn.daxpay.open.payment.trade.alloc.entity.AllocOrder;
import cn.daxpay.open.payment.trade.alloc.runtime.service.AllocSyncService;
import cn.daxpay.open.payment.unipay.param.trade.alloc.AllocSyncParam;
import cn.daxpay.open.payment.unipay.result.trade.alloc.AllocSyncResult;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 分账同步服务(对外)
@Slf4j
@Service
@RequiredArgsConstructor
public class AllocOrderSyncService {

    private final AllocOrderManager allocOrderManager;
    private final AllocSyncService allocSyncService;

    /// 分账同步(主动拉通道状态纠正)
    public AllocSyncResult sync(AllocSyncParam param) {
        if (StrUtil.isBlank(param.getAllocNo()) && StrUtil.isBlank(param.getBizAllocNo())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.orderNoRequired");
        }
        AllocOrder order;
        if (StrUtil.isNotBlank(param.getAllocNo())) {
            order = allocOrderManager.findByAllocNo(param.getAllocNo())
                    .orElseThrow(() -> new DataNotExistException("pay.error.alloc.allocNotFound"));
        } else {
            order = allocOrderManager.findByBizAllocNo(param.getBizAllocNo(), param.getMchNo())
                    .orElseThrow(() -> new DataNotExistException("pay.error.alloc.allocNotFound"));
        }
        String statusBefore = order.getStatus();
        allocSyncService.sync(order.getAllocNo());
        // 重新查询同步后状态
        AllocOrder synced = allocOrderManager.findByAllocNo(order.getAllocNo()).orElse(order);
        String statusAfter = synced.getStatus();
        boolean adjust = !Objects.equals(statusBefore, statusAfter);
        return new AllocSyncResult()
                .setOrderStatus(statusAfter)
                .setAdjust(adjust);
    }
}
