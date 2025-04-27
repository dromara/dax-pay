package org.dromara.daxpay.service.dao.record.sync;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import org.dromara.daxpay.service.entity.record.sync.TradeSyncRecord;
import org.dromara.daxpay.service.param.record.TradeSyncRecordQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xxm
 * @since 2023/7/14
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class TradeSyncRecordManager extends BaseManager<TradeSyncRecordMapper, TradeSyncRecord> {

    /**
     * 分页
     */
    public Page<TradeSyncRecord> page(PageParam pageParam, TradeSyncRecordQuery query) {
        Page<TradeSyncRecord> mpPage = MpUtil.getMpPage(pageParam, TradeSyncRecord.class);
        QueryWrapper<TradeSyncRecord> generator = QueryGenerator.generator(query);
        return page(mpPage, generator);
    }

}
