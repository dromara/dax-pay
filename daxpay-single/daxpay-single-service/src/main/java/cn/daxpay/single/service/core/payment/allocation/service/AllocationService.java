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
import cn.daxpay.single.param.payment.allocation.AllocationParam;
import cn.daxpay.single.param.payment.allocation.QueryAllocOrderParam;
import cn.daxpay.single.result.allocation.AllocationResult;
import cn.daxpay.single.result.order.AllocOrderDetailResult;
import cn.daxpay.single.result.order.AllocOrderResult;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.order.allocation.convert.AllocOrderConvert;
import cn.daxpay.single.service.core.order.allocation.dao.AllocOrderDetailManager;
import cn.daxpay.single.service.core.order.allocation.dao.AllocationOrderManager;
import cn.daxpay.single.service.core.order.allocation.entity.AllocOrder;
import cn.daxpay.single.service.core.order.allocation.entity.AllocOrderDetail;
import cn.daxpay.single.service.core.order.allocation.entity.OrderAndDetail;
import cn.daxpay.single.service.core.order.allocation.service.AllocOrderService;
import cn.daxpay.single.service.core.order.pay.entity.PayOrder;
import cn.daxpay.single.service.core.order.pay.service.PayOrderQueryService;
import cn.daxpay.single.service.core.payment.allocation.dao.AllocationGroupManager;
import cn.daxpay.single.service.core.payment.allocation.entity.AllocationGroup;
import cn.daxpay.single.service.dto.allocation.AllocationGroupReceiverResult;
import cn.daxpay.single.service.func.AbsAllocationStrategy;
import cn.daxpay.single.service.util.PayStrategyFactory;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.daxpay.single.code.AllocOrderStatusEnum.ALLOCATION_END;
import static cn.daxpay.single.code.AllocOrderStatusEnum.FINISH_FAILED;

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

    private final AllocOrderService allocOrderService;

    private final AllocOrderDetailManager allocOrderDetailManager;

    private final AllocationAssistService allocationAssistService;

    private final PayOrderQueryService payOrderQueryService;

    private final LockTemplate lockTemplate;

    /**
     * 开启分账 多次请求只会分账一次
     * 优先级 分账接收方列表 > 分账组编号 > 默认分账组
     */
    public AllocationResult allocation(AllocationParam param) {
        // 判断是否已经有分账订单
        AllocOrder allocOrder = allocationOrderManager.findByBizAllocNo(param.getBizAllocationNo())
                .orElse(null);
        if (Objects.nonNull(allocOrder)){
            // 重复分账
            return this.retryAllocation(param, allocOrder);
        } else {
            // 首次分账
            PayOrder payOrder = this.getAndCheckPayOrder(param);
            return this.allocation(param, payOrder);
        }
    }

    /**
     * 开启分账  优先级 分账接收方列表 > 分账组编号 > 默认分账组
     */
    public AllocationResult allocation(AllocationParam param, PayOrder payOrder) {
        LockInfo lock = lockTemplate.lock("payment:allocation:" + payOrder.getId(),10000,200);
        if (Objects.isNull(lock)){
            throw new RepetitiveOperationException("分账发起处理中，请勿重复操作");
        }
        try {
            // 构建分账订单相关信息
            OrderAndDetail orderAndDetail = this.checkAndCreateAlloc(param, payOrder);
            // 检查是否需要进行分账
            AllocOrder order = orderAndDetail.getOrder();
            List<AllocOrderDetail> details = orderAndDetail.getDetails();
            // 无需进行分账,
            if (Objects.equals(order.getStatus(),AllocOrderStatusEnum.IGNORE.getCode())){
                return new AllocationResult()
                        .setAllocNo(order.getAllocNo())
                        .setBizAllocNo(order.getBizAllocNo())
                        .setStatus(order.getStatus());
            }

            // 创建分账策略并初始化
            AbsAllocationStrategy allocationStrategy = PayStrategyFactory.create(payOrder.getChannel(),AbsAllocationStrategy.class);
            allocationStrategy.initParam(order, details);
            try {
                // 分账预处理
                allocationStrategy.doBeforeHandler();
                // 分账处理
                allocationStrategy.allocation();
                // 执行中
                order.setStatus(AllocOrderStatusEnum.ALLOCATION_PROCESSING.getCode())
                        .setResult(AllocOrderResultEnum.ALL_PENDING.getCode())
                        .setErrorMsg(null);
            } catch (Exception e) {
                log.error("分账出现错误:", e);
                // 失败
                order.setStatus(AllocOrderStatusEnum.ALLOCATION_FAILED.getCode())
                        .setErrorMsg(e.getMessage());
                // TODO 返回异常处理
            }
            // 网关分账号
            String outAllocationNo = PaymentContextLocal.get()
                    .getAllocationInfo()
                    .getOutAllocationNo();
            order.setOutAllocNo(outAllocationNo);
            allocationOrderManager.updateById(order);
            return new AllocationResult()
                    .setAllocNo(order.getAllocNo())
                    .setBizAllocNo(order.getBizAllocNo())
                    .setStatus(order.getStatus());
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 重新分账
     */
    private AllocationResult retryAllocation(AllocationParam param, AllocOrder order){
        LockInfo lock = lockTemplate.lock("payment:allocation:" + order.getOrderId(),10000,200);
        if (Objects.isNull(lock)){
            throw new RepetitiveOperationException("分账发起处理中，请勿重复操作");
        }
        try {
            // 需要是分账中分账中或者完成状态才能重新分账
            List<String> list = Arrays.asList(ALLOCATION_END.getCode(),
                    AllocOrderStatusEnum.ALLOCATION_FAILED.getCode(),
                    AllocOrderStatusEnum.ALLOCATION_PROCESSING.getCode());
            if (!list.contains(order.getStatus())){
                throw new PayFailureException("分账单状态错误，无法重试");
            }
            List<AllocOrderDetail> details = this.getDetails(order.getId());
            // 创建分账策略并初始化
            AbsAllocationStrategy allocationStrategy =  PayStrategyFactory.create(order.getChannel(),AbsAllocationStrategy.class);
            allocationStrategy.initParam(order, details);
            // 分账预处理
            allocationStrategy.doBeforeHandler();
            //  更新分账单扩展信息
            allocationAssistService.updateOrder(param, order);
            try {
                // 重复分账处理
                allocationStrategy.allocation();
                order.setStatus(AllocOrderStatusEnum.ALLOCATION_PROCESSING.getCode())
                        .setErrorMsg(null);

            } catch (Exception e) {
                log.error("重新分账出现错误:", e);
                // TODO 失败
                order.setStatus(AllocOrderStatusEnum.ALLOCATION_FAILED.getCode())
                        .setErrorMsg(e.getMessage());
            }
            allocationOrderManager.updateById(order);
            return new AllocationResult()
                    .setAllocNo(order.getAllocNo())
                    .setBizAllocNo(order.getBizAllocNo())
                    .setStatus(order.getStatus());
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 分账完结
     */
    public AllocationResult finish(AllocFinishParam param) {
        AllocOrder allocOrder;
        if (Objects.nonNull(param.getAllocNo())){
            allocOrder = allocationOrderManager.findByAllocNo(param.getAllocNo())
                    .orElseThrow(() -> new DataNotExistException("未查询到分账单信息"));
        } else {
            allocOrder = allocationOrderManager.findByBizAllocNo(param.getBizAllocNo())
                    .orElseThrow(() -> new DataNotExistException("未查询到分账单信息"));
        }
        return this.finish(allocOrder);
    }

    /**
     * 分账完结
     */
    public AllocationResult finish(AllocOrder allocOrder) {
        // 只有分账结束后才可以完结
        if (!Arrays.asList(ALLOCATION_END.getCode(),FINISH_FAILED.getCode()).contains(allocOrder.getStatus())) {
            throw new PayFailureException("分账单状态错误");
        }
        List<AllocOrderDetail> details = this.getDetails(allocOrder.getId());

        // 创建分账策略并初始化
        AbsAllocationStrategy allocationStrategy =  PayStrategyFactory.create(allocOrder.getChannel(),AbsAllocationStrategy.class);
        allocationStrategy.initParam(allocOrder, details);

        // 分账完结预处理
        allocationStrategy.doBeforeHandler();
        try {
            // 完结处理
            allocationStrategy.finish();
            // 完结状态
            allocOrder.setStatus(AllocOrderStatusEnum.FINISH.getCode())
                    .setFinishTime(LocalDateTime.now())
                    .setErrorMsg(null);
        } catch (Exception e) {
            log.error("分账完结错误:", e);
            // 失败
            allocOrder.setStatus(FINISH_FAILED.getCode())
                    .setErrorMsg(e.getMessage());
        }
        allocationOrderManager.updateById(allocOrder);
        return new AllocationResult()
                .setAllocNo(allocOrder.getAllocNo())
                .setBizAllocNo(allocOrder.getBizAllocNo())
                .setStatus(allocOrder.getStatus());
    }

    /**
     * 获取并检查支付订单
     */
    private PayOrder getAndCheckPayOrder(AllocationParam param) {
        // 查询支付单
        PayOrder payOrder = payOrderQueryService.findByBizOrOrderNo(param.getOrderNo(), param.getBizOrderNo())
                .orElseThrow(() -> new PayFailureException("支付单不存在"));
        // 判断订单是否可以分账
        if (!payOrder.getAllocation()){
            throw new PayFailureException("该订单不允许分账");
        }
        // 判断分账状态
        if (Objects.equals(PayOrderAllocStatusEnum.ALLOCATION.getCode(), payOrder.getAllocStatus())){
            throw new PayFailureException("该订单已分账完成");
        }
        return payOrder;
    }

    /**
     * 查询分账结果
     */
    public AllocOrderResult queryAllocationOrder(QueryAllocOrderParam param) {
        // 查询分账单
        AllocOrder allocOrder = allocationOrderManager.findByAllocNo(param.getAllocNo())
                .orElseThrow(() -> new PayFailureException("分账单不存在"));
        AllocOrderResult result = AllocOrderConvert.CONVERT.toResult(allocOrder);
        // 查询分账单明细
        List<AllocOrderDetailResult> details = allocOrderDetailManager.findAllByOrderId(allocOrder.getId()).stream()
                .map(AllocOrderConvert.CONVERT::toResult)
                .collect(Collectors.toList());
        result.setDetails(details);
        return result;
    }

    /**
     * 构建分账订单相关信息
     */
    private OrderAndDetail checkAndCreateAlloc(AllocationParam param, PayOrder payOrder){
        // 创建分账单和明细并保存, 同时更新支付订单状态 使用事务
        OrderAndDetail orderAndDetail;
        // 判断是否传输了分账接收方列表
        if (CollUtil.isNotEmpty(param.getReceivers())) {
            orderAndDetail = allocOrderService.createAndUpdate(param, payOrder);
        } else {
            AllocationGroup allocationGroup;
            if (Objects.nonNull(param.getGroupNo())){
                // 指定分账组
                allocationGroup = groupManager.findByGroupNo(param.getGroupNo()).orElseThrow(() -> new DataNotExistException("未查询到分账组"));
            } else {
                // 默认分账组
                allocationGroup = groupManager.findDefaultGroup(payOrder.getChannel()).orElseThrow(() -> new PayFailureException("未查询到默认分账组"));
            }
            // 判断通道类型是否一致
            if (!Objects.equals(allocationGroup.getChannel(), payOrder.getChannel())){
                throw new PayFailureException("分账接收方列表存在非本通道的数据");
            }

            List<AllocationGroupReceiverResult> receiversByGroups = allocationGroupService.findReceiversByGroups(allocationGroup.getId());
            orderAndDetail = allocOrderService.createAndUpdate(param ,payOrder, receiversByGroups);
        }
        return orderAndDetail;
    }

    /**
     * 获取发起分账或完结的明细
     */
    private List<AllocOrderDetail> getDetails(Long allocOrderId){
        List<AllocOrderDetail> details = allocOrderDetailManager.findAllByOrderId(allocOrderId);
        // 过滤掉忽略的条目
        return details.stream()
                .filter(detail -> !Objects.equals(detail.getResult(), AllocDetailResultEnum.IGNORE.getCode()))
                .collect(Collectors.toList());
    }
}
