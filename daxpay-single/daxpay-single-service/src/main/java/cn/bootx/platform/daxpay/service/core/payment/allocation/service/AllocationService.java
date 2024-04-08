package cn.bootx.platform.daxpay.service.core.payment.allocation.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.param.pay.allocation.AllocationFinishParam;
import cn.bootx.platform.daxpay.param.pay.allocation.AllocationStartParam;
import cn.bootx.platform.daxpay.result.allocation.AllocationResult;
import cn.bootx.platform.daxpay.service.code.AllocationStatusEnum;
import cn.bootx.platform.daxpay.service.core.order.allocation.dao.AllocationOrderManager;
import cn.bootx.platform.daxpay.service.core.order.allocation.entity.AllocationOrder;
import cn.bootx.platform.daxpay.service.core.order.allocation.entity.AllocationOrderDetail;
import cn.bootx.platform.daxpay.service.core.order.allocation.entity.OrderAndDetail;
import cn.bootx.platform.daxpay.service.core.order.allocation.service.AllocationOrderService;
import cn.bootx.platform.daxpay.service.core.order.pay.dao.PayChannelOrderManager;
import cn.bootx.platform.daxpay.service.core.order.pay.dao.PayOrderManager;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayChannelOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.payment.allocation.dao.AllocationGroupManager;
import cn.bootx.platform.daxpay.service.core.payment.allocation.entity.AllocationGroup;
import cn.bootx.platform.daxpay.service.core.payment.allocation.factory.AllocationFactory;
import cn.bootx.platform.daxpay.service.dto.allocation.AllocationGroupReceiverResult;
import cn.bootx.platform.daxpay.service.func.AbsAllocationStrategy;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    private final PayChannelOrderManager payChannelOrderManager;

    private final AllocationGroupManager groupManager;

    private final AllocationOrderManager allocationOrderManager;

    private final AllocationGroupService allocationGroupService;

    private final AllocationOrderService allocationOrderService;

    /**
     * 开启分账, 使用分账组进行分账
     */
    public AllocationResult allocation(AllocationStartParam param) {

        PayOrder payOrder = this.getAndCheckPayOrder(param);

        // 查询待分账的通道支付订单
        PayChannelOrder channelOrder = payChannelOrderManager.findByAsyncChannel(payOrder.getId())
                .orElseThrow(() -> new DataNotExistException("未查询到支付通道订单"));

        // 查询分账组 未传输使用默认该通道默认分账组
        AllocationGroup allocationGroup;
        if (Objects.nonNull(param.getAllocationGroupId())) {
            allocationGroup = groupManager.findById(param.getAllocationGroupId()).orElseThrow(() -> new DataNotExistException("未查询到分账组"));
        } else {
            allocationGroup = groupManager.findDefaultGroup(payOrder.getAsyncChannel()).orElseThrow(() -> new DataNotExistException("未查询到默认分账组"));
        }
        List<AllocationGroupReceiverResult> receiversByGroups = allocationGroupService.findReceiversByGroups(allocationGroup.getId());

        // 创建分账单和明细并保存, 同时更新支付订单状态 使用事务
        OrderAndDetail orderAndDetail = allocationOrderService.createAndUpdate(param ,payOrder, channelOrder.getAmount(), receiversByGroups);

        // 创建分账策略并初始化
        AbsAllocationStrategy allocationStrategy = AllocationFactory.create(payOrder.getAsyncChannel());
        AllocationOrder order = orderAndDetail.getOrder();
        List<AllocationOrderDetail> details = orderAndDetail.getDetails();
        allocationStrategy.initParam(order, details);

        // 分账预处理
        allocationStrategy.doBeforeHandler();
        try {
            // 分账处理
            allocationStrategy.allocation();
            // 执行中
            order.setStatus(AllocationStatusEnum.PARTIAL_PROCESSING.getCode())
                    .setErrorMsg(null);
        } catch (Exception e) {
            // 失败
            order.setStatus(AllocationStatusEnum.PARTIAL_FAILED.getCode())
                    .setErrorMsg(e.getMessage());
        }
        allocationOrderManager.updateById(order);

        return new AllocationResult().setOrderId(order.getId())
                .setAllocationNo(order.getAllocationNo())
                .setStatus(order.getStatus());
    }

    /**
     * 分账完结
     */
    public void finish(AllocationFinishParam param) {
        AllocationOrder allocationOrder;
        if (Objects.nonNull(param.getOrderId())){
            allocationOrder = allocationOrderManager.findById(param.getOrderId())
                    .orElseThrow(() -> new DataNotExistException("未查询到分账单信息"));
        } else {
            allocationOrder = allocationOrderManager.findByAllocationNo(param.getAllocationNo())
                    .orElseThrow(() -> new DataNotExistException("未查询到分账单信息"));
        }

        // 创建分账策略并初始化
        AbsAllocationStrategy allocationStrategy = AllocationFactory.create(allocationOrder.getChannel());
        allocationStrategy.initParam(allocationOrder, null);

        // 分账完结预处理
        allocationStrategy.doBeforeHandler();
        try {
            // 分账处理
            allocationStrategy.finish();
            // 执行中
            allocationOrder.setStatus(AllocationStatusEnum.FINISH_PROCESSING.getCode())
                    .setErrorMsg(null);
        } catch (Exception e) {
            // 失败
            allocationOrder.setStatus(AllocationStatusEnum.FINISH_FAILED.getCode())
                    .setErrorMsg(e.getMessage());
        }

        allocationOrderManager.updateById(allocationOrder);
    }

    /**
     * 获取并检查支付订单
     */
    private PayOrder getAndCheckPayOrder(AllocationStartParam param) {
        // 查询支付单
        PayOrder payOrder = null;
        if (Objects.nonNull(param.getPaymentId())){
            payOrder = payOrderManager.findById(param.getPaymentId())
                    .orElseThrow(() -> new DataNotExistException("未查询到支付订单"));
        }
        if (StrUtil.isNotBlank(param.getBusinessNo())){
            payOrder = payOrderManager.findByBusinessNo(param.getBusinessNo())
                    .orElseThrow(() -> new DataNotExistException("未查询到支付订单"));
        }
        // 判断订单是否可以分账
        if (!payOrder.isAllocation()){
            throw new PayFailureException("该订单不允许分账");
        }
        // 判断分账状态
        if (Objects.equals(AllocationStatusEnum.FINISH_SUCCESS.getCode(), payOrder.getAllocationStatus())){
            throw new PayFailureException("该订单已分账完成");
        }
        return payOrder;
    }


}
