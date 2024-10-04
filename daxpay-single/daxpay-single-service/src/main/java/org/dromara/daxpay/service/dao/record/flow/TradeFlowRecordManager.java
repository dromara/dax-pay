package org.dromara.daxpay.service.dao.record.flow;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import org.dromara.daxpay.core.enums.TradeTypeEnum;
import org.dromara.daxpay.service.entity.record.flow.TradeFlowRecord;
import org.dromara.daxpay.service.param.record.TradeFlowRecordQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * 交易流水
 * @author xxm
 * @since 2024/6/30
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

    /**
     * 查询汇总金额
     */
    public BigDecimal getTotalAmount(TradeFlowRecordQuery query, TradeTypeEnum tradeTypeEnum){
        QueryWrapper<TradeFlowRecord> generator = QueryGenerator.generator(query);
        generator.eq(MpUtil.getColumnName(TradeFlowRecord::getType), tradeTypeEnum.getCode());
        return baseMapper.getTotalAmount(generator);
    }

}
