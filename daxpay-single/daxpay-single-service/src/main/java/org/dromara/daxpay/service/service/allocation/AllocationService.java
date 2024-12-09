package org.dromara.daxpay.service.service.allocation;

import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.exception.RepetitiveOperationException;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.core.enums.AllocationResultEnum;
import org.dromara.daxpay.core.enums.AllocationStatusEnum;
import org.dromara.daxpay.core.exception.DataErrorException;
import org.dromara.daxpay.core.exception.TradeStatusErrorException;
import org.dromara.daxpay.core.param.allocation.transaction.AllocFinishParam;
import org.dromara.daxpay.core.param.allocation.transaction.AllocationParam;
import org.dromara.daxpay.core.param.allocation.transaction.QueryAllocOrderParam;
import org.dromara.daxpay.core.result.allocation.order.AllocOrderResult;
import org.dromara.daxpay.core.result.allocation.order.AllocationResult;
import org.dromara.daxpay.service.bo.allocation.AllocStartResultBo;
import org.dromara.daxpay.service.convert.allocation.AllocOrderConvert;
import org.dromara.daxpay.service.dao.allocation.transaction.AllocDetailManager;
import org.dromara.daxpay.service.dao.allocation.transaction.AllocOrderManager;
import org.dromara.daxpay.service.entity.allocation.transaction.AllocDetail;
import org.dromara.daxpay.service.entity.allocation.transaction.AllocOrder;
import org.dromara.daxpay.service.entity.allocation.transaction.AllocAndDetail;
import org.dromara.daxpay.service.entity.order.pay.PayOrder;
import org.dromara.daxpay.service.service.allocation.order.AllocationAssistService;
import org.dromara.daxpay.service.strategy.AbsAllocationStrategy;
import org.dromara.daxpay.service.util.PaymentStrategyFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.dromara.daxpay.core.enums.AllocationStatusEnum.ALLOC_END;
import static org.dromara.daxpay.core.enums.AllocationStatusEnum.FINISH_FAILED;

