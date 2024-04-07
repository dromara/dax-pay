package cn.bootx.platform.daxpay.service.core.order.allocation.service;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.daxpay.param.pay.allocation.AllocationStartParam;
import cn.bootx.platform.daxpay.service.code.AllocationStatusEnum;
import cn.bootx.platform.daxpay.service.core.order.allocation.dao.AllocationOrderDetailManager;
import cn.bootx.platform.daxpay.service.core.order.allocation.dao.AllocationOrderManager;
import cn.bootx.platform.daxpay.service.core.order.allocation.entity.AllocationOrder;
import cn.bootx.platform.daxpay.service.core.order.allocation.entity.AllocationOrderDetail;
import cn.bootx.platform.daxpay.service.core.order.allocation.entity.OrderAndDetail;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.dto.allocation.AllocationGroupReceiverResult;
import cn.bootx.platform.daxpay.service.param.order.AllocationOrderQuery;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 分账订单服务
 * @author xxm
 * @since 2024/4/7
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AllocationOrderService {
    private final AllocationOrderManager allocationOrderManager;
    private final AllocationOrderDetailManager allocationOrderDetailManager;

    /**
     * 分页查询分账订单
     */
    public void page(PageParam pageParam, AllocationOrderQuery param){

    }

    /**
     * 生成分账订单
     */
    @Transactional(rollbackFor = Exception.class)
    public OrderAndDetail create(AllocationStartParam param, PayOrder payOrder, int orderAmount, List<AllocationGroupReceiverResult> receiversByGroups){
        long orderId = IdUtil.getSnowflakeNextId();

        // 请求号不存在使用订单ID
        String allocationNo = param.getAllocationNo();
        if (StrUtil.isBlank(allocationNo)){
            allocationNo = String.valueOf(orderId);
        }

        // 订单明细
        List<AllocationOrderDetail> details = receiversByGroups.stream()
                .map(o -> {
                    // 计算分账金额, 小数不分直接舍弃, 防止分账金额超过上限
                    Integer rate = o.getRate();
                    Integer amount = orderAmount * rate / 10000;
                    AllocationOrderDetail detail = new AllocationOrderDetail();
                    detail.setOrderId(orderId)
                            .setReceiverId(o.getId())
                            .setStatus(AllocationStatusEnum.WAITING.getCode())
                            .setAmount(amount)
                            .setRate(rate)
                            .setReceiverType(o.getReceiverType())
                            .setReceiverName(o.getReceiverName())
                            .setReceiverAccount(o.getReceiverAccount());
                    return detail;
                })
                .collect(Collectors.toList());
        // 求分账的总额
        Integer sumAmount = details.stream()
                .map(AllocationOrderDetail::getAmount)
                .reduce(0, Integer::sum);
        // 分账订单
        AllocationOrder allocationOrder = new AllocationOrder()
                .setPaymentId(payOrder.getId())
                .setAllocationNo(allocationNo)
                .setChannel(payOrder.getAsyncChannel())
                .setGatewayPayOrderNo(payOrder.getGatewayOrderNo())
                .setOutReqNo(String.valueOf(orderId))
                .setDescription(param.getDescription())
                .setStatus(AllocationStatusEnum.WAITING.getCode())
                .setAmount(sumAmount);
        allocationOrder.setId(orderId);
        // 保存
        allocationOrderDetailManager.saveAll(details);
        allocationOrderManager.save(allocationOrder);

        return new OrderAndDetail().setOrder(allocationOrder).setDetails(details);
    }

}
