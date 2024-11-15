package org.dromara.daxpay.service.service.allocation.transaction;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.core.result.allocation.transaction.AllocDetailResult;
import org.dromara.daxpay.core.result.allocation.transaction.AllocTransactionResult;
import org.dromara.daxpay.service.dao.allocation.transaction.AllocDetailManager;
import org.dromara.daxpay.service.dao.allocation.transaction.AllocTransactionManager;
import org.dromara.daxpay.service.entity.allocation.transaction.AllocDetail;
import org.dromara.daxpay.service.entity.allocation.transaction.AllocTransaction;
import org.dromara.daxpay.service.param.order.allocation.AllocOrderQuery;
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
public class AllocQueryService {

    private final AllocDetailManager allocOrderDetailManager;

    private final AllocTransactionManager allocationOrderManager;

    /**
     * 分页查询
     */
    public PageResult<AllocTransactionResult> page(PageParam pageParam, AllocOrderQuery param){
        return MpUtil.toPageResult(allocationOrderManager.page(pageParam, param));
    }

    /**
     * 查询详情
     */
    public AllocTransactionResult findById(Long id) {
        return allocationOrderManager.findById(id).map(AllocTransaction::toResult).orElseThrow(() -> new DataNotExistException("分账订单不存在"));
    }

    /**
     * 查询订单明细列表
     */
    public List<AllocDetailResult> findDetailsByOrderId(Long orderId){
        return MpUtil.toListResult(allocOrderDetailManager.findAllByOrderId(orderId));
    }

    /**
     * 查询订单明细详情
     */
    public AllocDetailResult findDetailById(Long id){
        return allocOrderDetailManager.findById(id).map(AllocDetail::toResult).orElseThrow(() -> new DataNotExistException("分账订单明细不存在"));
    }

}

