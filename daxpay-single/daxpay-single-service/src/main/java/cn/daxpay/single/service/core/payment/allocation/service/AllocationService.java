package cn.daxpay.single.service.core.payment.allocation.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.exception.RepetitiveOperationException;
import cn.bootx.platform.common.core.exception.ValidationFailedException;
import cn.bootx.platform.common.core.util.CollUtil;
import cn.bootx.platform.common.spring.util.WebServletUtil;
import cn.daxpay.single.core.code.AllocDetailResultEnum;
import cn.daxpay.single.core.code.AllocOrderResultEnum;
import cn.daxpay.single.core.code.AllocOrderStatusEnum;
import cn.daxpay.single.core.code.PayOrderAllocStatusEnum;
import cn.daxpay.single.core.exception.DataErrorException;
import cn.daxpay.single.core.exception.OperationUnsupportedException;
import cn.daxpay.single.core.exception.TradeNotExistException;
import cn.daxpay.single.core.exception.TradeStatusErrorException;
import cn.daxpay.single.core.param.payment.allocation.AllocFinishParam;
import cn.daxpay.single.core.param.payment.allocation.AllocationParam;
import cn.daxpay.single.core.param.payment.allocation.QueryAllocOrderParam;
import cn.daxpay.single.core.result.allocation.AllocationResult;
import cn.daxpay.single.core.result.order.AllocOrderDetailResult;
import cn.daxpay.single.core.result.order.AllocOrderResult;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.order.allocation.convert.AllocOrderConvert;
import cn.daxpay.single.service.core.order.allocation.dao.AllocOrderDetailManager;
import cn.daxpay.single.service.core.order.allocation.dao.AllocOrderManager;
import cn.daxpay.single.service.core.order.allocation.entity.AllocOrder;
import cn.daxpay.single.service.core.order.allocation.entity.AllocOrderDetail;
import cn.daxpay.single.service.core.order.allocation.entity.OrderAndDetail;
import cn.daxpay.single.service.core.order.allocation.service.AllocOrderService;
import cn.daxpay.single.service.core.order.pay.entity.PayOrder;
import cn.daxpay.single.service.core.order.pay.service.PayOrderQueryService;
import cn.daxpay.single.service.core.payment.allocation.dao.AllocGroupManager;
import cn.daxpay.single.service.core.payment.allocation.entity.AllocGroup;
import cn.daxpay.single.service.dto.allocation.AllocGroupReceiverResult;
import cn.daxpay.single.service.func.AbsAllocStrategy;
import cn.daxpay.single.service.util.PayStrategyFactory;
import cn.hutool.extra.servlet.ServletUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.daxpay.single.core.code.AllocOrderStatusEnum.ALLOCATION_END;
import static cn.daxpay.single.core.code.AllocOrderStatusEnum.FINISH_FAILED;

