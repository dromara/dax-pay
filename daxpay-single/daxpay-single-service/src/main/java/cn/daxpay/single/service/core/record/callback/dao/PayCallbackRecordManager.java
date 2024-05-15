package cn.daxpay.single.service.core.record.callback.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.generator.QueryGenerator;
import cn.daxpay.single.service.core.record.callback.entity.PayCallbackRecord;
import cn.daxpay.single.service.param.record.PayCallbackRecordQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * 支付回调通知
 * @author xxm
 * @since 2023/12/18
 */
@Slf4j
@Repository
public class PayCallbackRecordManager extends BaseManager<PayCallbackRecordMapper, PayCallbackRecord> {

    /**
     * 分页
     */
    public Page<PayCallbackRecord> page(PageParam pageParam, PayCallbackRecordQuery query){
        Page<PayCallbackRecord> mpPage = MpUtil.getMpPage(pageParam, PayCallbackRecord.class);
        QueryWrapper<PayCallbackRecord> generator = QueryGenerator.generator(query);
        return page(mpPage, generator);
    }
}
