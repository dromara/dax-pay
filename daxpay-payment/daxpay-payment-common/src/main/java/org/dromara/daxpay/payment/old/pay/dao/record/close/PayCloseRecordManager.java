package org.dromara.daxpay.payment.old.pay.dao.record.close;

import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.platform.common.mybatisplus.query.generator.QueryGenerator;
import org.dromara.daxpay.platform.common.mybatisplus.util.MpUtil;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.payment.old.pay.entity.record.close.PayCloseRecord;
import org.dromara.daxpay.payment.old.pay.param.record.PayCloseRecordQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;


@Slf4j
@Repository
@RequiredArgsConstructor
public class PayCloseRecordManager extends BaseManager<PayCloseRecordMapper, PayCloseRecord> {

    /// 分页
    public Page<PayCloseRecord> page(PageParam pageParam, PayCloseRecordQuery param){
        Page<PayCloseRecord> mpPage = MpUtil.getMpPage(pageParam, PayCloseRecord.class);
        QueryWrapper<PayCloseRecord> generator = QueryGenerator.generator(param);
        return page(mpPage, generator);
    }
}
