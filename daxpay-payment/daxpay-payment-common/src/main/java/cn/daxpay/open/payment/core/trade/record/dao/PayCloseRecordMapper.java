package cn.daxpay.open.payment.core.trade.record.dao;

import cn.daxpay.open.payment.core.trade.record.entity.PayCloseRecord;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 支付关闭记录 Mapper
///
@Mapper
public interface PayCloseRecordMapper extends MPJBaseMapper<PayCloseRecord> {
}
