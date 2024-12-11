package org.dromara.daxpay.service.service.allocation.order;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.service.dao.allocation.transaction.AllocDetailManager;
import org.dromara.daxpay.service.dao.allocation.transaction.AllocOrderManager;
import org.dromara.daxpay.service.entity.allocation.transaction.AllocDetail;
import org.dromara.daxpay.service.entity.allocation.transaction.AllocOrder;
import org.dromara.daxpay.service.param.order.allocation.AllocOrderQuery;
import org.dromara.daxpay.service.result.allocation.order.AllocDetailVo;
import org.dromara.daxpay.service.result.allocation.order.AllocOrderVo;
import org.springframework.stereotype.Service;

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

    private final AllocDetailManager allocOrderDetailManager;

    private final AllocOrderManager allocationOrderManager;

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

}

