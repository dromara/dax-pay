package cn.daxpay.single.service.core.record.repair.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.generator.QueryGenerator;
import cn.daxpay.single.service.core.record.repair.entity.TradeAdjustRecord;
import cn.daxpay.single.service.param.report.TradeAdjustRecordQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xxm
 * @since 2024/7/15
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class TradeAdjustRecordManager extends BaseManager<TradeAdjustRecordMapper, TradeAdjustRecord> {

    public Page<TradeAdjustRecord> page(PageParam pageParam, TradeAdjustRecordQuery param){
        QueryWrapper<TradeAdjustRecord> generator = QueryGenerator.generator(param);
        Page<TradeAdjustRecord> mpPage = MpUtil.getMpPage(pageParam, TradeAdjustRecord.class);

        return this.page(mpPage, generator);

    }
}
