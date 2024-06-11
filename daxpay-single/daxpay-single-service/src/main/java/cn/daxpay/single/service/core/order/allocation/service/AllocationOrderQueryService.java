package cn.daxpay.single.service.core.order.allocation.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.dto.LabelValue;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.core.util.ResultConvertUtil;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.single.code.PayChannelEnum;
import cn.daxpay.single.service.core.order.allocation.dao.AllocationOrderDetailManager;
import cn.daxpay.single.service.core.order.allocation.dao.AllocationOrderManager;
import cn.daxpay.single.service.core.order.allocation.entity.AllocationOrder;
import cn.daxpay.single.service.core.order.allocation.entity.AllocationOrderDetail;
import cn.daxpay.single.service.dto.order.allocation.AllocationOrderDetailDto;
import cn.daxpay.single.service.dto.order.allocation.AllocationOrderDto;
import cn.daxpay.single.service.param.order.AllocationOrderQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 分账订单查询服务类
 * @author xxm
 * @since 2024/5/30
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AllocationOrderQueryService {

    private final AllocationOrderDetailManager allocationOrderDetailManager;

    private final AllocationOrderManager allocationOrderManager;

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
     * 查询详情
     */
    public AllocationOrderDto findByAllocNo(String allocNo) {
        return allocationOrderManager.findByAllocationNo(allocNo).map(AllocationOrder::toDto).orElseThrow(() -> new DataNotExistException("分账订单不存在"));
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

}
