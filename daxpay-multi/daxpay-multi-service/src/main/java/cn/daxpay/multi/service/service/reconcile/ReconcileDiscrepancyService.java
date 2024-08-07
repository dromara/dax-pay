package cn.daxpay.multi.service.service.reconcile;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.daxpay.multi.service.dao.reconcile.ReconcileDiscrepancyManager;
import cn.daxpay.multi.service.entity.reconcile.ReconcileDiscrepancy;
import cn.daxpay.multi.service.param.reconcile.ReconcileDiscrepancyQuery;
import cn.daxpay.multi.service.result.reconcile.ReconcileDiscrepancyResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 对账差异记录服务
 * @author xxm
 * @since 2024/8/7
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReconcileDiscrepancyService {
    private final ReconcileDiscrepancyManager reconcileDiscrepancyManager;

    /**
     * 分页
     */
    public PageResult<ReconcileDiscrepancyResult> page(PageParam pageParam, ReconcileDiscrepancyQuery query) {
        return MpUtil.toPageResult(reconcileDiscrepancyManager.page(pageParam, query));
    }

    /**
     * 详情
     */
    public ReconcileDiscrepancyResult findById(Long id) {
        return reconcileDiscrepancyManager.findById(id).map(ReconcileDiscrepancy::toResult)
                .orElseThrow(() -> new DataNotExistException("对账差异记录不存在"));
    }
}
