package cn.daxpay.open.payment.old.pay.dao.record.callback;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.payment.old.pay.entity.record.callback.TradeCallbackRecord;
import cn.daxpay.open.payment.old.pay.param.record.TradeCallbackRecordQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class TradeCallbackRecordManager extends BaseManager<TradeCallbackRecordMapper, TradeCallbackRecord> {

    /// 分页
    public Page<TradeCallbackRecord> page(PageParam pageParam, TradeCallbackRecordQuery query){
        Page<TradeCallbackRecord> mpPage = MpUtil.getMpPage(pageParam, TradeCallbackRecord.class);
        QueryWrapper<TradeCallbackRecord> generator = QueryGenerator.generator(query);
        return page(mpPage, generator);
    }
}
