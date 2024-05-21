package cn.daxpay.single.service.core.payment.allocation.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.exception.RepetitiveOperationException;
import cn.bootx.platform.common.core.util.CollUtil;
import cn.daxpay.single.code.AllocDetailResultEnum;
import cn.daxpay.single.code.AllocOrderResultEnum;
import cn.daxpay.single.code.AllocOrderStatusEnum;
import cn.daxpay.single.code.PayOrderAllocStatusEnum;
import cn.daxpay.single.exception.pay.PayFailureException;
import cn.daxpay.single.param.payment.allocation.AllocFinishParam;
import cn.daxpay.single.param.payment.allocation.AllocStartParam;
import cn.daxpay.single.param.payment.allocation.AllocSyncParam;
import cn.daxpay.single.param.payment.allocation.QueryAllocOrderParam;
import cn.daxpay.single.result.allocation.AllocOrderDetailResult;
import cn.daxpay.single.result.allocation.AllocOrderResult;
import cn.daxpay.single.result.allocation.AllocationResult;
import cn.daxpay.single.result.allocation.AllocationSyncResult;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.order.allocation.convert.AllocationConvert;
import cn.daxpay.single.service.core.order.allocation.dao.AllocationOrderDetailManager;
import cn.daxpay.single.service.core.order.allocation.dao.AllocationOrderManager;
import cn.daxpay.single.service.core.order.allocation.entity.AllocationOrder;
import cn.daxpay.single.service.core.order.allocation.entity.AllocationOrderDetail;
import cn.daxpay.single.service.core.order.allocation.entity.OrderAndDetail;
import cn.daxpay.single.service.core.order.allocation.service.AllocationOrderService;
import cn.daxpay.single.service.core.order.pay.entity.PayOrder;
import cn.daxpay.single.service.core.order.pay.service.PayOrderQueryService;
import cn.daxpay.single.service.core.payment.allocation.dao.AllocationGroupManager;
import cn.daxpay.single.service.core.payment.allocation.entity.AllocationGroup;
import cn.daxpay.single.service.core.payment.allocation.factory.AllocationFactory;
import cn.daxpay.single.service.dto.allocation.AllocationGroupReceiverResult;
import cn.daxpay.single.service.func.AbsAllocationStrategy;
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
import java.util.stream.Collectors;

