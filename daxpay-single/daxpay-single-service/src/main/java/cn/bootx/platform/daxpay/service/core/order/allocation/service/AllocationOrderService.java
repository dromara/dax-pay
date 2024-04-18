package cn.bootx.platform.daxpay.service.core.order.allocation.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.dto.LabelValue;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.core.util.ResultConvertUtil;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.code.AllocationDetailResultEnum;
import cn.bootx.platform.daxpay.code.AllocationOrderStatusEnum;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayOrderAllocationStatusEnum;
import cn.bootx.platform.daxpay.param.pay.allocation.AllocationStartParam;
import cn.bootx.platform.daxpay.service.core.order.allocation.dao.AllocationOrderDetailManager;
import cn.bootx.platform.daxpay.service.core.order.allocation.dao.AllocationOrderManager;
import cn.bootx.platform.daxpay.service.core.order.allocation.entity.AllocationOrder;
import cn.bootx.platform.daxpay.service.core.order.allocation.entity.AllocationOrderDetail;
import cn.bootx.platform.daxpay.service.core.order.allocation.entity.OrderAndDetail;
import cn.bootx.platform.daxpay.service.core.order.pay.dao.PayOrderManager;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.dto.allocation.AllocationGroupReceiverResult;
import cn.bootx.platform.daxpay.service.dto.order.allocation.AllocationOrderDetailDto;
import cn.bootx.platform.daxpay.service.dto.order.allocation.AllocationOrderDto;
import cn.bootx.platform.daxpay.service.param.order.AllocationOrderQuery;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 分账订单服务
 * @author xxm
 * @since 2024/4/7
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AllocationOrderService {
    private final AllocationOrderManager allocationOrderManager;
    private final AllocationOrderDetailManager allocationOrderDetailManager;

    private final PayOrderManager payOrderManager;


    /**
     * 获取可以分账的通道
     */
    public List<LabelValue> findChannels(){
        return Arrays.asList(
                new LabelValue(PayChannelEnum.ALI.getName(),PayChannelEnum.ALI.getCode()),
                new LabelValue(PayChannelEnum.WECHAT.getName(),PayChannelEnum.WECHAT.getCode())
        );
    }

    /**
     * 分页查询
     */
    public PageResult<AllocationOrderDto> page(PageParam pageParam, AllocationOrderQuery param){
        return MpUtil.convert2DtoPageResult(allocationOrderManager.page(pageParam, param));
    }


    /**
     * 查询详情
     */
    public AllocationOrderDto findById(Long id) {
        return allocationOrderManager.findById(id).map(AllocationOrder::toDto).orElseThrow(() -> new DataNotExistException("分账订单不存在"));
    }

    /**
     * 查询订单明细列表
     */
    public List<AllocationOrderDetailDto> findDetailsByOrderId(Long orderId){
        return ResultConvertUtil.dtoListConvert(allocationOrderDetailManager.findAllByOrderId(orderId));
    }

    /**
     * 查询订单明细详情
     */
    public AllocationOrderDetailDto findDetailById(Long id){
        return allocationOrderDetailManager.findById(id).map(AllocationOrderDetail::toDto).orElseThrow(() -> new DataNotExistException("分账订单明细不存在"));
    }


    /**
     * 生成分账订单
     */
    @Transactional(rollbackFor = Exception.class)
    public OrderAndDetail createAndUpdate(AllocationStartParam param, PayOrder payOrder, int orderAmount, List<AllocationGroupReceiverResult> receiversByGroups){
        long orderId = IdUtil.getSnowflakeNextId();

        // 请求号不存在使用订单ID
        String allocationNo = param.getAllocationNo();
        if (StrUtil.isBlank(allocationNo)){
            allocationNo = String.valueOf(orderId);
        }

        // 订单明细
        List<AllocationOrderDetail> details = receiversByGroups.stream()
                .map(o -> {
                    // 计算分账金额, 小数不分直接舍弃, 防止分账金额超过上限
                    Integer rate = o.getRate();
                    Integer amount = orderAmount * rate / 10000;
                    AllocationOrderDetail detail = new AllocationOrderDetail();
                    detail.setAllocationId(orderId)
                            .setReceiverId(o.getId())
                            .setAmount(amount)
                            .setResult(AllocationDetailResultEnum.PENDING.getCode())
                            .setRate(rate)
                            .setReceiverType(o.getReceiverType())
                            .setReceiverName(o.getReceiverName())
                            .setReceiverAccount(o.getReceiverAccount());
                    return detail;
                })
                .collect(Collectors.toList());
        // 求分账的总额
        Integer sumAmount = details.stream()
                .map(AllocationOrderDetail::getAmount)
                .reduce(0, Integer::sum);
        // 分账订单
        AllocationOrder allocationOrder = new AllocationOrder()
                .setPaymentId(payOrder.getId())
                .setTitle(payOrder.getTitle())
                .setAllocationNo(allocationNo)
                .setChannel(payOrder.getAsyncChannel())
                .setGatewayPayOrderNo(payOrder.getGatewayOrderNo())
                .setOrderNo(String.valueOf(orderId))
                .setDescription(param.getDescription())
                .setStatus(AllocationOrderStatusEnum.ALLOCATION_PROCESSING.getCode())
                .setAmount(sumAmount);
        allocationOrder.setId(orderId);
        // 更新支付订单分账状态
        payOrder.setAllocationStatus(PayOrderAllocationStatusEnum.ALLOCATION.getCode());
        payOrderManager.updateById(payOrder);
        // 因为加密后字段值会发生变更, 所以在保存前备份一下
        List<AllocationOrderDetail> detailsBack = details.stream()
                .map(o -> {
                    AllocationOrderDetail allocationOrderDetail = new AllocationOrderDetail();
                    BeanUtil.copyProperties(o, allocationOrderDetail);
                    return allocationOrderDetail;
                })
                .collect(Collectors.toList());
        allocationOrderDetailManager.saveAll(details);
        allocationOrderManager.save(allocationOrder);
        return new OrderAndDetail().setOrder(allocationOrder).setDetails(detailsBack);
    }

}
