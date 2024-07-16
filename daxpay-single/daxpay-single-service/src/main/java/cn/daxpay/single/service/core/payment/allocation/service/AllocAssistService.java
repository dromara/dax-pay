package cn.daxpay.single.service.core.payment.allocation.service;

import cn.daxpay.single.core.param.payment.allocation.AllocationParam;
import cn.daxpay.single.service.core.order.allocation.dao.AllocOrderManager;
import cn.daxpay.single.service.core.order.allocation.entity.AllocOrder;
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
public class AllocAssistService {


    private final AllocOrderManager allocOrderManager;

    /**
     * 根据新传入的分账订单更新订单信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateOrder(AllocationParam allocationParam, AllocOrder orderExtra) {
        // 扩展信息
        orderExtra.setClientIp(allocationParam.getClientIp())
                .setNotifyUrl(allocationParam.getNotifyUrl())
                .setAttach(allocationParam.getAttach())
                .setReqTime(allocationParam.getReqTime());
        allocOrderManager.updateById(orderExtra);
    }
}
