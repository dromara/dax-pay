package cn.daxpay.open.payment.trade.record.dao;

import cn.daxpay.open.payment.trade.record.entity.PaySyncRecord;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 支付同步记录 Mapper
///
@Mapper
public interface PaySyncRecordMapper extends MPJBaseMapper<PaySyncRecord> {
}
