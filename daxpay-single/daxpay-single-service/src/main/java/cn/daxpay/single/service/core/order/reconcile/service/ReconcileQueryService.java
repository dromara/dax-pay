package cn.daxpay.single.service.core.order.reconcile.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.single.service.core.order.reconcile.dao.ReconcileOrderManager;
import cn.daxpay.single.service.core.order.reconcile.dao.ReconcileOutTradeManager;
import cn.daxpay.single.service.core.order.reconcile.entity.ReconcileOutTrade;
import cn.daxpay.single.service.core.order.reconcile.entity.ReconcileOrder;
import cn.daxpay.single.service.dto.order.reconcile.ReconcileOrderDto;
import cn.daxpay.single.service.dto.order.reconcile.ReconcileOutTradeDto;
import cn.daxpay.single.service.param.reconcile.ReconcileTradeDetailQuery;
import cn.daxpay.single.service.param.reconcile.ReconcileOrderQuery;
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
    private final ReconcileOutTradeManager detailManager;

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
     * 通道交易明细分页
     */
    public PageResult<ReconcileOutTradeDto> pageDetail(PageParam pageParam, ReconcileTradeDetailQuery query){
        return MpUtil.convert2DtoPageResult(detailManager.page(pageParam, query));
    }

    /**
     * 通道交易详情
     */
    public ReconcileOutTradeDto findDetailById(Long  id){
        return detailManager.findById(id).map(ReconcileOutTrade::toDto)
                .orElseThrow(()->new DataNotExistException("对账详情不存在"));
    }
}
