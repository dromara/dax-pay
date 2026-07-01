package cn.daxpay.open.payment.old.pay.dao.record.flow;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.enums.pay.trade.TradeTypeEnum;
import cn.daxpay.open.payment.old.pay.entity.record.flow.TradeFlowRecord;
import cn.daxpay.open.payment.old.pay.param.record.TradeFlowRecordQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/// # 交易流水
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class TradeFlowRecordManager extends BaseManager<TradeFlowRecordMapper, TradeFlowRecord> {

    /// 分页
    public Page<TradeFlowRecord> page(PageParam pageParam, TradeFlowRecordQuery param){
        Page<TradeFlowRecord> mpPage = MpUtil.getMpPage(pageParam, TradeFlowRecord.class);
        QueryWrapper<TradeFlowRecord> generator = QueryGenerator.generator(param);
        return page(mpPage, generator);
    }

    /// 查询汇总金额
    public Long getTotalAmount(TradeFlowRecordQuery query, TradeTypeEnum tradeTypeEnum){
        QueryWrapper<TradeFlowRecord> generator = QueryGenerator.generator(query);
        generator.eq(MpUtil.getColumnName(TradeFlowRecord::getType), tradeTypeEnum.getCode());
        return baseMapper.getTotalAmount(generator);
    }

}