/**
 * 分账服务
 * @author xxm
 * @since 2024/4/6
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AllocationService {

    private final AllocGroupManager groupManager;

    private final AllocOrderManager allocOrderManager;

    private final AllocGroupService allocGroupService;

    private final AllocOrderService allocOrderService;

    private final AllocOrderDetailManager allocOrderDetailManager;

    private final AllocAssistService allocAssistService;

    private final PayOrderQueryService payOrderQueryService;

    private final LockTemplate lockTemplate;

    /**
     * 开启分账 多次请求只会分账一次
     * 优先级 分账接收方列表 > 分账组编号 > 默认分账组
     */
    public AllocationResult allocation(AllocationParam param) {
        // 判断是否已经有分账订单
        AllocOrder allocOrder = allocOrderManager.findByBizAllocNo(param.getBizAllocNo())
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
        LockInfo lock = lockTemplate.lock("payment:alloc:" + payOrder.getId(),10000,200);
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
            AbsAllocStrategy allocationStrategy = PayStrategyFactory.create(payOrder.getChannel(), AbsAllocStrategy.class);
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
            String outAllocNo = PaymentContextLocal.get()
                    .getAllocationInfo()
                    .getOutAllocNo();
            order.setOutAllocNo(outAllocNo);
            allocOrderManager.updateById(order);
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
        LockInfo lock = lockTemplate.lock("payment:alloc:" + order.getOrderId(),10000,200);
        if (Objects.isNull(lock)){
            throw new RepetitiveOperationException("分账发起处理中，请勿重复操作");
        }
        try {
            // 需要是分账中分账中或者完成状态才能重新分账
            List<String> list = Arrays.asList(ALLOCATION_END.getCode(),
                    AllocOrderStatusEnum.ALLOCATION_FAILED.getCode(),
                    AllocOrderStatusEnum.ALLOCATION_PROCESSING.getCode());
            if (!list.contains(order.getStatus())){
                throw new TradeStatusErrorException("分账单状态错误，无法重试");
            }
            List<AllocOrderDetail> details = this.getDetails(order.getId());
            // 创建分账策略并初始化
            AbsAllocStrategy allocationStrategy =  PayStrategyFactory.create(order.getChannel(), AbsAllocStrategy.class);
            allocationStrategy.initParam(order, details);
            // 分账预处理
            allocationStrategy.doBeforeHandler();
            //  更新分账单信息
            allocAssistService.updateOrder(param, order);
            try {
                // 重复分账处理
                allocationStrategy.allocation();
                order.setStatus(AllocOrderStatusEnum.ALLOCATION_PROCESSING.getCode())
                        .setErrorMsg(null);

            } catch (Exception e) {
                log.error("重新分账出现错误:", e);
                order.setStatus(AllocOrderStatusEnum.ALLOCATION_FAILED.getCode())
                        .setErrorMsg(e.getMessage());
            }
            allocOrderManager.updateById(order);
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
            allocOrder = allocOrderManager.findByAllocNo(param.getAllocNo())
                    .orElseThrow(() -> new DataNotExistException("未查询到分账单信息"));
        } else {
            allocOrder = allocOrderManager.findByBizAllocNo(param.getBizAllocNo())
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
            throw new TradeStatusErrorException("分账单状态错误");
        }
        List<AllocOrderDetail> details = this.getDetails(allocOrder.getId());

        // 创建分账策略并初始化
        AbsAllocStrategy allocationStrategy =  PayStrategyFactory.create(allocOrder.getChannel(), AbsAllocStrategy.class);
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
        allocOrderManager.updateById(allocOrder);
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
                .orElseThrow(() -> new TradeNotExistException("支付单不存在"));
        // 判断订单是否可以分账
        if (!payOrder.getAllocation()){
            throw new OperationUnsupportedException("该订单不允许分账");
        }
        // 判断分账状态
        if (Objects.equals(PayOrderAllocStatusEnum.ALLOCATION.getCode(), payOrder.getAllocStatus())){
            throw new TradeStatusErrorException("该订单已分账完成");
        }
        return payOrder;
    }

    /**
     * 查询分账结果
     */
    public AllocOrderResult queryAllocationOrder(QueryAllocOrderParam param) {
        // 查询分账单
        AllocOrder allocOrder = allocOrderManager.findByAllocNo(param.getAllocNo())
                .orElseThrow(() -> new DataErrorException("分账单不存在"));
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
            AllocGroup allocGroup;
            if (Objects.nonNull(param.getGroupNo())){
                // 指定分账组
                allocGroup = groupManager.findByGroupNo(param.getGroupNo()).orElseThrow(() -> new DataErrorException("未查询到分账组"));
            } else {
                // 默认分账组
                allocGroup = groupManager.findDefaultGroup(payOrder.getChannel()).orElseThrow(() -> new DataErrorException("未查询到默认分账组"));
            }
            // 判断通道类型是否一致
            if (!Objects.equals(allocGroup.getChannel(), payOrder.getChannel())){
                throw new ValidationFailedException("分账接收方列表存在非本通道的数据");
            }

            List<AllocGroupReceiverResult> receiversByGroups = allocGroupService.findReceiversByGroups(allocGroup.getId());
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

    /**
     * 手动重试
     */
    public void retry(String bizAllocNo) {
        AllocOrder allocOrder = allocOrderManager.findByBizAllocNo(bizAllocNo)
                .orElseThrow(() -> new DataErrorException("未查询到分账单"));
        String ip = Optional.ofNullable(WebServletUtil.getRequest())
                .map(ServletUtil::getClientIP)
                .orElse("未知");
        AllocationParam param = new AllocationParam();
        param.setBizAllocNo(allocOrder.getBizAllocNo());
        param.setAttach(allocOrder.getAttach());
        param.setNotifyUrl(allocOrder.getNotifyUrl());
        param.setClientIp(ip);
        this.retryAllocation(param, allocOrder);
    }
}
