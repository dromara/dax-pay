package cn.daxpay.single.service.core.order.reconcile.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.single.service.core.order.reconcile.dao.ReconcileDiffManager;
import cn.daxpay.single.service.core.order.reconcile.entity.ReconcileDiff;
import cn.daxpay.single.service.dto.order.reconcile.ReconcileDiffDto;
import cn.daxpay.single.service.param.reconcile.ReconcileDiffQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 对账差异单服务
 * @author xxm
 * @since 2024/2/29
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReconcileDiffService {
    private final ReconcileDiffManager recordManager;

    /**
     * 批量保存
     */
    public void saveAll(List<ReconcileDiff> diffRecords) {
        recordManager.saveAll(diffRecords);
    }

    /**
     * 分页查询
     */
    public PageResult<ReconcileDiffDto> page(PageParam pageParam, ReconcileDiffQuery query) {
        return MpUtil.convert2DtoPageResult(recordManager.page(pageParam, query));
    }

    /**
     * 详情查询
     */
    public ReconcileDiffDto findById(Long id) {
        return recordManager.findById(id).map(ReconcileDiff::toDto).orElseThrow(() -> new DataNotExistException("不存在的对账差异单"));
    }
}
