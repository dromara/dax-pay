package org.dromara.daxpay.service.service.allocation.order;

import cn.bootx.platform.core.exception.ValidationFailedException;
import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.core.enums.AllocDetailResultEnum;
import org.dromara.daxpay.core.enums.PayAllocStatusEnum;
import org.dromara.daxpay.core.exception.DataErrorException;
import org.dromara.daxpay.core.exception.OperationUnsupportedException;
import org.dromara.daxpay.core.exception.TradeNotExistException;
import org.dromara.daxpay.core.exception.TradeStatusErrorException;
import org.dromara.daxpay.core.param.allocation.transaction.AllocationParam;
import org.dromara.daxpay.service.dao.allocation.receiver.AllocGroupManager;
import org.dromara.daxpay.service.dao.allocation.transaction.AllocDetailManager;
import org.dromara.daxpay.service.dao.allocation.transaction.AllocOrderManager;
import org.dromara.daxpay.service.entity.allocation.receiver.AllocGroup;
import org.dromara.daxpay.service.entity.allocation.transaction.AllocOrder;
import org.dromara.daxpay.service.entity.allocation.transaction.AllocDetail;
import org.dromara.daxpay.service.entity.allocation.transaction.AllocAndDetail;
import org.dromara.daxpay.service.entity.order.pay.PayOrder;
import org.dromara.daxpay.service.service.allocation.receiver.AllocGroupService;
import org.dromara.daxpay.service.service.order.pay.PayOrderQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 分账支撑方法
 * @author xxm
 * @since 2024/5/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AllocationAssistService {

    private final AllocOrderManager transactionManager;

    private final PayOrderQueryService payOrderQueryService;

    private final AllocGroupManager groupManager;

    private final AllocGroupService allocationGroupService;

    private final AllocOrderService allocOrderService;

    private final AllocDetailManager allocOrderDetailManager;


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
     * 获取并检查支付订单
     */
    public PayOrder getAndCheckPayOrder(AllocationParam param) {
        // 查询支付单
        PayOrder payOrder = payOrderQueryService.findByBizOrOrderNo(param.getOrderNo(), param.getBizOrderNo(), param.getAppId())
                .orElseThrow(() -> new TradeNotExistException("支付单不存在"));
        // 判断订单是否可以分账
        if (!payOrder.getAllocation()){
            throw new OperationUnsupportedException("该订单不允许分账");
        }
        // 判断分账状态
        if (Objects.equals(PayAllocStatusEnum.ALLOCATION.getCode(), payOrder.getAllocStatus())){
            throw new TradeStatusErrorException("该订单已分账完成");
        }
        return payOrder;
    }

    /**
     * 构建分账订单相关信息
     */
    public AllocAndDetail checkAndCreateAlloc(AllocationParam param, PayOrder payOrder){
        // 创建分账单和明细并保存, 同时更新支付订单状态 使用事务
        AllocAndDetail orderAndDetail;
        // 判断是否传输了分账接收方列表
        if (CollUtil.isNotEmpty(param.getReceivers())) {
            orderAndDetail = allocOrderService.createAndUpdate(param, payOrder);
        } else {
            AllocGroup allocationGroup;
            if (Objects.nonNull(param.getGroupNo())){
                // 指定分账组
                allocationGroup = groupManager.findByGroupNo(param.getGroupNo(),param.getAppId()).orElseThrow(() -> new DataErrorException("未查询到分账组"));
            } else {
                // 默认分账组
                allocationGroup = groupManager.findDefaultGroup(payOrder.getChannel(),param.getAppId()).orElseThrow(() -> new DataErrorException("未查询到默认分账组"));
            }
            // 判断通道类型是否一致
            if (!Objects.equals(allocationGroup.getChannel(), payOrder.getChannel())){
                throw new ValidationFailedException("分账接收方列表存在非本通道的数据");
            }

            var receiversByGroups = allocationGroupService.findReceiversByGroups(allocationGroup.getId());
            orderAndDetail = allocOrderService.createAndUpdate(param ,payOrder, receiversByGroups);
        }
        return orderAndDetail;
    }

    /**
     * 获取发起分账或完结的明细
     */
    public List<AllocDetail> getDetails(Long allocOrderId){
        List<AllocDetail> details = allocOrderDetailManager.findAllByOrderId(allocOrderId);
        // 过滤掉忽略的条目
        return details.stream()
                .filter(detail -> !Objects.equals(detail.getResult(), AllocDetailResultEnum.IGNORE.getCode()))
                .collect(Collectors.toList());
    }
}
