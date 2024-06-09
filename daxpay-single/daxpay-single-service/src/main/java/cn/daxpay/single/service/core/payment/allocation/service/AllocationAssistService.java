package cn.daxpay.single.service.core.payment.allocation.service;

import cn.daxpay.single.param.payment.allocation.AllocationParam;
import cn.daxpay.single.service.core.order.allocation.dao.AllocationOrderExtraManager;
import cn.daxpay.single.service.core.order.allocation.entity.AllocationOrderExtra;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 分账支撑方法
 * @author xxm
 * @since 2024/5/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AllocationAssistService {


    private final AllocationOrderExtraManager allocationOrderExtraManager;

    /**
     * 根据新传入的分账订单更新订单和扩展信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateOrder(AllocationParam allocationParam, AllocationOrderExtra orderExtra) {
        // 扩展信息
        orderExtra.setClientIp(allocationParam.getClientIp())
                .setNotifyUrl(allocationParam.getNotifyUrl())
                .setAttach(allocationParam.getAttach())
                .setClientIp(allocationParam.getClientIp())
                .setReqTime(allocationParam.getReqTime());
        allocationOrderExtraManager.updateById(orderExtra);
    }
}
