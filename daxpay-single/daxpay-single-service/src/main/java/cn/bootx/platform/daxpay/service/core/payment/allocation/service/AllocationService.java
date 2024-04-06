package cn.bootx.platform.daxpay.service.core.payment.allocation.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.daxpay.param.pay.AllocationParam;
import cn.bootx.platform.daxpay.service.core.order.pay.dao.PayOrderManager;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.payment.allocation.dao.AllocationGroupManager;
import cn.bootx.platform.daxpay.service.core.payment.allocation.entity.AllocationGroup;
import cn.bootx.platform.daxpay.service.dto.allocation.AllocationGroupReceiverResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 分账服务
 * @author xxm
 * @since 2024/4/6
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AllocationService {

    private final PayOrderManager payOrderManager;

    private final AllocationGroupManager groupManager;

    private final AllocationGroupService allocationGroupService;

    /**
     * 开启分账
     */
    @Transactional(rollbackFor = Exception.class)
    public void allocation(AllocationParam param) {
        // 查询支付单
        PayOrder payOrder = null;
        if (Objects.nonNull(param.getPaymentId())){
            payOrder = payOrderManager.findById(param.getPaymentId())
                    .orElseThrow(() -> new DataNotExistException("未查询到支付订单"));
        }
        if (Objects.isNull(payOrder)){
            payOrder = payOrderManager.findByBusinessNo(param.getBusinessNo())
                    .orElseThrow(() -> new DataNotExistException("未查询到支付订单"));
        }
        AllocationGroup allocationGroup = groupManager.findById(param.getAllocationGroupId())
                .orElseThrow(() -> new DataNotExistException("未查询到分账组"));
        List<AllocationGroupReceiverResult> receiversByGroups = allocationGroupService.findReceiversByGroups(param.getAllocationGroupId());

    }

}
