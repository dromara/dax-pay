package cn.daxpay.open.payment.trade.record.dao;

import cn.daxpay.open.payment.trade.record.entity.PayCallbackRecord;
import cn.daxpay.open.payment.trade.record.param.PayCallbackRecordQuery;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Repository;

/// # 通道入站回调记录管理器
///
@Repository
public class PayCallbackRecordManager extends BaseManager<PayCallbackRecordMapper, PayCallbackRecord> {

    /// 分页查询
    public Page<PayCallbackRecord> page(PageParam pageParam, PayCallbackRecordQuery query) {
        Page<PayCallbackRecord> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<PayCallbackRecord> wrapper = QueryGenerator.generator(query);
        wrapper.lambda().orderByDesc(PayCallbackRecord::getId);
        return this.page(mpPage, wrapper);
    }
}
