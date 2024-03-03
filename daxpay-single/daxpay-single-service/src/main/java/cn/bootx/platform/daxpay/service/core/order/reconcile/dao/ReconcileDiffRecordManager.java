package cn.bootx.platform.daxpay.service.core.order.reconcile.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.generator.QueryGenerator;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.ReconcileDiffRecord;
import cn.bootx.platform.daxpay.service.param.reconcile.ReconcileDiffQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xxm
 * @since 2024/2/28
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class ReconcileDiffRecordManager extends BaseManager<ReconcileDiffRecordMapper, ReconcileDiffRecord> {

    /**
     * 分页
     */
    public Page<ReconcileDiffRecord> page(PageParam pageParam, ReconcileDiffQuery query){
        Page<ReconcileDiffRecord> mpPage = MpUtil.getMpPage(pageParam, ReconcileDiffRecord.class);
        QueryWrapper<ReconcileDiffRecord> generator = QueryGenerator.generator(query);
        return this.page(mpPage,generator);
    }
}
