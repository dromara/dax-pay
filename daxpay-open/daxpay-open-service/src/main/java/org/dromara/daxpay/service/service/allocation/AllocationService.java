package org.dromara.daxpay.service.service.allocation;

import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.exception.RepetitiveOperationException;
import cn.bootx.platform.starter.redis.delay.service.DelayJobService;
import org.dromara.daxpay.core.enums.AllocDetailResultEnum;
import org.dromara.daxpay.core.enums.AllocationResultEnum;
import org.dromara.daxpay.core.enums.AllocationStatusEnum;
import org.dromara.daxpay.core.enums.PayStatusEnum;
import org.dromara.daxpay.core.exception.DataErrorException;
import org.dromara.daxpay.core.exception.TradeStatusErrorException;
import org.dromara.daxpay.core.param.allocation.order.AllocFinishParam;
import org.dromara.daxpay.core.param.allocation.order.AllocationParam;
import org.dromara.daxpay.core.param.allocation.order.QueryAllocOrderParam;
import org.dromara.daxpay.core.result.allocation.AllocationResult;
import org.dromara.daxpay.core.result.allocation.order.AllocOrderResult;
import org.dromara.daxpay.service.code.DaxPayCode;
import org.dromara.daxpay.service.convert.allocation.AllocOrderConvert;
import org.dromara.daxpay.service.dao.allocation.AllocConfigManager;
import org.dromara.daxpay.service.dao.allocation.order.AllocDetailManager;
import org.dromara.daxpay.service.dao.allocation.order.AllocOrderManager;
import org.dromara.daxpay.service.entity.allocation.AllocConfig;
import org.dromara.daxpay.service.entity.allocation.order.AllocAndDetail;
import org.dromara.daxpay.service.entity.allocation.order.AllocDetail;
import org.dromara.daxpay.service.entity.allocation.order.AllocOrder;
import org.dromara.daxpay.service.entity.order.pay.PayOrder;
import org.dromara.daxpay.service.service.allocation.order.AllocOrderQueryService;
import org.dromara.daxpay.service.service.allocation.order.AllocOrderService;
import org.dromara.daxpay.service.service.assist.PaymentAssistService;
import org.dromara.daxpay.service.service.notice.MerchantNoticeService;
import org.dromara.daxpay.service.strategy.AbsAllocationStrategy;
import org.dromara.daxpay.service.util.PaymentStrategyFactory;
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

    private final LockTemplate lockTemplate;

    private final PaymentAssistService paymentAssistService;

    private final AllocOrderQueryService allocOrderQueryService;

    private final AllocOrderService allocOrderService;

    private final MerchantNoticeService merchantNoticeService;

    private final DelayJobService delayJobService;

    private final AllocConfigManager allocConfigManager;

    /**
     * 开启分账 多次请求只会分账一次
     * 优先级 分账接收方列表 > 分账组编号 > 默认分账组
     */
    public AllocationResult start(AllocationParam param) {
        // 判断是否已经有分账订单
        var allocOrder = allocationOrderManager.findByBizAllocNo(param.getBizAllocNo(), param.getAppId()).orElse(null);
        if (Objects.nonNull(allocOrder)){
            // 重复分账
            return this.retryAlloc(param, allocOrder);
        } else {
            // 首次分账
            PayOrder payOrder = allocOrderQueryService.getAndCheckPayOrder(param);
            return this.start(param, payOrder);
        }
    }

    /**
     * 开启分账  优先级 分账接收方列表 > 分账组编号 > 默认分账组
     */
    public AllocationResult start(AllocationParam param, PayOrder payOrder) {
        LockInfo lock = lockTemplate.lock("payment:allocation:" + payOrder.getId(),10000,200);
        if (Objects.isNull(lock)){
            throw new RepetitiveOperationException("分账发起处理中，请勿重复操作");
        }
        try {
            // 构建分账订单相关信息
            AllocAndDetail orderAndDetail = allocOrderService.checkAndCreateAlloc(param, payOrder);
            // 检查是否需要进行分账
            var allocOrder = orderAndDetail.transaction();
            List<AllocDetail> details = orderAndDetail.details();
            // 无需进行分账,
            if (Objects.equals(allocOrder.getStatus(), AllocationStatusEnum.IGNORE.getCode())){
                return new AllocationResult()
                        .setAllocNo(allocOrder.getAllocNo())
                        .setBizAllocNo(allocOrder.getBizAllocNo())
                        .setStatus(allocOrder.getStatus())
                        .setResult(allocOrder.getResult());
            }
            // 通用处理
            return startAlloc(allocOrder, details, param);
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 重新分账
     */
    public AllocationResult retryAlloc(Long id){
        var allocOrder = allocationOrderManager.findById(id).orElseThrow(() -> new DataNotExistException("分账单不存在"));
        AllocationParam param = new AllocationParam();
        param.setNotifyUrl(allocOrder.getNotifyUrl())
                .setAttach(allocOrder.getAttach())
                .setReqTime(allocOrder.getReqTime());
        param.setClientIp(allocOrder.getClientIp());
        return this.retryAlloc(param, allocOrder);
    }

    /**
     * 重新分账
     */
    private AllocationResult retryAlloc(AllocationParam param, AllocOrder allocOrder){
        LockInfo lock = lockTemplate.lock("payment:allocation:" + allocOrder.getOrderId(),10000,200);
        if (Objects.isNull(lock)){
            throw new RepetitiveOperationException("分账发起处理中，请勿重复操作");
        }
        try {
            // 需要是分账中分账中或者完成状态才能重新分账
            List<String> list = Arrays.asList(
                    AllocationStatusEnum.ALLOC_END.getCode(),
                    AllocationStatusEnum.ALLOC_FAILED.getCode(),
                    AllocationStatusEnum.PROCESSING.getCode());
            if (!list.contains(allocOrder.getStatus())){
                throw new TradeStatusErrorException("分账单状态错误，无法重试");
            }
            List<AllocDetail> details = allocOrderQueryService.getDetails(allocOrder.getId());
            // 通用处理
            return startAlloc(allocOrder, details, param);
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 分账发起通用处理
     */
    private AllocationResult startAlloc(AllocOrder allocOrder, List<AllocDetail> details, AllocationParam param){
        // 创建分账策略并初始化
        var allocationStrategy =  PaymentStrategyFactory.create(allocOrder.getChannel(), AbsAllocationStrategy.class);
        allocationStrategy.initParam(allocOrder, details);
        // 分账预处理
        allocationStrategy.doBeforeHandler();
        //  更新分账单信息
        allocOrderService.updateOrder(param, allocOrder);
        try {
            // 重复分账处理
            var result = allocationStrategy.start();
            // 如果返回分账成功, 走分账完成分支
            if (result.getResult() == AllocationResultEnum.ALL_SUCCESS){
                // 分账成功, 判断是否需要完结
                allocOrder.setStatus(result.isNeedFinish()?AllocationStatusEnum.ALLOC_END.getCode():AllocationStatusEnum.FINISH.getCode())
                        .setResult(AllocationResultEnum.ALL_SUCCESS.getCode())
                        .setOutAllocNo(result.getOutAllocNo())
                        .setFinishTime(result.getFinishTime())
                        .setErrorMsg(null);
                // 明细设置为成功
                details.forEach(o->o.setFinishTime(LocalDateTime.now()).setResult(AllocDetailResultEnum.SUCCESS.getCode()));
                // 发送通知
                merchantNoticeService.registerAllocNotice(allocOrder, details);
            } else {
                // 分账处理中
                allocOrder.setStatus(AllocationStatusEnum.PROCESSING.getCode())
                        .setResult(AllocationResultEnum.ALL_PENDING.getCode())
                        .setOutAllocNo(result.getOutAllocNo())
                        .setErrorMsg(null);
                // 注册两分钟后的分账同步事件
                delayJobService.registerByTransaction(allocOrder.getId(), DaxPayCode.Event.ORDER_ALLOC_SYNC, 2*60*1000L);
            }
        } catch (Exception e) {
            log.error("重新分账出现错误:", e);
            allocOrder.setStatus(AllocationStatusEnum.ALLOC_FAILED.getCode())
                    .setErrorMsg(e.getMessage());
        }
        return new AllocationResult()
                .setAllocNo(allocOrder.getAllocNo())
                .setBizAllocNo(allocOrder.getBizAllocNo())
                .setStatus(allocOrder.getStatus())
                .setResult(allocOrder.getResult());
    }

    /**
     * 自动分账完结
     */
    public void autoFinish(AllocOrder allocOrder) {
        if (!Arrays.asList(AllocationStatusEnum.ALLOC_END.getCode(),AllocationStatusEnum.FINISH_FAILED.getCode()).contains(allocOrder.getStatus())) {
            log.warn("忽略分账完结自动处理，分账单状态不正确:{}", allocOrder.getStatus());
            return;
        }
        paymentAssistService.initMchAndApp(allocOrder.getAppId());
        this.finish(allocOrder);
    }

    /**
     * 分账完结
     */
    public AllocationResult finish(Long id) {
        AllocOrder allocOrder = allocationOrderManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("未查询到分账单信息"));
        paymentAssistService.initMchAndApp(allocOrder.getAppId());
        return this.finish(allocOrder);
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
        if (!Arrays.asList(AllocationStatusEnum.ALLOC_END.getCode(),AllocationStatusEnum.FINISH_FAILED.getCode()).contains(allocOrder.getStatus())) {
            throw new TradeStatusErrorException("分账单状态错误");
        }
        List<AllocDetail> details = allocOrderQueryService.getDetails(allocOrder.getId());

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
            allocOrder.setStatus(AllocationStatusEnum.FINISH_FAILED.getCode())
                    .setErrorMsg(e.getMessage());
        }
        allocationOrderManager.updateById(allocOrder);
        merchantNoticeService.registerAllocNotice(allocOrder, details);
        return new AllocationResult()
                .setAllocNo(allocOrder.getAllocNo())
                .setBizAllocNo(allocOrder.getBizAllocNo())
                .setStatus(allocOrder.getStatus())
                .setResult(allocOrder.getResult());
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


    /**
     * 注册自动分账事件
     */
    public void registerAutoAlloc(PayOrder payOrder) {
        // 订单是否完成
        if (!Objects.equals(payOrder.getStatus(), PayStatusEnum.SUCCESS.getCode())){
            return;
        }
        // 是否开启自动分账
        if (payOrder.getAllocation() && payOrder.getAutoAllocation()){
            AllocConfig allocConfig = allocConfigManager.findByAppId(payOrder.getAppId()).orElse(null);
            if (Objects.nonNull(allocConfig)&&allocConfig.getAutoAlloc()){
                // 注册定时执行的分账完结任务, 如果未设置默认为一天后进行分账
                Integer delayTime = Optional.ofNullable(allocConfig.getDelayTime()).orElse(24*60);
                delayJobService.registerByTransaction(payOrder.getId(), DaxPayCode.Event.ORDER_ALLOC_START, delayTime*60*1000L);
            }
        }
    }
}
