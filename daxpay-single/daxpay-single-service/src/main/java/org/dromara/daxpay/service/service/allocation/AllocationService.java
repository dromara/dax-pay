package org.dromara.daxpay.service.service.allocation;

import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.exception.RepetitiveOperationException;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.core.enums.AllocTransactionResultEnum;
import org.dromara.daxpay.core.enums.AllocTransactionStatusEnum;
import org.dromara.daxpay.core.exception.DataErrorException;
import org.dromara.daxpay.core.exception.TradeStatusErrorException;
import org.dromara.daxpay.core.param.allocation.transaction.AllocFinishParam;
import org.dromara.daxpay.core.param.allocation.transaction.AllocationParam;
import org.dromara.daxpay.core.param.allocation.transaction.QueryAllocTransactionParam;
import org.dromara.daxpay.core.result.allocation.transaction.AllocResult;
import org.dromara.daxpay.core.result.allocation.transaction.AllocTransactionResult;
import org.dromara.daxpay.service.bo.allocation.receiver.AllocStartResultBo;
import org.dromara.daxpay.service.convert.allocation.AllocTransactionConvert;
import org.dromara.daxpay.service.dao.allocation.transaction.AllocDetailManager;
import org.dromara.daxpay.service.dao.allocation.transaction.AllocTransactionManager;
import org.dromara.daxpay.service.entity.allocation.transaction.AllocDetail;
import org.dromara.daxpay.service.entity.allocation.transaction.AllocTransaction;
import org.dromara.daxpay.service.entity.allocation.transaction.TransactionAndDetail;
import org.dromara.daxpay.service.entity.order.pay.PayOrder;
import org.dromara.daxpay.service.service.allocation.transaction.AllocAssistService;
import org.dromara.daxpay.service.strategy.AbsAllocationStrategy;
import org.dromara.daxpay.service.util.PaymentStrategyFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.dromara.daxpay.core.enums.AllocTransactionStatusEnum.ALLOC_END;
import static org.dromara.daxpay.core.enums.AllocTransactionStatusEnum.FINISH_FAILED;

/**
 * 分账处理
 * @author xxm
 * @since 2024/11/14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AllocationService {

    private final AllocTransactionManager allocationOrderManager;

    private final AllocDetailManager allocOrderDetailManager;

    private final AllocAssistService allocationAssistService;

    private final LockTemplate lockTemplate;

    /**
     * 开启分账 多次请求只会分账一次
     * 优先级 分账接收方列表 > 分账组编号 > 默认分账组
     */
    public AllocResult allocation(AllocationParam param) {
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
    public AllocResult allocation(AllocationParam param, PayOrder payOrder) {
        LockInfo lock = lockTemplate.lock("payment:allocation:" + payOrder.getId(),10000,200);
        if (Objects.isNull(lock)){
            throw new RepetitiveOperationException("分账发起处理中，请勿重复操作");
        }
        try {
            // 构建分账订单相关信息
            TransactionAndDetail orderAndDetail = allocationAssistService.checkAndCreateAlloc(param, payOrder);
            // 检查是否需要进行分账
            var order = orderAndDetail.transaction();
            List<AllocDetail> details = orderAndDetail.details();
            // 无需进行分账,
            if (Objects.equals(order.getStatus(), AllocTransactionStatusEnum.IGNORE.getCode())){
                return new AllocResult()
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
                order.setStatus(AllocTransactionStatusEnum.ALLOC_PROCESSING.getCode())
                        .setResult(AllocTransactionResultEnum.ALL_PENDING.getCode())
                        .setOutAllocNo(result.getOutAllocNo())
                        .setErrorMsg(null);
            } catch (Exception e) {
                log.error("分账出现错误:", e);
                // 失败
                order.setStatus(AllocTransactionStatusEnum.ALLOC_FAILED.getCode())
                        .setErrorMsg(e.getMessage());
                // TODO 返回异常处理
            }
            allocationOrderManager.updateById(order);
            return new AllocResult()
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
    private AllocResult retryAlloc(AllocationParam param, AllocTransaction order){
        LockInfo lock = lockTemplate.lock("payment:allocation:" + order.getOrderId(),10000,200);
        if (Objects.isNull(lock)){
            throw new RepetitiveOperationException("分账发起处理中，请勿重复操作");
        }
        try {
            // 需要是分账中分账中或者完成状态才能重新分账
            List<String> list = Arrays.asList(ALLOC_END.getCode(),
                    AllocTransactionStatusEnum.ALLOC_FAILED.getCode(),
                    AllocTransactionStatusEnum.ALLOC_PROCESSING.getCode());
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
                order.setStatus(AllocTransactionStatusEnum.ALLOC_PROCESSING.getCode())
                        .setErrorMsg(null);

            } catch (Exception e) {
                log.error("重新分账出现错误:", e);
                order.setStatus(AllocTransactionStatusEnum.ALLOC_FAILED.getCode())
                        .setErrorMsg(e.getMessage());
            }
            allocationOrderManager.updateById(order);
            return new AllocResult()
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
    public AllocResult finish(AllocFinishParam param) {
        AllocTransaction allocOrder;
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
    public AllocResult finish(AllocTransaction allocOrder) {
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
            allocOrder.setStatus(AllocTransactionStatusEnum.FINISH.getCode())
                    .setFinishTime(LocalDateTime.now())
                    .setErrorMsg(null);
        } catch (Exception e) {
            log.error("分账完结错误:", e);
            // 失败
            allocOrder.setStatus(FINISH_FAILED.getCode())
                    .setErrorMsg(e.getMessage());
        }
        allocationOrderManager.updateById(allocOrder);
        return new AllocResult()
                .setAllocNo(allocOrder.getAllocNo())
                .setBizAllocNo(allocOrder.getBizAllocNo())
                .setStatus(allocOrder.getStatus());
    }


    /**
     * 查询分账结果
     */
    public AllocTransactionResult queryAllocTransaction(QueryAllocTransactionParam param) {
        // 查询分账单
        var allocOrder = allocationOrderManager.findByAllocNo(param.getAllocNo(), param.getAppId())
                .orElseThrow(() -> new DataErrorException("分账单不存在"));
        var result = AllocTransactionConvert.CONVERT.toResult(allocOrder);
        // 查询分账单明细
        var details = allocOrderDetailManager.findAllByOrderId(allocOrder.getId()).stream()
                .map(AllocTransactionConvert.CONVERT::toResult)
                .collect(Collectors.toList());
        result.setDetails(details);
        return result;
    }


}
