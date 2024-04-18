package cn.bootx.platform.daxpay.service.core.payment.allocation.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.daxpay.code.AllocationDetailResultEnum;
import cn.bootx.platform.daxpay.code.AllocationOrderResultEnum;
import cn.bootx.platform.daxpay.code.AllocationOrderStatusEnum;
import cn.bootx.platform.daxpay.code.PayOrderAllocationStatusEnum;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.param.pay.AllocationSyncParam;
import cn.bootx.platform.daxpay.param.pay.allocation.AllocationFinishParam;
import cn.bootx.platform.daxpay.param.pay.allocation.AllocationResetParam;
import cn.bootx.platform.daxpay.param.pay.allocation.AllocationStartParam;
import cn.bootx.platform.daxpay.result.allocation.AllocationResult;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.order.allocation.dao.AllocationOrderDetailManager;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
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

    private final AllocationOrderDetailManager allocationOrderDetailManager;

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
            order.setStatus(AllocationOrderStatusEnum.ALLOCATION_PROCESSING.getCode())
                    .setErrorMsg(null);
        } catch (Exception e) {
            // 失败
            order.setStatus(AllocationOrderStatusEnum.ALLOCATION_FAILED.getCode())
                    .setErrorMsg(e.getMessage());
        }
        // 网关分账号
        String gatewayNo = PaymentContextLocal.get()
                .getAllocationInfo()
                .getGatewayNo();
        order.setGatewayAllocationNo(gatewayNo);
        allocationOrderManager.updateById(order);

        return new AllocationResult().setOrderId(order.getId())
                .setAllocationNo(order.getAllocationNo())
                .setStatus(order.getStatus());
    }

    /**
     * 重新分账
     */
    public void retryAllocation(AllocationResetParam param){
        AllocationOrder allocationOrder;
        if (Objects.nonNull(param.getOrderId())){
            allocationOrder = allocationOrderManager.findById(param.getOrderId())
                    .orElseThrow(() -> new DataNotExistException("未查询到分账单信息"));
        } else {
            allocationOrder = allocationOrderManager.findByAllocationNo(param.getAllocationNo())
                    .orElseThrow(() -> new DataNotExistException("未查询到分账单信息"));
        }
        // 需要是分账中分账中或者完成状态才能重新分账
        List<String> list = Arrays.asList(AllocationOrderStatusEnum.ALLOCATION_END.getCode(),
                AllocationOrderStatusEnum.ALLOCATION_FAILED.getCode(),
                AllocationOrderStatusEnum.ALLOCATION_PROCESSING.getCode());
        if (!list.contains(allocationOrder.getStatus())){
            throw new PayFailureException("分账单状态错误");
        }

        List<AllocationOrderDetail> details = allocationOrderDetailManager.findAllByOrderId(allocationOrder.getId());

        // 创建分账策略并初始化
        AbsAllocationStrategy allocationStrategy = AllocationFactory.create(allocationOrder.getChannel());
        allocationStrategy.initParam(allocationOrder, details);

        // 分账预处理
        allocationStrategy.doBeforeHandler();
        try {
            // 重复分账处理
            allocationStrategy.allocation();
            allocationOrder.setStatus(AllocationOrderStatusEnum.ALLOCATION_PROCESSING.getCode())
                    .setErrorMsg(null);

        } catch (Exception e) {
            // 失败
            allocationOrder.setStatus(AllocationOrderStatusEnum.ALLOCATION_FAILED.getCode())
                    .setErrorMsg(e.getMessage());
        }
        allocationOrderManager.updateById(allocationOrder);
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
        // 只有分账结束后才可以完结
        if (!AllocationOrderStatusEnum.ALLOCATION_END.getCode().equals(allocationOrder.getStatus())){
            throw new PayFailureException("分账单状态错误");
        }

        List<AllocationOrderDetail> details = allocationOrderDetailManager.findAllByOrderId(allocationOrder.getId());
        // 创建分账策略并初始化
        AbsAllocationStrategy allocationStrategy = AllocationFactory.create(allocationOrder.getChannel());
        allocationStrategy.initParam(allocationOrder, details);

        // 分账完结预处理
        allocationStrategy.doBeforeHandler();
        try {
            // 完结处理
            allocationStrategy.finish();
            // 完结状态
            allocationOrder.setStatus(AllocationOrderStatusEnum.FINISH.getCode())
                    .setErrorMsg(null);
        } catch (Exception e) {
            // 失败
            allocationOrder.setStatus(AllocationOrderStatusEnum.FINISH_FAILED.getCode())
                    .setErrorMsg(e.getMessage());
        }
        allocationOrderManager.updateById(allocationOrder);
    }

    /**
     * 分账同步, 开启一个新的事务, 不受外部抛出异常的影响
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void sync(AllocationSyncParam param) {
        // 获取分账订单
        AllocationOrder allocationOrder = null;
        if (Objects.nonNull(param.getAllocationId())){
            allocationOrder = allocationOrderManager.findById(param.getAllocationId())
                    .orElseThrow(() -> new DataNotExistException("分账单不存在"));
        }
        if (Objects.isNull(allocationOrder)){
            allocationOrder = allocationOrderManager.findByAllocationNo(param.getAllocationNo())
                    .orElseThrow(() -> new DataNotExistException("分账单不存在"));
        }
        List<AllocationOrderDetail> detailList = allocationOrderDetailManager.findAllByOrderId(allocationOrder.getId());
        // 获取分账策略
        AbsAllocationStrategy allocationStrategy = AllocationFactory.create(allocationOrder.getChannel());
        allocationStrategy.initParam(allocationOrder, detailList);
        // 分账完结预处理
        allocationStrategy.doBeforeHandler();
        allocationStrategy.doSync();

        // 根据订单明细更新订单的状态和处理结果
        this.updateOrderStatus(allocationOrder, detailList);

        allocationOrderDetailManager.updateAllById(detailList);
        allocationOrderManager.updateById(allocationOrder);
    }

    /**
     * 根据订单明细更新订单的状态和处理结果, 如果订单是分账结束或失败, 不更新状态
     */
    private void updateOrderStatus(AllocationOrder allocationOrder, List<AllocationOrderDetail> details){
        // 如果是分账结束或失败, 不更新状态
        String status = allocationOrder.getStatus();
        // 判断明细状态. 获取成功和失败的
        long successCount = details.stream()
                .map(AllocationOrderDetail::getResult)
                .filter(AllocationDetailResultEnum.SUCCESS.getCode()::equals)
                .count();
        long failCount = details.stream()
                .map(AllocationOrderDetail::getResult)
                .filter(AllocationDetailResultEnum.FAIL.getCode()::equals)
                .count();

        // 成功和失败都为0 进行中
        if (successCount == 0 && failCount == 0){
            allocationOrder.setStatus(AllocationOrderStatusEnum.ALLOCATION_PROCESSING.getCode())
                    .setResult(AllocationOrderResultEnum.ALL_PENDING.getCode());
        } else if (failCount == details.size()){
            // 全部失败
            allocationOrder.setStatus(AllocationOrderStatusEnum.ALLOCATION_END.getCode())
                    .setResult(AllocationOrderResultEnum.ALL_FAILED.getCode());
        } else if (successCount == details.size()){
            // 全部成功
            allocationOrder.setStatus(AllocationOrderStatusEnum.ALLOCATION_END.getCode())
                    .setResult(AllocationOrderResultEnum.ALL_SUCCESS.getCode());
        } else {
            // 部分成功
            allocationOrder.setStatus(AllocationOrderStatusEnum.ALLOCATION_END.getCode())
                    .setResult(AllocationOrderResultEnum.PART_SUCCESS.getCode());
        }
        // 如果是分账结束或失败, 状态复原
        List<String> list = Arrays.asList(AllocationOrderStatusEnum.FINISH.getCode(), AllocationOrderStatusEnum.FINISH_FAILED.getCode());
        if (list.contains(status)){
            allocationOrder.setStatus(AllocationOrderStatusEnum.FINISH.getCode())
                    .setResult(AllocationOrderResultEnum.ALL_SUCCESS.getCode());
        }
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
        if (Objects.equals(PayOrderAllocationStatusEnum.ALLOCATION.getCode(), payOrder.getAllocationStatus())){
            throw new PayFailureException("该订单已分账完成");
        }
        return payOrder;
    }
}