/**
 * 分账处理
 * @author xxm
 * @since 2024/11/14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AllocationService {

    private final AllocOrderManager allocationOrderManager;

    private final AllocDetailManager allocOrderDetailManager;

    private final AllocationAssistService allocationAssistService;

    private final LockTemplate lockTemplate;

    /**
     * 开启分账 多次请求只会分账一次
     * 优先级 分账接收方列表 > 分账组编号 > 默认分账组
     */
    public AllocationResult allocation(AllocationParam param) {
        // 判断是否已经有分账订单
        var allocOrder = allocationOrderManager.findByBizAllocNo(param.getBizAllocNo(), param.getAppId()).orElse(null);
        if (Objects.nonNull(allocOrder)){
            // 重复分账
            return this.retryAlloc(param, allocOrder);
        } else {
            // 首次分账
            PayOrder payOrder = allocationAssistService.getAndCheckPayOrder(param);
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
            AllocAndDetail orderAndDetail = allocationAssistService.checkAndCreateAlloc(param, payOrder);
            // 检查是否需要进行分账
            var order = orderAndDetail.transaction();
            List<AllocDetail> details = orderAndDetail.details();
            // 无需进行分账,
            if (Objects.equals(order.getStatus(), AllocationStatusEnum.IGNORE.getCode())){
                return new AllocationResult()
                        .setAllocNo(order.getAllocNo())
                        .setBizAllocNo(order.getBizAllocNo())
                        .setStatus(order.getStatus());
            }
            // 创建分账策略并初始化
            var allocationStrategy = PaymentStrategyFactory.create(payOrder.getChannel(),AbsAllocationStrategy.class);
            allocationStrategy.initParam(order, details);
            try {
                // 分账预处理
                allocationStrategy.doBeforeHandler();
                // 分账处理
                AllocStartResultBo result = allocationStrategy.start();
                // 执行中
                order.setStatus(AllocationStatusEnum.PROCESSING.getCode())
                        .setResult(AllocationResultEnum.ALL_PENDING.getCode())
                        .setOutAllocNo(result.getOutAllocNo())
                        .setErrorMsg(null);
            } catch (Exception e) {
                log.error("分账出现错误:", e);
                // 失败
                order.setStatus(AllocationStatusEnum.ALLOC_FAILED.getCode())
                        .setErrorMsg(e.getMessage());
                // TODO 返回异常处理
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
     * 重新分账
     */
    private AllocationResult retryAlloc(AllocationParam param, AllocOrder order){
        LockInfo lock = lockTemplate.lock("payment:allocation:" + order.getOrderId(),10000,200);
        if (Objects.isNull(lock)){
            throw new RepetitiveOperationException("分账发起处理中，请勿重复操作");
        }
        try {
            // 需要是分账中分账中或者完成状态才能重新分账
            List<String> list = Arrays.asList(ALLOC_END.getCode(),
                    AllocationStatusEnum.ALLOC_FAILED.getCode(),
                    AllocationStatusEnum.PROCESSING.getCode());
            if (!list.contains(order.getStatus())){
                throw new TradeStatusErrorException("分账单状态错误，无法重试");
            }
            List<AllocDetail> details = allocationAssistService.getDetails(order.getId());
            // 创建分账策略并初始化
            var allocationStrategy =  PaymentStrategyFactory.create(order.getChannel(),AbsAllocationStrategy.class);
            allocationStrategy.initParam(order, details);
            // 分账预处理
            allocationStrategy.doBeforeHandler();
            //  更新分账单信息
            allocationAssistService.updateOrder(param, order);
            try {
                // 重复分账处理
                allocationStrategy.start();
                order.setStatus(AllocationStatusEnum.PROCESSING.getCode())
                        .setErrorMsg(null);

            } catch (Exception e) {
                log.error("重新分账出现错误:", e);
                order.setStatus(AllocationStatusEnum.ALLOC_FAILED.getCode())
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
            allocOrder = allocationOrderManager.findByAllocNo(param.getAllocNo(), param.getAppId())
                    .orElseThrow(() -> new DataNotExistException("未查询到分账单信息"));
        } else {
            allocOrder = allocationOrderManager.findByBizAllocNo(param.getBizAllocNo(), param.getAppId())
                    .orElseThrow(() -> new DataNotExistException("未查询到分账单信息"));
        }
        return this.finish(allocOrder);
    }

    /**
     * 分账完结
     */
    public AllocationResult finish(AllocOrder allocOrder) {
        // 只有分账结束后才可以完结
        if (!Arrays.asList(ALLOC_END.getCode(),FINISH_FAILED.getCode()).contains(allocOrder.getStatus())) {
            throw new TradeStatusErrorException("分账单状态错误");
        }
        List<AllocDetail> details = allocationAssistService.getDetails(allocOrder.getId());

        // 创建分账策略并初始化
        AbsAllocationStrategy allocationStrategy =  PaymentStrategyFactory.create(allocOrder.getChannel(),AbsAllocationStrategy.class);
        allocationStrategy.initParam(allocOrder, details);

        // 分账完结预处理
        allocationStrategy.doBeforeHandler();
        try {
            // 完结处理
            allocationStrategy.finish();
            // 完结状态
            allocOrder.setStatus(AllocationStatusEnum.FINISH.getCode())
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
     * 查询分账结果
     */
    public AllocOrderResult queryAllocOrder(QueryAllocOrderParam param) {
        // 查询分账单
        var allocOrder = allocationOrderManager.findByAllocNo(param.getAllocNo(), param.getAppId())
                .orElseThrow(() -> new DataErrorException("分账单不存在"));
        var result = AllocOrderConvert.CONVERT.toResult(allocOrder);
        // 查询分账单明细
        var details = allocOrderDetailManager.findAllByOrderId(allocOrder.getId()).stream()
                .map(AllocOrderConvert.CONVERT::toResult)
                .collect(Collectors.toList());
        result.setDetails(details);
        return result;
    }


}
