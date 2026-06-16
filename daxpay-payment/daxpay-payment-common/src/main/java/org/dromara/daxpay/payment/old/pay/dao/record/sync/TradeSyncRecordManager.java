package org.dromara.daxpay.payment.old.pay.dao.record.sync;

import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.platform.common.mybatisplus.query.generator.QueryGenerator;
import org.dromara.daxpay.platform.common.mybatisplus.util.MpUtil;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.payment.old.pay.entity.record.sync.TradeSyncRecord;
import org.dromara.daxpay.payment.old.pay.param.record.TradeSyncRecordQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;


@Slf4j
@Repository
@RequiredArgsConstructor
public class TradeSyncRecordManager extends BaseManager<TradeSyncRecordMapper, TradeSyncRecord> {

    /// 分页
    public Page<TradeSyncRecord> page(PageParam pageParam, TradeSyncRecordQuery query) {
        Page<TradeSyncRecord> mpPage = MpUtil.getMpPage(pageParam, TradeSyncRecord.class);
        QueryWrapper<TradeSyncRecord> generator = QueryGenerator.generator(query);
        return page(mpPage, generator);
    }

}
