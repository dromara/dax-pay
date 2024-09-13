package cn.daxpay.single.service.core.order.allocation.service;

import cn.bootx.platform.common.core.exception.ValidationFailedException;
import cn.daxpay.single.core.code.AllocDetailResultEnum;
import cn.daxpay.single.core.code.AllocOrderResultEnum;
import cn.daxpay.single.core.code.AllocOrderStatusEnum;
import cn.daxpay.single.core.code.PayOrderAllocStatusEnum;
import cn.daxpay.single.core.param.payment.allocation.AllocReceiverParam;
import cn.daxpay.single.core.param.payment.allocation.AllocationParam;
import cn.daxpay.single.core.util.TradeNoGenerateUtil;
import cn.daxpay.single.service.core.order.allocation.dao.AllocOrderDetailManager;
import cn.daxpay.single.service.core.order.allocation.dao.AllocOrderManager;
import cn.daxpay.single.service.core.order.allocation.entity.AllocOrder;
import cn.daxpay.single.service.core.order.allocation.entity.AllocOrderDetail;
import cn.daxpay.single.service.core.order.allocation.entity.OrderAndDetail;
import cn.daxpay.single.service.core.order.pay.dao.PayOrderManager;
import cn.daxpay.single.service.core.order.pay.entity.PayOrder;
import cn.daxpay.single.service.core.payment.allocation.dao.AllocReceiverManager;
import cn.daxpay.single.service.core.payment.allocation.entity.AllocReceiver;
import cn.daxpay.single.service.dto.allocation.AllocGroupReceiverResult;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 分账订单服务
 * @author xxm
 * @since 2024/4/7
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AllocOrderService {
    private final AllocReceiverManager receiverManager;

    private final AllocOrderManager allocOrderManager;

    private final AllocOrderDetailManager allocOrderDetailManager;

    private final PayOrderManager payOrderManager;


    /**
     * 生成分账订单, 根据分账组创建
     */
    @Transactional(rollbackFor = Exception.class)
    public OrderAndDetail createAndUpdate(AllocationParam param, PayOrder payOrder, List<AllocGroupReceiverResult> receiversByGroups){
        // 订单明细
        List<AllocOrderDetail> details = receiversByGroups.stream()
                .map(o -> {
                    // 计算分账金额, 小数部分直接舍弃, 防止分账金额超过上限
                    Integer rate = o.getRate();
                    // 等同于 payOrder.getAmount() * rate / 10000;
                    int amount = BigDecimal.valueOf(payOrder.getAmount())
                            .multiply(BigDecimal.valueOf(rate))
                            .divide(BigDecimal.valueOf(10000), 0, RoundingMode.DOWN).intValue();

                    AllocOrderDetail detail = new AllocOrderDetail()
                            .setReceiverNo(o.getReceiverNo())
                            .setReceiverId(o.getId())
                            .setAmount(amount)
                            .setResult(AllocDetailResultEnum.PENDING.getCode())
                            .setRate(rate)
                            .setReceiverType(o.getReceiverType())
                            .setReceiverName(o.getReceiverName())
                            .setReceiverAccount(o.getReceiverAccount());
                    // 如果金额为0, 设置为分账失败, 不参与分账
                    if (amount == 0){
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
    public OrderAndDetail createAndUpdate(AllocationParam param, PayOrder payOrder) {
        List<String> receiverNos = param.getReceivers()
                .stream()
                .map(AllocReceiverParam::getReceiverNo)
                .distinct()
                .collect(Collectors.toList());
        if (receiverNos.size() != param.getReceivers().size()){
            throw new ValidationFailedException("分账接收方编号重复");
        }
        Map<String, Integer> receiverNoMap = param.getReceivers()
                .stream()
                .collect(Collectors.toMap(AllocReceiverParam::getReceiverNo, AllocReceiverParam::getAmount));
        // 查询分账接收方信息
        List<AllocReceiver> receivers = receiverManager.findAllByReceiverNos(receiverNos);
        if (receivers.size() != receiverNos.size()){
            throw new ValidationFailedException("分账接收方列表存在重复或无效的数据");
        }
        // 判断分账接收方类型是否都与分账订单类型匹配
        boolean anyMatch = receivers.stream()
                .anyMatch(o -> !Objects.equals(o.getChannel(), payOrder.getChannel()));
        if (anyMatch){
            throw new ValidationFailedException("分账接收方列表存在非本通道的数据");
        }


        long allocId = IdUtil.getSnowflakeNextId();

        // 订单明细
        List<AllocOrderDetail> details = receivers.stream()
                .map(o -> {
                    // 计算分账比例, 不是很精确
                    Integer amount = receiverNoMap.get(o.getReceiverNo());
                    Integer rate = BigDecimal.valueOf(amount)
                            .divide(BigDecimal.valueOf(payOrder.getAmount()), 4, RoundingMode.DOWN)
                            .multiply(BigDecimal.valueOf(10000)).intValue();
                    AllocOrderDetail detail = new AllocOrderDetail();
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
    private OrderAndDetail saveAllocOrder(AllocationParam param, PayOrder payOrder, List<AllocOrderDetail> details ) {
        long allocId = IdUtil.getSnowflakeNextId();
        // 分账明细设置ID
        details.forEach(o -> o.setAllocationId(allocId));
        // 求分账的总额
        Integer sumAmount = details.stream()
                .map(AllocOrderDetail::getAmount)
                .reduce(0, Integer::sum);
        // 分账订单
        AllocOrder allocOrder = new AllocOrder()
                .setOrderId(payOrder.getId())
                .setOrderNo(payOrder.getOrderNo())
                .setBizOrderNo(payOrder.getBizOrderNo())
                .setOutOrderNo(payOrder.getOutOrderNo())
                .setTitle(payOrder.getTitle())
                .setAllocNo(TradeNoGenerateUtil.allocation())
                .setBizAllocNo(param.getBizAllocNo())
                .setChannel(payOrder.getChannel())
                .setDescription(param.getDescription())
                .setStatus(AllocOrderStatusEnum.ALLOCATION_PROCESSING.getCode())
                .setResult(AllocOrderResultEnum.ALL_PENDING.getCode())
                .setAmount(sumAmount)
                .setNotifyUrl(param.getNotifyUrl())
                .setAttach(param.getAttach())
                .setClientIp(param.getClientIp());
        // 如果分账订单金额为0, 设置为忽略状态
        if (sumAmount == 0){
            allocOrder.setStatus(AllocOrderStatusEnum.IGNORE.getCode())
                    .setFinishTime(LocalDateTime.now())
                    .setResult(AllocOrderStatusEnum.ALLOCATION_FAILED.getCode())
                    .setErrorMsg("分账比例有误或金额太小, 无法进行分账");
        }

        allocOrder.setId(allocId);
        // 更新支付订单分账状态
        payOrder.setAllocStatus(PayOrderAllocStatusEnum.ALLOCATION.getCode());
        payOrderManager.updateById(payOrder);
        allocOrderDetailManager.saveAll(details);
        allocOrderManager.save(allocOrder);
        return new OrderAndDetail().setOrder(allocOrder).setDetails(details);
    }
}

