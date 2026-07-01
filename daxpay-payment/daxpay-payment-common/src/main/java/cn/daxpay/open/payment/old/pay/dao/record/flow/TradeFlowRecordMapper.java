package cn.daxpay.open.payment.old.pay.dao.record.flow;

import cn.daxpay.open.payment.old.pay.entity.record.flow.TradeFlowRecord;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/// # 交易流水
///
@Mapper
public interface TradeFlowRecordMapper extends MPJBaseMapper<TradeFlowRecord> {

    @Select("select sum(amount)::bigint from pay_trade_flow_record ${ew.customSqlSegment}")
    Long getTotalAmount(@Param(Constants.WRAPPER) QueryWrapper<TradeFlowRecord> generator);
}
