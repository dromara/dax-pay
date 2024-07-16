package cn.daxpay.single.service.core.order.allocation.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.dto.LabelValue;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.core.util.ResultConvertUtil;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.single.core.code.PayChannelEnum;
import cn.daxpay.single.service.core.order.allocation.dao.AllocOrderDetailManager;
import cn.daxpay.single.service.core.order.allocation.dao.AllocOrderManager;
import cn.daxpay.single.service.core.order.allocation.entity.AllocOrder;
import cn.daxpay.single.service.core.order.allocation.entity.AllocOrderDetail;
import cn.daxpay.single.service.dto.order.allocation.AllocOrderDetailDto;
import cn.daxpay.single.service.dto.order.allocation.AllocOrderDto;
import cn.daxpay.single.service.param.order.AllocOrderQuery;
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
public class AllocOrderQueryService {

    private final AllocOrderDetailManager allocOrderDetailManager;

    private final AllocOrderManager allocOrderManager;

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
    public PageResult<AllocOrderDto> page(PageParam pageParam, AllocOrderQuery param){
        return MpUtil.convert2DtoPageResult(allocOrderManager.page(pageParam, param));
    }

    /**
     * 查询详情
     */
    public AllocOrderDto findById(Long id) {
        return allocOrderManager.findById(id).map(AllocOrder::toDto).orElseThrow(() -> new DataNotExistException("分账订单不存在"));
    }

    /**
     * 查询详情
     */
    public AllocOrderDto findByAllocNo(String allocNo) {
        return allocOrderManager.findByAllocNo(allocNo).map(AllocOrder::toDto).orElseThrow(() -> new DataNotExistException("分账订单不存在"));
    }

    /**
     * 查询订单明细列表
     */
    public List<AllocOrderDetailDto> findDetailsByOrderId(Long orderId){
        return ResultConvertUtil.dtoListConvert(allocOrderDetailManager.findAllByOrderId(orderId));
    }

    /**
     * 查询订单明细详情
     */
    public AllocOrderDetailDto findDetailById(Long id){
        return allocOrderDetailManager.findById(id).map(AllocOrderDetail::toDto).orElseThrow(() -> new DataNotExistException("分账订单明细不存在"));
    }

}
