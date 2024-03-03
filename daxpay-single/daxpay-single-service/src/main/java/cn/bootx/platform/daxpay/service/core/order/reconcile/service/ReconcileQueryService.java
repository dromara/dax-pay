package cn.bootx.platform.daxpay.service.core.order.reconcile.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.service.core.order.reconcile.dao.ReconcileDetailManager;
import cn.bootx.platform.daxpay.service.core.order.reconcile.dao.ReconcileOrderManager;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.ReconcileDetail;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.ReconcileOrder;
import cn.bootx.platform.daxpay.service.dto.order.reconcile.ReconcileDetailDto;
import cn.bootx.platform.daxpay.service.dto.order.reconcile.ReconcileOrderDto;
import cn.bootx.platform.daxpay.service.param.reconcile.ReconcileDetailQuery;
import cn.bootx.platform.daxpay.service.param.reconcile.ReconcileOrderQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 支付对账订单查询服务类
 * @author xxm
 * @since 2024/1/21
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReconcileQueryService {
    private final ReconcileOrderManager orderManager;
    private final ReconcileDetailManager detailManager;

    /**
     * 分页
     */
    public PageResult<ReconcileOrderDto> page(PageParam pageParam, ReconcileOrderQuery query){
        return MpUtil.convert2DtoPageResult(orderManager.page(pageParam, query));
    }

    /**
     * 对账订单
     */
    public ReconcileOrderDto findById(Long id){
        return orderManager.findById(id).map(ReconcileOrder::toDto)
                .orElseThrow(()->new DataNotExistException("对账订单不存在"));
    }

    /**
     * 明细分页
     */
    public PageResult<ReconcileDetailDto> pageDetail(PageParam pageParam, ReconcileDetailQuery query){
        return MpUtil.convert2DtoPageResult(detailManager.page(pageParam, query));
    }

    /**
     * 明细详情
     */
    public ReconcileDetailDto findDetailById(Long  id){
        return detailManager.findById(id).map(ReconcileDetail::toDto)
                .orElseThrow(()->new DataNotExistException("对账详情不存在"));
    }

}
