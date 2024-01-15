package cn.bootx.platform.daxpay.service.core.record.sync.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.generator.QueryGenerator;
import cn.bootx.platform.daxpay.service.core.record.sync.entity.PaySyncRecord;
import cn.bootx.platform.daxpay.service.param.record.PaySyncRecordQuery;
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
public class PaySyncRecordManager extends BaseManager<PaySyncRecordMapper, PaySyncRecord> {

    /**
     * 分页
     */
    public Page<PaySyncRecord> page(PageParam pageParam, PaySyncRecordQuery query) {
        Page<PaySyncRecord> mpPage = MpUtil.getMpPage(pageParam, PaySyncRecord.class);
        QueryWrapper<PaySyncRecord> generator = QueryGenerator.generator(query);
        return page(mpPage, generator);
    }

}
