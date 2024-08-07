package cn.daxpay.multi.service.service.reconcile;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.daxpay.multi.service.dao.reconcile.ReconcileStatementManager;
import cn.daxpay.multi.service.entity.reconcile.ReconcileStatement;
import cn.daxpay.multi.service.param.reconcile.ReconcileStatementQuery;
import cn.daxpay.multi.service.result.reconcile.ReconcileStatementResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 对账查询服务
 * @author xxm
 * @since 2024/8/5
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReconcileStatementQueryService {
    private final ReconcileStatementManager orderManager;

    /**
     * 分页
     */
    public PageResult<ReconcileStatementResult> page(PageParam pageParam, ReconcileStatementQuery query){
        return MpUtil.toPageResult(orderManager.page(pageParam, query));
    }

    /**
     * 明细
     */
    public ReconcileStatementResult findById(Long id){
        return orderManager.findById(id).map(ReconcileStatement::toResult)
                .orElseThrow(()->new DataNotExistException("对账订单不存在"));
    }
}
