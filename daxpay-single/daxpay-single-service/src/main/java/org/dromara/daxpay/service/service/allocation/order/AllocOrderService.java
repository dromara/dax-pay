package org.dromara.daxpay.service.service.allocation.order;

import cn.bootx.platform.core.exception.ValidationFailedException;
import cn.bootx.platform.core.util.BigDecimalUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.core.enums.AllocDetailResultEnum;
import org.dromara.daxpay.core.enums.AllocationResultEnum;
import org.dromara.daxpay.core.enums.AllocationStatusEnum;
import org.dromara.daxpay.core.enums.PayAllocStatusEnum;
import org.dromara.daxpay.core.exception.DataErrorException;
import org.dromara.daxpay.core.param.allocation.order.AllocationParam;
import org.dromara.daxpay.core.param.allocation.order.AllocationParam.ReceiverParam;
import org.dromara.daxpay.core.util.TradeNoGenerateUtil;
import org.dromara.daxpay.service.dao.allocation.receiver.AllocGroupManager;
import org.dromara.daxpay.service.dao.allocation.receiver.AllocReceiverManager;
import org.dromara.daxpay.service.dao.allocation.order.AllocDetailManager;
import org.dromara.daxpay.service.dao.allocation.order.AllocOrderManager;
import org.dromara.daxpay.service.dao.order.pay.PayOrderManager;
import org.dromara.daxpay.service.entity.allocation.receiver.AllocGroup;
import org.dromara.daxpay.service.entity.allocation.order.AllocAndDetail;
import org.dromara.daxpay.service.entity.allocation.order.AllocDetail;
import org.dromara.daxpay.service.entity.allocation.order.AllocOrder;
import org.dromara.daxpay.service.entity.order.pay.PayOrder;
import org.dromara.daxpay.service.result.allocation.receiver.AllocGroupReceiverVo;
import org.dromara.daxpay.service.service.allocation.receiver.AllocGroupService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 分账交易记录服务
 * @author xxm
 * @since 2024/11/14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AllocOrderService {

    private final AllocReceiverManager receiverManager;

    private final AllocOrderManager transactionManager;

    private final AllocGroupManager groupManager;

    private final AllocGroupService allocGroupService;

    private final AllocDetailManager transactionDetailManager;

    private final PayOrderManager payOrderManager;

    /**
     * 生成分账订单, 根据分账组创建
     */
    @Transactional(rollbackFor = Exception.class)
    public AllocAndDetail createAndUpdate(AllocationParam param, PayOrder payOrder, List<AllocGroupReceiverVo> receiversByGroups) {
        // 订单明细
        List<AllocDetail> details = receiversByGroups.stream()
                .map(o -> {
                    // 计算分账金额, 小数部分直接舍弃, 防止分账金额超过上限
                    // 等同于 payOrder.getAmount() * rate / 100;
                    var amount = payOrder.getAmount()
                            .multiply(o.getRate())
                            .divide(BigDecimal.valueOf(100), 2, RoundingMode.DOWN);

                    AllocDetail detail = new AllocDetail()
                            .setReceiverNo(o.getReceiverNo())
                            .setReceiverId(o.getReceiverId())
                            .setAmount(amount)
                            .setResult(AllocDetailResultEnum.PENDING.getCode())
                            .setRate(o.getRate())
                            .setReceiverType(o.getReceiverType())
                            .setReceiverName(o.getReceiverName())
                            .setReceiverAccount(o.getReceiverAccount());
                    // 如果金额为0, 设置为分账失败, 不参与分账
                    if (BigDecimalUtil.isEqual(amount, BigDecimal.ZERO)) {
                        detail.setResult(AllocDetailResultEnum.IGNORE.getCode())
                                .setErrorMsg("分账比例有误或金额太小, 无法进行分账")
                                .setFinishTime(LocalDateTime.now());
                    }
                    return detail;
                })
                .collect(Collectors.toList());
        return this.saveAllocOrder(param, payOrder, details);
    }

    /**
     * 生成分账订单, 通过传入的分账方创建
     */
    @Transactional(rollbackFor = Exception.class)
    public AllocAndDetail createAndUpdate(AllocationParam param, PayOrder payOrder) {
        List<String> receiverNos = param.getReceivers()
                .stream()
                .map(ReceiverParam::getReceiverNo)
                .distinct()
                .collect(Collectors.toList());
        if (receiverNos.size() != param.getReceivers()
                .size()) {
            throw new ValidationFailedException("分账接收方编号重复");
        }
        var receiverNoMap = param.getReceivers()
                .stream()
                .collect(Collectors.toMap(ReceiverParam::getReceiverNo, ReceiverParam::getAmount));
        // 查询分账接收方信息
        var receivers = receiverManager.findAllByReceiverNos(receiverNos, payOrder.getAppId());
        if (receivers.size() != receiverNos.size()) {
            throw new ValidationFailedException("分账接收方列表存在重复或无效的数据");
        }
        // 判断分账接收方类型是否都与分账订单类型匹配
        boolean anyMatch = receivers.stream()
                .anyMatch(o -> !Objects.equals(o.getChannel(), payOrder.getChannel()));
        if (anyMatch) {
            throw new ValidationFailedException("分账接收方列表存在非本通道的数据");
        }
        long allocId = IdUtil.getSnowflakeNextId();
        // 订单明细
        List<AllocDetail> details = receivers.stream()
                .map(o -> {
                    // 计算分账比例, 不是很精确
                    var amount = receiverNoMap.get(o.getReceiverNo());
                    var rate = amount
                            .divide(payOrder.getAmount(), 2, RoundingMode.DOWN)
                            .multiply(BigDecimal.valueOf(100));
                    AllocDetail detail = new AllocDetail();
                    detail.setAllocationId(allocId)
                            .setReceiverId(o.getId())
                            .setReceiverNo(o.getReceiverNo())
                            .setAmount(amount)
                            .setResult(AllocDetailResultEnum.PENDING.getCode())
                            .setRate(rate)
                            .setReceiverType(o.getReceiverType())
                            .setReceiverName(o.getReceiverName())
                            .setReceiverAccount(o.getReceiverAccount());
                    return detail;
                })
                .collect(Collectors.toList());
        return this.saveAllocOrder(param, payOrder, details);
    }

    /**
     * 保存分账相关订单信息
     */
    private AllocAndDetail saveAllocOrder(AllocationParam param, PayOrder payOrder, List<AllocDetail> details) {
        long allocId = IdUtil.getSnowflakeNextId();
        // 分账明细设置ID
        details.forEach(o -> o.setAllocationId(allocId));
        // 求分账的总额
        var sumAmount = details.stream()
                .map(AllocDetail::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        // 分账订单
        var allocOrder = new AllocOrder()
                .setOrderId(payOrder.getId())
                .setOrderNo(payOrder.getOrderNo())
                .setBizOrderNo(payOrder.getBizOrderNo())
                .setOutOrderNo(payOrder.getOutOrderNo())
                .setAllocNo(TradeNoGenerateUtil.allocation())
                .setBizAllocNo(param.getBizAllocNo())
                .setChannel(payOrder.getChannel())
                .setTitle(param.getTitle())
                .setDescription(param.getDescription())
                .setStatus(AllocationStatusEnum.PROCESSING.getCode())
                .setResult(AllocationResultEnum.ALL_PENDING.getCode())
                .setAmount(sumAmount)
                .setNotifyUrl(param.getNotifyUrl())
                .setAttach(param.getAttach())
                .setClientIp(param.getClientIp());
        // 订单标题为空, 则使用支付订单标题
        if (StrUtil.isBlank(allocOrder.getTitle())){
            allocOrder.setTitle(payOrder.getTitle());
        }
        // 如果分账订单金额为0, 设置为忽略状态
        if (BigDecimalUtil.isEqual(sumAmount, BigDecimal.ZERO)) {
            allocOrder.setStatus(AllocationStatusEnum.IGNORE.getCode())
                    .setFinishTime(LocalDateTime.now())
                    .setResult(AllocationStatusEnum.ALLOC_FAILED.getCode())
                    .setErrorMsg("分账比例有误或金额太小, 无法进行分账");
        }

        allocOrder.setId(allocId);
        // 更新支付订单分账状态
        payOrder.setAllocStatus(PayAllocStatusEnum.ALLOCATION.getCode());
        payOrderManager.updateById(payOrder);
        transactionDetailManager.saveAll(details);
        transactionManager.save(allocOrder);
        return new AllocAndDetail(allocOrder,details);
    }

    /**
     * 根据新传入的分账订单更新订单信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateOrder(AllocationParam allocationParam, AllocOrder orderExtra) {
        // 扩展信息
        orderExtra.setClientIp(allocationParam.getClientIp())
                .setNotifyUrl(allocationParam.getNotifyUrl())
                .setAttach(allocationParam.getAttach())
                .setReqTime(allocationParam.getReqTime());
        transactionManager.updateById(orderExtra);
    }


    /**
     * 检查并构建分账订单相关信息
     */
    @Transactional(rollbackFor = Exception.class)
    public AllocAndDetail checkAndCreateAlloc(AllocationParam param, PayOrder payOrder){
        // 创建分账单和明细并保存, 同时更新支付订单状态 使用事务
        AllocAndDetail orderAndDetail;
        // 判断是否传输了分账接收方列表
        if (CollUtil.isNotEmpty(param.getReceivers())) {
            orderAndDetail = this.createAndUpdate(param, payOrder);
        } else {
            AllocGroup allocationGroup;
            if (Objects.nonNull(param.getGroupNo())){
                // 指定分账组
                allocationGroup = groupManager.findByGroupNo(payOrder.getChannel(), param.getGroupNo(),param.getAppId())
                        .orElseThrow(() -> new DataErrorException("未查询到分账组"));
            } else {
                // 默认分账组
                allocationGroup = groupManager.findDefaultGroup(payOrder.getChannel(),param.getAppId())
                        .orElseThrow(() -> new DataErrorException("未查询到默认分账组"));
            }
            // 判断通道类型是否一致
            if (!Objects.equals(allocationGroup.getChannel(), payOrder.getChannel())){
                throw new ValidationFailedException("分账接收方列表存在非本通道的数据");
            }

            var receiversByGroups = allocGroupService.findReceiversByGroups(allocationGroup.getId());
            orderAndDetail = this.createAndUpdate(param ,payOrder, receiversByGroups);
        }
        return orderAndDetail;
    }
}

