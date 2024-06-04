package cn.daxpay.single.service.core.record.flow.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.generator.QueryGenerator;
import cn.daxpay.single.service.core.record.flow.entity.TradeFlowRecord;
import cn.daxpay.single.service.param.record.TradeFlowRecordQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xxm
 * @since 2024/4/21
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class TradeFlowRecordManager extends BaseManager<TradeFlowRecordMapper, TradeFlowRecord> {

    /**
     * 分页
     */
    public Page<TradeFlowRecord> page(PageParam pageParam, TradeFlowRecordQuery param){
        Page<TradeFlowRecord> mpPage = MpUtil.getMpPage(pageParam, TradeFlowRecord.class);
        QueryWrapper<TradeFlowRecord> generator = QueryGenerator.generator(param);
        return page(mpPage, generator);
    }
}
