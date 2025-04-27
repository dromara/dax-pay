package org.dromara.daxpay.service.dao.record.flow;

import org.dromara.daxpay.service.entity.record.flow.TradeFlowRecord;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 * 交易流水
 * @author xxm
 * @since 2024/6/30
 */
@Mapper
public interface TradeFlowRecordMapper extends MPJBaseMapper<TradeFlowRecord> {

    @Select("select sum(amount) from pay_trade_flow_record ${ew.customSqlSegment}")
    BigDecimal getTotalAmount(@Param(Constants.WRAPPER) QueryWrapper<TradeFlowRecord> generator);
}
