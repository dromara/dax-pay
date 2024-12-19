package org.dromara.daxpay.service.service.allocation.order;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.core.enums.AllocDetailResultEnum;
import org.dromara.daxpay.core.enums.PayAllocStatusEnum;
import org.dromara.daxpay.core.exception.OperationUnsupportedException;
import org.dromara.daxpay.core.exception.TradeNotExistException;
import org.dromara.daxpay.core.exception.TradeStatusErrorException;
import org.dromara.daxpay.core.param.allocation.order.AllocationParam;
import org.dromara.daxpay.service.dao.allocation.order.AllocDetailManager;
import org.dromara.daxpay.service.dao.allocation.order.AllocOrderManager;
import org.dromara.daxpay.service.entity.allocation.order.AllocDetail;
import org.dromara.daxpay.service.entity.allocation.order.AllocOrder;
import org.dromara.daxpay.service.entity.order.pay.PayOrder;
import org.dromara.daxpay.service.param.order.allocation.AllocOrderQuery;
import org.dromara.daxpay.service.result.allocation.order.AllocDetailVo;
import org.dromara.daxpay.service.result.allocation.order.AllocOrderVo;
import org.dromara.daxpay.service.service.order.pay.PayOrderQueryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 分账订单查询服务类
 * @author xxm
 * @since 2024/5/30
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AllocOrderQueryService {

    private final AllocDetailManager allocOrderDetailManager;

    private final AllocOrderManager allocationOrderManager;
    private final PayOrderQueryService payOrderQueryService;

    /**
     * 分页查询
     */
    public PageResult<AllocOrderVo> page(PageParam pageParam, AllocOrderQuery param){
        return MpUtil.toPageResult(allocationOrderManager.page(pageParam, param));
    }

    /**
     * 查询详情
     */
    public AllocOrderVo findById(Long id) {
        return allocationOrderManager.findById(id).map(AllocOrder::toResult).orElseThrow(() -> new DataNotExistException("分账订单不存在"));
    }

    /**
     * 查询订单明细列表
     */
    public List<AllocDetailVo> findDetailsByOrderId(Long orderId){
        return MpUtil.toListResult(allocOrderDetailManager.findAllByOrderId(orderId));
    }

    /**
     * 查询订单明细详情
     */
    public AllocDetailVo findDetailById(Long id){
        return allocOrderDetailManager.findById(id).map(AllocDetail::toResult).orElseThrow(() -> new DataNotExistException("分账订单明细不存在"));
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
}