/**
 * 分账服务
 * @author xxm
 * @since 2024/4/6
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AllocationService {

    private final AllocationGroupManager groupManager;

    private final AllocationOrderManager allocationOrderManager;

    private final AllocationGroupService allocationGroupService;

    private final AllocationOrderService allocationOrderService;

    private final AllocationOrderDetailManager allocationOrderDetailManager;

    private final PayOrderQueryService payOrderQueryService;

    private final LockTemplate lockTemplate;


    /**
     * 开启分账 多次请求只会分账一次
     * 优先级 分账接收方列表 > 分账组编号 > 默认分账组
     */
    public AllocationResult allocation(AllocStartParam param) {
        // 判断是否已经有分账订单
        AllocationOrder allocationOrder = allocationOrderManager.findByBizAllocationNo(param.getBizAllocationNo())
                .orElse(null);
        if (Objects.nonNull(allocationOrder)){
            // 重复分账
            return this.retryAllocation(allocationOrder);
        } else {
            // 首次分账
            PayOrder payOrder = this.getAndCheckPayOrder(param);
            return this.allocation(param, payOrder);
        }
    }

    /**
     * 开启分账  优先级 分账接收方列表 > 分账组编号 > 默认分账组
     */
    public AllocationResult allocation(AllocStartParam param, PayOrder payOrder) {
        LockInfo lock = lockTemplate.lock("payment:allocation:" + payOrder.getId(),10000,200);
        if (Objects.isNull(lock)){
            throw new RepetitiveOperationException("分账发起处理中，请勿重复操作");
        }
        try {
            // 创建分账单和明细并保存, 同时更新支付订单状态 使用事务
            OrderAndDetail orderAndDetail;
            // 判断是否传输了分账接收方列表
            if (CollUtil.isNotEmpty(param.getReceivers())) {
                orderAndDetail = allocationOrderService.createAndUpdate(param, payOrder);
            } else if (Objects.nonNull(param.getGroupNo())){
                // 指定分账组
                AllocationGroup allocationGroup = groupManager.findByGroupNo(param.getGroupNo()).orElseThrow(() -> new DataNotExistException("未查询到分账组"));
                List<AllocationGroupReceiverResult> receiversByGroups = allocationGroupService.findReceiversByGroups(allocationGroup.getId());
                orderAndDetail = allocationOrderService.createAndUpdate(param ,payOrder, receiversByGroups);
            } else {
                // 默认分账组
                AllocationGroup allocationGroup = groupManager.findDefaultGroup(payOrder.getChannel()).orElseThrow(() -> new PayFailureException("未查询到默认分账组"));
                List<AllocationGroupReceiverResult> receiversByGroups = allocationGroupService.findReceiversByGroups(allocationGroup.getId());
                orderAndDetail = allocationOrderService.createAndUpdate(param ,payOrder, receiversByGroups);
            }
            // 创建分账策略并初始化
            AbsAllocationStrategy allocationStrategy = AllocationFactory.create(payOrder.getChannel());
            AllocationOrder order = orderAndDetail.getOrder();
            List<AllocationOrderDetail> details = orderAndDetail.getDetails();
            allocationStrategy.initParam(order, details);
            // 分账预处理
            allocationStrategy.doBeforeHandler();
            try {
                // 分账处理
                allocationStrategy.allocation();
                // 执行中
                order.setStatus(AllocOrderStatusEnum.ALLOCATION_PROCESSING.getCode())
                        .setErrorMsg(null);
            } catch (Exception e) {
                log.error("分账出现错误:", e);
                // 失败
                order.setStatus(AllocOrderStatusEnum.ALLOCATION_FAILED.getCode())
                        .setErrorMsg(e.getMessage());
            }
            // 网关分账号
            String gatewayNo = PaymentContextLocal.get()
                    .getAllocationInfo()
                    .getOutAllocationNo();
            order.setOutAllocationNo(gatewayNo);
            allocationOrderManager.updateById(order);
            return new AllocationResult()
                    .setAllocationNo(order.getAllocationNo())
                    .setStatus(order.getStatus());
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 重新分账
     */
    private AllocationResult retryAllocation(AllocationOrder order){
        LockInfo lock = lockTemplate.lock("payment:allocation:" + order.getOrderId(),10000,200);
        if (Objects.isNull(lock)){
            throw new RepetitiveOperationException("分账发起处理中，请勿重复操作");
        }
        try {
            // 需要是分账中分账中或者完成状态才能重新分账
            List<String> list = Arrays.asList(AllocOrderStatusEnum.ALLOCATION_END.getCode(),
                    AllocOrderStatusEnum.ALLOCATION_FAILED.getCode(),
                    AllocOrderStatusEnum.ALLOCATION_PROCESSING.getCode());
            if (!list.contains(order.getStatus())){
                throw new PayFailureException("分账单状态错误，无法重试");
            }
            List<AllocationOrderDetail> details = allocationOrderDetailManager.findAllByOrderId(order.getId());
            // 创建分账策略并初始化
            AbsAllocationStrategy allocationStrategy = AllocationFactory.create(order.getChannel());
            allocationStrategy.initParam(order, details);

            // 分账预处理
            allocationStrategy.doBeforeHandler();
            try {
                // 重复分账处理
                allocationStrategy.allocation();
                order.setStatus(AllocOrderStatusEnum.ALLOCATION_PROCESSING.getCode())
                        .setErrorMsg(null);

            } catch (Exception e) {
                log.error("重新分账出现错误:", e);
                // 失败
                order.setStatus(AllocOrderStatusEnum.ALLOCATION_FAILED.getCode())
                        .setErrorMsg(e.getMessage());
            }
            allocationOrderManager.updateById(order);
            return new AllocationResult()
                    .setAllocationNo(order.getAllocationNo())
                    .setStatus(order.getStatus());
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 分账完结
     */
    public AllocationResult finish(AllocFinishParam param) {
        AllocationOrder allocationOrder;
        if (Objects.nonNull(param.getAllocationNo())){
            allocationOrder = allocationOrderManager.findByAllocationNo(param.getAllocationNo())
                    .orElseThrow(() -> new DataNotExistException("未查询到分账单信息"));
        } else {
            allocationOrder = allocationOrderManager.findByBizAllocationNo(param.getBizAllocationNo())
                    .orElseThrow(() -> new DataNotExistException("未查询到分账单信息"));
        }
        return this.finish(allocationOrder);
    }

    /**
     * 分账完结
     */
    public AllocationResult finish(AllocationOrder allocationOrder) {
        // 只有分账结束后才可以完结
        if (!AllocOrderStatusEnum.ALLOCATION_END.getCode().equals(allocationOrder.getStatus())){
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
            allocationOrder.setStatus(AllocOrderStatusEnum.FINISH.getCode())
                    .setErrorMsg(null);
        } catch (Exception e) {
            log.error("分账完结错误:", e);
            // 失败
            allocationOrder.setStatus(AllocOrderStatusEnum.FINISH_FAILED.getCode())
                    .setErrorMsg(e.getMessage());
        }
        allocationOrderManager.updateById(allocationOrder);
        return new AllocationResult()
                .setAllocationNo(allocationOrder.getAllocationNo())
                .setStatus(allocationOrder.getStatus());
    }

    /**
     * 分账同步, 开启一个新的事务, 不受外部抛出异常的影响
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public AllocationSyncResult sync(AllocSyncParam param) {
        // 获取分账订单
        AllocationOrder allocationOrder = null;
        if (Objects.nonNull(param.getAllocationNo())){
            allocationOrder = allocationOrderManager.findByAllocationNo(param.getAllocationNo())
                    .orElseThrow(() -> new DataNotExistException("分账单不存在"));
        }
        if (Objects.isNull(allocationOrder)){
            allocationOrder = allocationOrderManager.findByAllocationNo(param.getBizAllocationNo())
                    .orElseThrow(() -> new DataNotExistException("分账单不存在"));
        }
        this.sync(allocationOrder);
        return new AllocationSyncResult();
    }

    /**
     * 分账同步
     */
    public void sync(AllocationOrder allocationOrder){
        LockInfo lock = lockTemplate.lock("payment:allocation:" + allocationOrder.getOrderId(),10000,200);
        if (Objects.isNull(lock)){
            throw new RepetitiveOperationException("分账同步中，请勿重复操作");
        }
        try {
            List<AllocationOrderDetail> detailList = allocationOrderDetailManager.findAllByOrderId(allocationOrder.getId());
            // 获取分账策略
            AbsAllocationStrategy allocationStrategy = AllocationFactory.create(allocationOrder.getChannel());
            allocationStrategy.initParam(allocationOrder, detailList);
            // 分账完结预处理
            allocationStrategy.doBeforeHandler();
            allocationStrategy.doSync();
            // TODO 保存分账同步记录
            // 根据订单明细更新订单的状态和处理结果
            this.updateOrderStatus(allocationOrder, detailList);
            allocationOrderDetailManager.updateAllById(detailList);
            allocationOrderManager.updateById(allocationOrder);
        } finally {
            lockTemplate.releaseLock(lock);
        }
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
                .filter(AllocDetailResultEnum.SUCCESS.getCode()::equals)
                .count();
        long failCount = details.stream()
                .map(AllocationOrderDetail::getResult)
                .filter(AllocDetailResultEnum.FAIL.getCode()::equals)
                .count();

        // 成功和失败都为0 进行中
        if (successCount == 0 && failCount == 0){
            allocationOrder.setStatus(AllocOrderStatusEnum.ALLOCATION_PROCESSING.getCode())
                    .setResult(AllocOrderResultEnum.ALL_PENDING.getCode());
        } else if (failCount == details.size()){
            // 全部失败
            allocationOrder.setStatus(AllocOrderStatusEnum.ALLOCATION_END.getCode())
                    .setResult(AllocOrderResultEnum.ALL_FAILED.getCode());
        } else if (successCount == details.size()){
            // 全部成功
            allocationOrder.setStatus(AllocOrderStatusEnum.ALLOCATION_END.getCode())
                    .setResult(AllocOrderResultEnum.ALL_SUCCESS.getCode());
        } else {
            // 部分成功
            allocationOrder.setStatus(AllocOrderStatusEnum.ALLOCATION_END.getCode())
                    .setResult(AllocOrderResultEnum.PART_SUCCESS.getCode());
        }
        // 如果是分账结束或失败, 状态复原
        List<String> list = Arrays.asList(AllocOrderStatusEnum.FINISH.getCode(), AllocOrderStatusEnum.FINISH_FAILED.getCode());
        if (list.contains(status)){
            allocationOrder.setStatus(AllocOrderStatusEnum.FINISH.getCode())
                    .setResult(AllocOrderResultEnum.ALL_SUCCESS.getCode());
        }
    }

    /**
     * 获取并检查支付订单
     */
    private PayOrder getAndCheckPayOrder(AllocStartParam param) {
        // 查询支付单
        PayOrder payOrder = payOrderQueryService.findByBizOrOrderNo(param.getOrderNo(), param.getBizOrderNo())
                .orElseThrow(() -> new PayFailureException("支付单不存在"));
        // 判断订单是否可以分账
        if (!payOrder.getAllocation()){
            throw new PayFailureException("该订单不允许分账");
        }
        // 判断分账状态
        if (Objects.equals(PayOrderAllocStatusEnum.ALLOCATION.getCode(), payOrder.getAllocationStatus())){
            throw new PayFailureException("该订单已分账完成");
        }
        return payOrder;
    }

    /**
     * 查询分账结果
     */
    public AllocOrderResult queryAllocationOrder(QueryAllocOrderParam param) {
        // 查询分账单
        AllocationOrder allocationOrder = allocationOrderManager.findByAllocationNo(param.getAllocationNo())
                .orElseThrow(() -> new PayFailureException("分账单不存在"));
        AllocOrderResult result = AllocationConvert.CONVERT.toResult(allocationOrder);
        // 查询分账单明细
        List<AllocOrderDetailResult> details = allocationOrderDetailManager.findAllByOrderId(allocationOrder.getId()).stream()
                .map(AllocationConvert.CONVERT::toResult)
                .collect(Collectors.toList());
        result.setDetails(details);
        return result;
    }
}
